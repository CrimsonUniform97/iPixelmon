package com.ipixelmon.tablet.apps.mail.packet;

import com.ipixelmon.PlayerUtil;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.mysql.InsertForm;
import com.ipixelmon.tablet.Tablet;
import com.ipixelmon.uuidmanager.UUIDManager;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by colby on 12/14/2016.
 */
public class PacketSendMail implements IMessage {

    public PacketSendMail() {
    }

    public static DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    public static boolean checkChar(char c) {
        Pattern p = Pattern.compile("[^a-z0-9 ,]", Pattern.CASE_INSENSITIVE);
        return p.matcher("" + c).find();
    }

    private String message;
    private String[] players;

    public PacketSendMail(String message, String... players) {
        this.message = message;
        this.players = players;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        message = ByteBufUtils.readUTF8String(buf);
        players = ByteBufUtils.readUTF8String(buf).split(",");
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, message);

        String toSend = "";
        for (String s : players) toSend += s + ",";

        ByteBufUtils.writeUTF8String(buf, toSend);
    }

    public static class Handler implements IMessageHandler<PacketSendMail, IMessage> {

        @Override
        public IMessage onMessage(PacketSendMail message, MessageContext ctx) {

            String playersNotFound = "";
            List<UUID> players = new ArrayList<>();
            UUID playerUUID;
            for(String player : message.players) {
                if(!player.isEmpty() || player != null) {
                    playerUUID = UUIDManager.getUUID(player);
                    if(playerUUID != null) {
                        players.add(playerUUID);
                    } else {
                        playersNotFound += player + ",";
                    }
                }
            }

            if(!playersNotFound.isEmpty()) {
                playersNotFound = playersNotFound.substring(0, playersNotFound.length() - 1);
                iPixelmon.network.sendTo(new PacketSendResponse(false, "Players not found " + playersNotFound + "."),
                        ctx.getServerHandler().playerEntity);
                return null;
            }

            if(players.isEmpty()) {
                iPixelmon.network.sendTo(new PacketSendResponse(false, "Players not found."),
                        ctx.getServerHandler().playerEntity);
                return null;
            }

            Date today = Calendar.getInstance().getTime();
            String reportDate = dateFormat.format(today);


            Iterator iterator = players.listIterator();

            UUID player;
            while(iterator.hasNext()) {
                player = (UUID) iterator.next();
                if(PlayerUtil.isPlayerOnline(player)) {
                    iPixelmon.network.sendTo(new PacketReceiveMail(reportDate,
                            ctx.getServerHandler().playerEntity.getName(), message.message),
                            PlayerUtil.getPlayer(player));
                    iterator.remove();
                }
            }

// TODO: Can't use ' because mysql messes up
            if(!players.isEmpty()) {
                for (UUID p : players) {
                    InsertForm insertForm = new InsertForm("Mail");
                    insertForm.add("sentDate", reportDate);
                    insertForm.add("receiver", p.toString());
                    insertForm.add("sender", ctx.getServerHandler().playerEntity.getUniqueID().toString());
                    insertForm.add("message", message.message);

                    iPixelmon.mysql.insert(Tablet.class, insertForm);
                }
            }

            iPixelmon.network.sendTo(new PacketSendResponse(true, ""), ctx.getServerHandler().playerEntity);
            return null;
        }

    }

}
