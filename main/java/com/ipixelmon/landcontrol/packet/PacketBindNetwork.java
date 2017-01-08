package com.ipixelmon.landcontrol.packet;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.toolCupboard.Network;
import com.ipixelmon.landcontrol.toolCupboard.ToolCupboardTileEntity;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by colby on 1/7/2017.
 */
public class PacketBindNetwork implements IMessage {

    private BlockPos pos;

    public PacketBindNetwork() {
    }

    public PacketBindNetwork(BlockPos pos) {
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

    public static class Handler implements IMessageHandler<PacketBindNetwork, IMessage> {

        @Override
        public IMessage onMessage(PacketBindNetwork message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;

            if(player.worldObj.getTileEntity(message.pos) == null) return null;
            if(!(player.worldObj.getTileEntity(message.pos) instanceof ToolCupboardTileEntity)) return null;

            ToolCupboardTileEntity tileEntity = (ToolCupboardTileEntity) player.worldObj.getTileEntity(message.pos);
            tileEntity = tileEntity.getBaseTile();

            Network network = new Network(player.getUniqueID());
            network.create();

            tileEntity.setNetwork(network);

            iPixelmon.network.sendTo(new PacketOpenGui(tileEntity, network.getPlayerMap()), player);
            return null;
        }

    }

}
