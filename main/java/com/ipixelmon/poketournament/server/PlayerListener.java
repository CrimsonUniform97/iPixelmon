package com.ipixelmon.poketournament.server;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.LandControlAPI;
import com.ipixelmon.poketournament.Arena;
import com.ipixelmon.poketournament.TournamentAPI;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class PlayerListener {


    // TODO: Need to test gui
    @SubscribeEvent
    public void onPlace(BlockEvent.PlaceEvent event) {
        Arena arena = TournamentAPI.Server.getArena(LandControlAPI.Server.getRegionAt(event.getWorld(), event.getPos()));

        if(arena == null) return;

        iPixelmon.network.sendTo(new PacketOpenTournamentGui(arena.getTournament()), (EntityPlayerMP) event.getPlayer());
    }

}
