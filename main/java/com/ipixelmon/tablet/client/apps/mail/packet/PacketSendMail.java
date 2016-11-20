package com.ipixelmon.tablet.client.apps.mail.packet;

import com.ipixelmon.PlayerUtil;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.mysql.InsertForm;
import com.ipixelmon.mysql.SelectionForm;
import com.ipixelmon.tablet.Tablet;
import com.ipixelmon.tablet.client.apps.mail.Mail;
import com.ipixelmon.uuidmanager.UUIDManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

/**
 * Created by colby on 11/18/2016.
 */
public class PacketSendMail implements IMessage {

    public PacketSendMail() {
    }

    private String message;
    private String playerName;

    public PacketSendMail(String message, String playerName) {
        this.message = message;
        this.playerName = playerName;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        message = ByteBufUtils.readUTF8String(buf);
        playerName = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, message);
        ByteBufUtils.writeUTF8String(buf, playerName);
    }

    public static class Handler implements IMessageHandler<PacketSendMail, IMessage> {

        @Override
        public IMessage onMessage(PacketSendMail message, MessageContext ctx) {
            Date today = Calendar.getInstance().getTime();
            UUID playerUUID = UUIDManager.getUUID(message.playerName);

            if (playerUUID == null) return null;

            EntityPlayerMP player = PlayerUtil.getPlayer(playerUUID);

            UUID mailID = UUID.randomUUID();
            InsertForm insertForm = new InsertForm("mail");
            insertForm.add("mailID", mailID.toString());
            insertForm.add("receiver", playerUUID.toString());
            insertForm.add("sender", ctx.getServerHandler().playerEntity.getUniqueID().toString());
            insertForm.add("message", message.message);
            insertForm.add("sentDate", PacketReceiveMail.df.format(today));
            insertForm.add("hasRead", false);
            iPixelmon.mysql.insert(Tablet.class, insertForm);

            ResultSet result = iPixelmon.mysql.selectAllFrom(Tablet.class, new SelectionForm("mail").where("mailID", mailID.toString()));
            boolean failed = false;
            try {
                if(!result.next()) {
                    failed = true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
                failed = true;
            }

            iPixelmon.network.sendTo(new PacketMailConfirmation(failed), ctx.getServerHandler().playerEntity);

            if (player != null && !failed) iPixelmon.network.sendTo(new PacketReceiveMail(mailID), player);

            return null;
        }

    }
}
