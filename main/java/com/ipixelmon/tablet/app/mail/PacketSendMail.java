package com.ipixelmon.tablet.app.mail;

import com.ipixelmon.notification.NotificationMod;
import com.ipixelmon.tablet.app.mail.client.ClientProxy;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketSendMail implements IMessage {

    private Mail mail;

    public PacketSendMail() {}

    public PacketSendMail(Mail mail) {
        this.mail = mail;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        mail = new Mail();
        mail.fromBytes(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        mail.toBytes(buf);
    }

    public static class Handler implements IMessageHandler<PacketSendMail, IMessage> {

        @Override
        public IMessage onMessage(PacketSendMail message, MessageContext ctx) {
            ClientProxy.mail.add(message.mail);
            NotificationMod.newSimpleNotification("New mail from: " + message.mail.getSenderName(), 5);
            return null;
        }

    }
}
