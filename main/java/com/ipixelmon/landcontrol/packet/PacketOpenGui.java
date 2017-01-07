package com.ipixelmon.landcontrol.packet;

import com.ipixelmon.landcontrol.ToolCupboardTileEntity;
import com.ipixelmon.landcontrol.gui.ToolCupboardGui;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by colby on 1/7/2017.
 */
public class PacketOpenGui implements IMessage {

    private BlockPos pos;

    public PacketOpenGui() {
    }

    public PacketOpenGui(BlockPos pos) {
        this.pos = pos;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
    }

    public static class Handler implements IMessageHandler<PacketOpenGui, IMessage> {

        @Override
        public IMessage onMessage(PacketOpenGui message, MessageContext ctx) {
            onMessage(message);
            return null;
        }

        @SideOnly(Side.CLIENT)
        private void onMessage(PacketOpenGui message) {
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    World world = Minecraft.getMinecraft().theWorld;
                    ToolCupboardTileEntity tileEntity = (ToolCupboardTileEntity) world.getTileEntity(message.pos);

                    if(tileEntity != null)
                        Minecraft.getMinecraft().displayGuiScreen(new ToolCupboardGui(tileEntity));
                }
            });

        }

    }
}
