package com.ipixelmon.itemdisplay;

import io.netty.buffer.ByteBuf;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketUpdateTileEntity implements IMessage {

    private BlockPos tilePos;
    private int scale;
    private double xOffset, yOffset, zOffset;

    public PacketUpdateTileEntity() {
    }

    public PacketUpdateTileEntity(BlockPos tilePos, int scale, double xOffset, double yOffset, double zOffset) {
        this.tilePos = tilePos;
        this.scale = scale;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.zOffset = zOffset;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        tilePos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        scale = buf.readInt();
        xOffset = buf.readDouble();
        yOffset = buf.readDouble();
        zOffset = buf.readDouble();

        if(xOffset == Double.NaN) xOffset = 0;
        if(yOffset == Double.NaN) yOffset = 0;
        if(zOffset == Double.NaN) zOffset = 0;
        if(scale == -999) scale = 1;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(tilePos.getX());
        buf.writeInt(tilePos.getY());
        buf.writeInt(tilePos.getZ());
        buf.writeInt(scale);
        buf.writeDouble(xOffset);
        buf.writeDouble(yOffset);
        buf.writeDouble(zOffset);
    }

    public static class Handler implements IMessageHandler<PacketUpdateTileEntity, IMessage> {

        @Override
        public IMessage onMessage(PacketUpdateTileEntity message, MessageContext ctx) {
            TileEntity tileEntity = ctx.getServerHandler().playerEntity.worldObj.getTileEntity(message.tilePos);
            if(tileEntity == null) return null;
            if(!(tileEntity instanceof ItemDisplayBlockTileEntity)) return null;

            ItemDisplayBlockTileEntity te = (ItemDisplayBlockTileEntity) tileEntity;
            te.setScale(message.scale);
            te.setxOffset(message.xOffset);
            te.setyOffset(message.yOffset);
            te.setzOffset(message.zOffset);
            return null;
        }

    }

}
