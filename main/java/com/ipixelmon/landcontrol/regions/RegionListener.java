package com.ipixelmon.landcontrol.regions;

import com.google.common.collect.Lists;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.LandControlAPI;
import com.ipixelmon.landcontrol.regions.packet.PacketOpenRegionGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

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

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        // TODO: May need some optimization, need to test it.
        for(Region region : LandControlAPI.Server.regions) {
            List<EntityPlayer> toRemove = Lists.newArrayList();
            for(EntityPlayer player : region.playersInside) {
                if(!region.getBounds().isVecInside(player.getPositionVector())) {
                    toRemove.add(player);
                    player.addChatComponentMessage(new ChatComponentText(region.getLeaveMsg()));
                }
            }

            region.playersInside.removeAll(toRemove);
        }

        Region region = LandControlAPI.Server.getRegionAt(event.player.getPosition());

        if(region == null) return;

        if(!region.playersInside.contains(event.player.getUniqueID())) {
            event.player.addChatComponentMessage(new ChatComponentText(region.getEnterMsg()));
            region.playersInside.add(event.player);
        }
    }

}
