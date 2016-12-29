package com.ipixelmon.mcstats;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by colbymchenry on 12/29/16.
 */
public class PacketUpdateEXP implements IMessage {

    public PacketUpdateEXP() {
    }

    private long exp;

    public PacketUpdateEXP(long exp) {
        this.exp = exp;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        exp = buf.readLong();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeLong(exp);
    }

    public static class Handler implements IMessageHandler<PacketUpdateEXP, IMessage> {

        @Override
        public IMessage onMessage(PacketUpdateEXP message, MessageContext ctx) {
            McStatsAPI.Client.EXP = message.exp;
            return null;
        }

    }
}
