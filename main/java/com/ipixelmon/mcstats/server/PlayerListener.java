package com.ipixelmon.mcstats.server;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.mcstats.McStatsAPI;
import com.ipixelmon.mcstats.PacketBrokeBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

/**
 * Created by colbymchenry on 12/29/16.
 */
public class PlayerListener {

    @SubscribeEvent
    public void onBreak(BlockEvent.BreakEvent event) {
        if(event.getPlayer() == null) return;

        EntityPlayerMP player = (EntityPlayerMP) event.getPlayer();
        Block block = event.world.getBlockState(event.pos).getBlock();

        if(!ServerProxy.xpValues.containsKey(block)) return;

        int xpGained = ServerProxy.xpValues.get(block);

        McStatsAPI.Server.giveEXP(player.getUniqueID(), xpGained);
        McStatsAPI.Server.updatePlayer(player);
        iPixelmon.network.sendTo(new PacketBrokeBlock(event.pos.getX(), event.pos.getY(), event.pos.getZ(), xpGained), player);
    }

    @SubscribeEvent
    public void onJoin(PlayerEvent.PlayerLoggedInEvent event) {
        McStatsAPI.Server.updatePlayer((EntityPlayerMP) event.player);
    }

}
