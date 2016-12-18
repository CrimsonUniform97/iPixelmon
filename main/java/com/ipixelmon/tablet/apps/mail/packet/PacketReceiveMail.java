package com.ipixelmon.tablet.apps.mail.packet;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.apps.mail.Mail;
import com.ipixelmon.tablet.apps.mail.MailObject;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.Date;

/**
 * Created by colby on 12/14/2016.
 */
public class PacketReceiveMail implements IMessage {

    public PacketReceiveMail() {
    }

    private String date, sender, message;

    public PacketReceiveMail(String date, String sender, String message) {
        this.date = date;
        this.sender = sender;
        this.message = message;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        sender = ByteBufUtils.readUTF8String(buf);
        message = ByteBufUtils.readUTF8String(buf);
        date = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, sender);
        ByteBufUtils.writeUTF8String(buf, message);
        ByteBufUtils.writeUTF8String(buf, date);
    }

    public static class Handler implements IMessageHandler<PacketReceiveMail, IMessage> {

        @Override
        public IMessage onMessage(PacketReceiveMail message, MessageContext ctx) {
            Date sentDate;

            try {
                sentDate = PacketSendMail.dateFormat.parse(message.date);
            } catch (ParseException e) {
                e.printStackTrace();
                return null;
            }

// TODO: Add notification
            Mail.mail.add(new MailObject(sentDate, message.sender, message.message, false));
            try {
                iPixelmon.clientDb.query("INSERT INTO tabletMail (sentDate, sender, message, read) VALUES" +
                        " ('" + message.date + "', '" + message.sender + "', '" + message.message + "', '0');");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
