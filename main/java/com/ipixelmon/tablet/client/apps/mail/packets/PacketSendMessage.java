package com.ipixelmon.tablet.client.apps.mail.packets;

import com.google.common.collect.Lists;
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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

/**
 * Created by colby on 12/9/2016.
 */
public class PacketSendMessage implements IMessage {

    UUID messageID;
    String message;
    String[] players;

    public PacketSendMessage() {
    }

    public PacketSendMessage(String message, UUID messageID) {
        this.message = message;
        this.messageID = messageID;
    }

    public PacketSendMessage(String message, String... players) {
        this.message = message;
        this.players = players;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        message = ByteBufUtils.readUTF8String(buf);
        if (buf.readBoolean())
            messageID = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        else
            players = ByteBufUtils.readUTF8String(buf).split(",");

    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, message);
        buf.writeBoolean(messageID != null);
        if (messageID != null) {
            ByteBufUtils.writeUTF8String(buf, messageID.toString());
        } else {
            String players = "";

            if (this.players.length > 10) return;

            for (String player : this.players) players += player + ",";

            ByteBufUtils.writeUTF8String(buf, players);
        }
    }

    public static class Handler implements IMessageHandler<PacketSendMessage, IMessage> {

        @Override
        public IMessage onMessage(PacketSendMessage message, MessageContext ctx) {
            try {
                // if conversation already exists update
                if (message.messageID != null) {
                    ResultSet resultSet = iPixelmon.mysql.query("SELECT * FROM tabletMessages WHERE messageID='" + message.messageID.toString() + "';");

                    if (resultSet.next()) {
                        List<UUID> validPlayers = Lists.newArrayList();
                        for (String s : resultSet.getString("players").split(","))
                            if (s != null && !s.isEmpty())
                                validPlayers.add(UUID.fromString(s));


                        if (!validPlayers.contains(ctx.getServerHandler().playerEntity.getUniqueID())) return null;

                        for (UUID p : validPlayers)
                            if (PlayerUtil.isPlayerOnline(p))
                                iPixelmon.network.sendTo(new PacketReceiveMessage(message.messageID, ctx.getServerHandler().playerEntity.getUniqueID(), message.message), PlayerUtil.getPlayer(p));
                    }
                } else {
                    UUID messageID = UUID.randomUUID();

                    if (message.players.length > 10) return null;

                    String players = "";

                    List<UUID> validPlayers = Lists.newArrayList();

                    UUID uuid;
                    for (String s : message.players) {
                        uuid = UUIDManager.getUUID(s);
                        if (uuid != null) {
                            players += uuid.toString() + ",";
                            validPlayers.add(uuid);
                        }
                    }

                    players += ctx.getServerHandler().playerEntity.getUniqueID().toString() + ",";
                    validPlayers.add(ctx.getServerHandler().playerEntity.getUniqueID());

                    iPixelmon.mysql.insert(Tablet.class, new InsertForm("Messages").add("messageID", messageID.toString()).add("players", players));

                    for (UUID p : validPlayers)
                        if (PlayerUtil.isPlayerOnline(p))
                            iPixelmon.network.sendTo(new PacketReceiveMessage(messageID, ctx.getServerHandler().playerEntity.getUniqueID(), message.message), PlayerUtil.getPlayer(p));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

}
