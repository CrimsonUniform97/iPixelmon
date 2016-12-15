package com.ipixelmon.tablet.apps.mail.packet;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by colby on 12/14/2016.
 */
public class PacketReceiveMail implements IMessage {

    public PacketReceiveMail() {
    }

    public PacketReceiveMail(String s) {
    }

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    public static class Handler implements IMessageHandler<PacketReceiveMail, IMessage> {

        @Override
        public IMessage onMessage(PacketReceiveMail message, MessageContext ctx) {
            return null;
        }
    }
}
