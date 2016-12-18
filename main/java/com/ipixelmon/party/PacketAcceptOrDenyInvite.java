package com.ipixelmon.party;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by colbymchenry on 12/18/16.
 */
public class PacketAcceptOrDenyInvite implements IMessage{

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    public static class Handler implements IMessageHandler<PacketAcceptOrDenyInvite, IMessage> {

        @Override
        public IMessage onMessage(PacketAcceptOrDenyInvite message, MessageContext ctx) {
            return null;
        }

    }
}
