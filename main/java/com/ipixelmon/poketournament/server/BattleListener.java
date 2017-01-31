package com.ipixelmon.poketournament.server;

import com.ipixelmon.gym.BattleController;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.LandControlAPI;
import com.ipixelmon.poketournament.Arena;
import com.ipixelmon.poketournament.TournamentAPI;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.pixelmonmod.pixelmon.api.events.SpectateEvent;
import com.pixelmonmod.pixelmon.api.events.BattleStartedEvent;
import com.pixelmonmod.pixelmon.api.events.PlayerBattleEndedEvent;

public class BattleListener {

    @SubscribeEvent
    public void onSpectate(SpectateEvent event) {

    }

    @SubscribeEvent
    public void onStart(BattleStartedEvent event) {

    }

    @SubscribeEvent
    public void onEnd(PlayerBattleEndedEvent event) throws Exception {
        Arena arena = Arena.getArena((BattleController) event.battleController);
        if(arena == null) return;

    }

    @SubscribeEvent
    public void onPlace(BlockEvent.PlaceEvent event) {
        Arena arena = TournamentAPI.Server.getArena(LandControlAPI.Server.getRegionAt(event.getWorld(), event.getPos()));

        if(arena == null) return;

        iPixelmon.network.sendTo(new PacketOpenTournamentGui(arena.getTournament()), (EntityPlayerMP) event.getPlayer());
    }

}
