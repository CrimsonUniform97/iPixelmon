package com.ipixelmon.itemdisplay;

import com.ipixelmon.iPixelmon;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketOpenGui implements IMessage {

    private BlockPos tilePos;

    public PacketOpenGui() {
    }

    public PacketOpenGui(BlockPos tilePos) {
        this.tilePos = tilePos;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        tilePos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(tilePos.getX());
        buf.writeInt(tilePos.getY());
        buf.writeInt(tilePos.getZ());
    }

    public static class Handler implements IMessageHandler<PacketOpenGui, IMessage> {

        @Override
        public IMessage onMessage(PacketOpenGui message, MessageContext ctx) {
            handle(message);
            return null;
        }

        @SideOnly(Side.CLIENT)
        public void handle(PacketOpenGui message) {
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    World world = iPixelmon.proxy.getDefaultWorld();
                    if(world == null) return;

                    TileEntity te = world.getTileEntity(message.tilePos);
                    if(te == null) return;
                    if(!(te instanceof ItemDisplayBlockTileEntity)) return;

                    Minecraft.getMinecraft().displayGuiScreen(new ItemDisplayBlockGui((ItemDisplayBlockTileEntity) te));
                }
            });
        }
    }

}
