package com.ipixelmon.landcontrol.regions;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.LandControlAPI;
import com.ipixelmon.landcontrol.regions.packet.PacketOpenRegionGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.Iterator;

public class RegionListener {

    @SubscribeEvent
    public void onBreak(BlockEvent.BreakEvent event) {
        Region region = LandControlAPI.Server.getRegionAt(event.world, event.pos);
        if (region == null) return;

        if (!region.getProperty(EnumRegionProperty.breakBlocks)) event.setCanceled(true);
    }

    @SubscribeEvent
    public void onPlace(BlockEvent.PlaceEvent event) {
        Region region = LandControlAPI.Server.getRegionAt(event.world, event.pos);
        if (region == null) return;

        if (!region.getProperty(EnumRegionProperty.placeBlocks)) event.setCanceled(true);
    }

    @SubscribeEvent
    public void onInteract(PlayerInteractEvent event) {
        Region region = LandControlAPI.Server.getRegionAt(event.world, event.pos);
        if (region == null) return;

        if (event.entityPlayer.getHeldItem() != null && event.entityPlayer.getHeldItem().getItem() == Items.feather) {
            SubRegion subRegion = region.getSubRegionAt(event.pos);
            if (subRegion != null) {
                iPixelmon.network.sendTo(new PacketOpenRegionGui(subRegion), (EntityPlayerMP) event.entityPlayer);
            } else {
                iPixelmon.network.sendTo(new PacketOpenRegionGui(region), (EntityPlayerMP) event.entityPlayer);
            }

            event.setCanceled(true);
            return;
        }

        if (!region.getProperty(EnumRegionProperty.interact)) event.setCanceled(true);

        if (event.world.getBlockState(event.pos).getBlock() == Blocks.chest) {
            if (!region.getProperty(EnumRegionProperty.chestAccess)) event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onDamage(LivingHurtEvent event) {
        if (!(event.entity instanceof EntityPlayer)) return;

        Region region = LandControlAPI.Server.getRegionAt(event.entity.worldObj, event.entity.getPosition());
        if (region == null) return;

        if (!region.getProperty(EnumRegionProperty.invincible)) event.setCanceled(true);

        if (!region.getProperty(EnumRegionProperty.pvp)) {
            if (event.source.getEntity() != null) {
                if (event.source.getEntity() instanceof EntityPlayer) event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        World world = event.player.getEntityWorld();
        Region region = LandControlAPI.Server.getRegionAt(world, player.getPosition());

        if (region == null) return;

        if (region.getEnterMsg() == null || region.getEnterMsg().isEmpty()) return;

        if (region.playersInside.contains(player)) return;
        region.playersInside.add(player);
        player.addChatComponentMessage(new ChatComponentText(region.getEnterMsg()));
    }

    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event) {
        // go through all regions
        for (Region r : LandControlAPI.Server.regions) {
            for (SubRegion s : r.getSubRegions()) {
                if (doLeave(s)) break;
            }

            doLeave(r);
        }
    }

    private boolean doLeave(Region r) {
        // check if it has a leave message
        if (r.getLeaveMsg() != null && !r.getLeaveMsg().isEmpty()) {
            // get players that are inside the region
            Iterator<EntityPlayer> iterator = r.playersInside.listIterator();
            // loop through the players
            while (iterator.hasNext()) {
                EntityPlayer p = iterator.next();

                // check if the player is inside the region
                if (p.getEntityWorld() != r.getWorld() ||
                        !r.getBounds().isVecInside(new Vec3(p.getPosition().getX(), p.getPosition().getY(), p.getPosition().getZ()))) {
                    // if not remove the player and send the leave message
                    p.addChatComponentMessage(new ChatComponentText(r.getLeaveMsg()));
                    iterator.remove();
                    return true;
                }
            }
        }

        return false;
    }

}
