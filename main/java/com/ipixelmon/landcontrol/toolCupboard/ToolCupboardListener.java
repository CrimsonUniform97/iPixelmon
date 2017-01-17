package com.ipixelmon.landcontrol.toolCupboard;

import com.google.common.collect.Maps;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.LandControlAPI;
import com.ipixelmon.landcontrol.regions.Region;
import com.ipixelmon.landcontrol.toolCupboard.packet.PacketOpenGui;
import com.ipixelmon.landcontrol.toolCupboard.ToolCupboardBlock;
import com.ipixelmon.landcontrol.toolCupboard.ToolCupboardTileEntity;
import com.ipixelmon.landcontrol.toolCupboard.packet.PacketSetSelectedTile;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;
import java.util.UUID;

/**
 * Created by colby on 1/6/2017.
 */
public class ToolCupboardListener {

    @SubscribeEvent
    public void onPlace(BlockEvent.PlaceEvent event) {
        // TODO: Play sound only on client side in notifications
        event.world.playSoundAtEntity(event.player, iPixelmon.id + ":friendRequest", 1.0F, 1.0F);

        if(event.placedBlock.getBlock() != ToolCupboardBlock.instance) return;

        Chunk chunk = event.world.getChunkFromBlockCoords(event.pos);
        int x = chunk.xPosition * 16;
        int y = 0;
        int z = chunk.zPosition * 16;
        AxisAlignedBB chunkBounds = AxisAlignedBB.fromBounds(x, y, z, x + 16, event.world.getHeight(), z + 16);

        for(Region r : LandControlAPI.Server.regions) {
            if(r.getBounds().intersectsWith(chunkBounds)) {
                event.player.addChatComponentMessage(new ChatComponentText("There is a region in that chunk."));
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onInteract(PlayerInteractEvent event) {
        ToolCupboardTileEntity tileEntity = LandControlAPI.Server.getTileEntity(event.world, event.pos);

        if (tileEntity == null) return;

        if (event.world.getBlockState(event.pos).getBlock() == ToolCupboardBlock.instance) {
            int meta = ToolCupboardBlock.instance.getMetaFromState(event.world.getBlockState(event.pos));
            if (tileEntity.getPos().equals(meta == 0 ? event.pos : event.pos.down())) {
                if (!event.entityPlayer.isSneaking()) {

                    /**
                     * Add access to modify this tileEntity if player is in the network
                     */
                    tileEntity.getAccessSet().add(event.entityPlayer.getUniqueID());

                    Map<UUID, String> players = Maps.newHashMap();

                    /**
                     * Only send players if the player is in the network
                     */
                    if (tileEntity.getNetwork().exists()
                            && tileEntity.getNetwork().getPlayers().contains(event.entityPlayer.getUniqueID())) {
                        players = tileEntity.getNetwork().getPlayerMap();
                    }

                    iPixelmon.network.sendTo(new PacketOpenGui(tileEntity, players), (EntityPlayerMP) event.entityPlayer);
                } else {
                    iPixelmon.network.sendTo(new PacketSetSelectedTile(tileEntity.getPos()), (EntityPlayerMP) event.entityPlayer);
                }

                return;
            }
        }

        if (!tileEntity.getNetwork().getPlayers().contains(event.entityPlayer.getUniqueID()))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public void onExplosion(ExplosionEvent event) {
        event.setCanceled(true);
    }

}
