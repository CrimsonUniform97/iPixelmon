package com.ipixelmon.landcontrol.server;

import com.google.common.collect.Maps;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.LandControlAPI;
import com.ipixelmon.landcontrol.packet.PacketOpenGui;
import com.ipixelmon.landcontrol.toolCupboard.ToolCupboardBlock;
import com.ipixelmon.landcontrol.toolCupboard.ToolCupboardTileEntity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.ExplosionEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;
import java.util.UUID;

/**
 * Created by colby on 1/6/2017.
 */
public class PlayerListener {

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
                    return;
                }
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
