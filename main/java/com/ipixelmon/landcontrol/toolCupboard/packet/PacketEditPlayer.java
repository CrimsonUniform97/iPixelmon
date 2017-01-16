package com.ipixelmon.landcontrol.toolCupboard.packet;

import com.google.common.collect.Lists;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.toolCupboard.Network;
import com.ipixelmon.landcontrol.toolCupboard.ToolCupboardTileEntity;
import com.ipixelmon.util.ArrayUtil;
import com.ipixelmon.uuidmanager.UUIDManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.List;
import java.util.Map;
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
            if (tileEntity.getBaseTile() == null) return null;

            tileEntity = tileEntity.getBaseTile();

            if(!tileEntity.getAccessSet().contains(player.getUniqueID())) return null;



            Network network = tileEntity.getNetwork();

            // stop players that aren't in the network from adding players
            if(network.exists() && !network.getPlayers().contains(player.getUniqueID())) return null;

            /**
             * ADD
             */
            if (message.add) {
                UUID playerToEdit = UUIDManager.getUUID(message.player);

                if (playerToEdit == null) {
                    iPixelmon.network.sendTo(new PacketGuiResponse("Player does not exist."), player);
                    return null;
                }

                if(!network.exists()) network.create();
                network.addPlayer(playerToEdit);
            }
            /**
             * REMOVE
             */
            else {
              if(!network.exists()) return null;

              UUID playerToEdit = UUID.fromString(message.player);

              if(playerToEdit.equals(network.getID())) return null;

              network.removePlayer(playerToEdit);

//              if(network.getPlayers().isEmpty()) network.delete();
            }

            List<String> players = Lists.newArrayList();
            Map<UUID, String> playerMap = network.getPlayerMap();

            for(UUID id : playerMap.keySet()) players.add(id.toString() + ";" + playerMap.get(id));

            iPixelmon.network.sendTo(new PacketGuiResponse("update=" + ArrayUtil.toString(players.toArray(new String[players.size()]))), player);

            iPixelmon.network.sendTo(new PacketGuiResponse("success"), player);
            return null;
        }

    }

}
