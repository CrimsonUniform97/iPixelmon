package com.ipixelmon.landcontrol.toolCupboard.packet;

import com.ipixelmon.landcontrol.client.PlayerListener;
import com.ipixelmon.landcontrol.toolCupboard.ToolCupboardTileEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketSetSelectedTile implements IMessage {

    private BlockPos pos;

    public PacketSetSelectedTile() {
    }

    public PacketSetSelectedTile(BlockPos pos) {
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

    public static class Handler implements IMessageHandler<PacketSetSelectedTile, IMessage> {

        @Override
        public IMessage onMessage(PacketSetSelectedTile message, MessageContext ctx) {
            doMessage(message);
            return null;
        }

        @SideOnly(Side.CLIENT)
        private void doMessage(PacketSetSelectedTile m) {
            if(PlayerListener.selectedTile != null) {
                if(PlayerListener.selectedTile.getPos().equals(m.pos)) {
                    PlayerListener.selectedTile = null;
                    return;
                }
            }

            PlayerListener.selectedTile = (ToolCupboardTileEntity) Minecraft.getMinecraft().thePlayer.getEntityWorld().getTileEntity(m.pos);
        }

    }

}
