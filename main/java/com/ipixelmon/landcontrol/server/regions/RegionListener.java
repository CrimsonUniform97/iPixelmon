package com.ipixelmon.landcontrol.server.regions;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.LandControlAPI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by colby on 1/8/2017.
 */
public class RegionListener {

    @SubscribeEvent
    public void onBreak(BlockEvent.BreakEvent event) {
        Region region = LandControlAPI.Server.getRegionAt(event.pos);
        if(region == null) return;

        if(!region.getProperty(EnumRegionProperty.breakBlocks)) event.setCanceled(true);
    }

    @SubscribeEvent
    public void onPlace(BlockEvent.PlaceEvent event) {
        Region region = LandControlAPI.Server.getRegionAt(event.pos);
        if(region == null) return;

        if(!region.getProperty(EnumRegionProperty.placeBlocks)) event.setCanceled(true);
    }

    @SubscribeEvent
    public void onInteract(PlayerInteractEvent event) {
        Region region = LandControlAPI.Server.getRegionAt(event.pos);
        if(region == null) return;

        if(event.entityPlayer.getHeldItem() != null && event.entityPlayer.getHeldItem().getItem() == Items.feather) {
            iPixelmon.network.sendTo(new PacketOpenRegionGui(region), (EntityPlayerMP) event.entityPlayer);
            event.setCanceled(true);
            return;
        }

        if(!region.getProperty(EnumRegionProperty.interact)) event.setCanceled(true);

        if(event.world.getBlockState(event.pos).getBlock() == Blocks.chest) {
            if(!region.getProperty(EnumRegionProperty.chestAccess)) event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onDamage(LivingHurtEvent event) {
        if(!(event.entity instanceof EntityPlayer)) return;

        Region region = LandControlAPI.Server.getRegionAt(event.entity.getPosition());
        if(region == null) return;

        if(!region.getProperty(EnumRegionProperty.invincible)) event.setCanceled(true);

        if(!region.getProperty(EnumRegionProperty.pvp)) {
            if(event.source.getEntity() != null) {
                if(event.source.getEntity() instanceof EntityPlayer) event.setCanceled(true);
            }
        }
    }

}
