package com.ipixelmon.landcontrol.toolCupboard;

import com.google.common.collect.Maps;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.LandControlAPI;
import com.ipixelmon.landcontrol.regions.Region;
import com.ipixelmon.landcontrol.toolCupboard.packet.PacketOpenGui;
import com.ipixelmon.landcontrol.toolCupboard.packet.PacketSetSelectedTile;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentString;
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
        if(event.getPlacedBlock().getBlock() != ToolCupboardBlock.instance) return;

        Chunk chunk = event.getWorld().getChunkFromBlockCoords(event.getPos());
        int x = chunk.xPosition * 16;
        int y = 0;
        int z = chunk.zPosition * 16;
        AxisAlignedBB chunkBounds = new AxisAlignedBB(x, y, z, x + 16, event.getWorld().getHeight(), z + 16);

        for(Region r : LandControlAPI.Server.regions) {
            if(r.getBounds().intersectsWith(chunkBounds)) {
                event.getPlayer().addChatComponentMessage(new TextComponentString("There is a region in that chunk."));
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onInteract(PlayerInteractEvent event) {
        ToolCupboardTileEntity tileEntity = LandControlAPI.Server.getTileEntity(event.getWorld(), event.getPos());

        if (tileEntity == null) return;

        if (event.getWorld().getBlockState(event.getPos()).getBlock() == ToolCupboardBlock.instance) {
            int meta = ToolCupboardBlock.instance.getMetaFromState(event.getWorld().getBlockState(event.getPos()));
            if (tileEntity.getPos().equals(meta == 0 ? event.getPos() : event.getPos().down())) {
                if (!event.getEntityPlayer().isSneaking()) {

                    /**
                     * Add access to modify this tileEntity if player is in the network
                     */
                    tileEntity.getAccessSet().add(event.getEntityPlayer().getUniqueID());

                    Map<UUID, String> players = Maps.newHashMap();

                    /**
                     * Only send players if the player is in the network
                     */
                    if (tileEntity.getNetwork().exists()
                            && tileEntity.getNetwork().getPlayers().contains(event.getEntityPlayer().getUniqueID())) {
                        players = tileEntity.getNetwork().getPlayerMap();
                    }

                    iPixelmon.network.sendTo(new PacketOpenGui(tileEntity, players), (EntityPlayerMP) event.getEntityPlayer());
                } else {
                    iPixelmon.network.sendTo(new PacketSetSelectedTile(tileEntity.getPos()), (EntityPlayerMP) event.getEntityPlayer());
                }

                return;
            }
        }

        if (!tileEntity.getNetwork().getPlayers().contains(event.getEntityPlayer().getUniqueID()))
            event.setCanceled(true);
    }

    @SubscribeEvent
    public void onExplosion(ExplosionEvent event) {
        event.setCanceled(true);
    }

}
