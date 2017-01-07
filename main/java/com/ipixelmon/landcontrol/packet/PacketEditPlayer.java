package com.ipixelmon.landcontrol.packet;

import com.ipixelmon.landcontrol.ToolCupboardTileEntity;
import com.ipixelmon.uuidmanager.UUIDManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

/**
 * Created by colby on 1/7/2017.
 */
public class PacketEditPlayer implements IMessage {

    private String player;
    private BlockPos pos;
    private boolean add;

    public PacketEditPlayer() {
    }

    public PacketEditPlayer(BlockPos pos, String player, boolean add) {
        this.pos = pos;
        this.player = player;
        this.add = add;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
        player = ByteBufUtils.readUTF8String(buf);
        add = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(pos.getX());
        buf.writeInt(pos.getY());
        buf.writeInt(pos.getZ());
        ByteBufUtils.writeUTF8String(buf, player);
        buf.writeBoolean(add);
    }

    public static class Handler implements IMessageHandler<PacketEditPlayer, IMessage> {

        @Override
        public IMessage onMessage(PacketEditPlayer message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            World world = player.getEntityWorld();

            ToolCupboardTileEntity tileEntity = (ToolCupboardTileEntity) world.getTileEntity(message.pos);

            if (tileEntity == null) return null;

            if(!tileEntity.haveAccess.contains(player.getUniqueID())) return null;

            UUID playerToEdit = UUIDManager.getUUID(message.player);

            if (playerToEdit == null) return null;

            if (message.add)
                tileEntity.getPlayers().put(playerToEdit, UUIDManager.getPlayerName(playerToEdit));
            else
                tileEntity.getPlayers().remove(playerToEdit);

            tileEntity.markDirty();
            world.markBlockForUpdate(message.pos);

            return null;
        }

    }

}
