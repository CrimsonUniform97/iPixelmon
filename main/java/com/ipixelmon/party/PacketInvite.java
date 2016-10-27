package com.ipixelmon.party;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

/**
 * Created by colby on 10/27/2016.
 */
public class PacketInvite implements IMessage {

    private UUID sender;


    public PacketInvite() {}

    public PacketInvite(UUID sender) {
        this.sender = sender;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        sender = UUID.fromString(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, sender.toString());
    }

    public static class Handler implements IMessageHandler<PacketInvite, IMessage> {

        @Override
        public IMessage onMessage(PacketInvite message, MessageContext ctx) {
            // TODO:
            return null;
        }
    }

}
