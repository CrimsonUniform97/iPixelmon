package com.ipixelmon.mcstats;

import com.ipixelmon.mcstats.client.EXPAnimation;
import com.ipixelmon.mcstats.client.PlayerListener;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.Calendar;

/**
 * Created by colbymchenry on 12/29/16.
 */
public class PacketBrokeBlock implements IMessage {

    public PacketBrokeBlock() {
    }

    private int x, y, z, exp;

    public PacketBrokeBlock(int x, int y, int z, int exp) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.exp = exp;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        x = buf.readInt();
        y = buf.readInt();
        z = buf.readInt();
        exp = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(x);
        buf.writeInt(y);
        buf.writeInt(z);
        buf.writeInt(exp);
    }

    public static class Handler implements IMessageHandler<PacketBrokeBlock, IMessage> {

        @Override
        public IMessage onMessage(PacketBrokeBlock message, MessageContext ctx) {
            McStatsAPI.Client.renderPos = new BlockPos(message.x, message.y, message.z);
            McStatsAPI.Client.renderStartTime = Calendar.getInstance().getTime();
            McStatsAPI.Client.renderEXP = message.exp;
            McStatsAPI.Client.expAnimation = null;
            McStatsAPI.Client.expAnimation = new EXPAnimation();
            return null;
        }

    }

}
