package com.ipixelmon.tablet.client.apps.mail.packet;

import com.ipixelmon.tablet.client.apps.mail.Mail;
import com.ipixelmon.tablet.client.apps.mail.MailObject;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.UUID;

/**
 * Created by colby on 11/18/2016.
 */
public class PacketReceiveMail implements IMessage {

    public static final DateFormat df = new SimpleDateFormat("MM/dd/yyyy hh:mm a");

    public PacketReceiveMail() {
    }

    private UUID mailID;

    public PacketReceiveMail(UUID mailID) {
        this.mailID = mailID;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        mailID = UUID.fromString(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, mailID.toString());
    }

    public static class Handler implements IMessageHandler<PacketReceiveMail, IMessage> {

        @Override
        public IMessage onMessage(PacketReceiveMail message, MessageContext ctx) {
            Mail.messages.add(new MailObject(message.mailID));
            return null;
        }

    }

}
