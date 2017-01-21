package com.ipixelmon.landcontrol.toolCupboard;

import com.ipixelmon.landcontrol.LandControlAPI;
import com.ipixelmon.landcontrol.regions.Region;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ToolCupboardListener {

    @SubscribeEvent
    public void onPlace(BlockEvent.PlaceEvent event) {
        ToolCupboardTileEntity baseTile = LandControlAPI.Server.getTileEntity(event.getWorld(), event.getPos());

        if (baseTile == null) return;

        if (!baseTile.getNetwork().getPlayers().contains(event.getPlayer().getUniqueID())) {
            event.setCanceled(true);
            return;
        }

        if(event.getPlacedBlock().getBlock() == ToolCupboardBlock.instance) {
            Chunk chunk = event.getWorld().getChunkFromBlockCoords(event.getPos());
            int x = chunk.xPosition * 16;
            int y = 0;
            int z = chunk.zPosition * 16;
            AxisAlignedBB chunkBounds = new AxisAlignedBB(x, y, z, x + 16, event.getWorld().getHeight(), z + 16);

            for (Region r : LandControlAPI.Server.regions) {
                if (r.getBounds().intersectsWith(chunkBounds)) {
                    event.getPlayer().addChatComponentMessage(new TextComponentString("There is a region in that chunk."));
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public void onInteract(PlayerInteractEvent event) {
        ToolCupboardTileEntity baseTile = LandControlAPI.Server.getTileEntity(event.getWorld(), event.getPos());

        if (baseTile == null) return;

        ToolCupboardTileEntity topTile = (ToolCupboardTileEntity) event.getWorld().getTileEntity(baseTile.getPos().up());

        if(baseTile.getPos().equals(event.getPos())) return;

        if(topTile == null) return;

        if(topTile.getPos().equals(event.getPos())) return;

        if (!baseTile.getNetwork().getPlayers().contains(event.getEntityPlayer().getUniqueID()))
            event.setCanceled(true);

    }

    @SubscribeEvent
    public void onExplosion(ExplosionEvent event) {
        event.setCanceled(true);
    }

}
