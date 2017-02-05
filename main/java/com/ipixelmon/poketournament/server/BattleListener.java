package com.ipixelmon.poketournament.server;

import com.ipixelmon.gym.BattleController;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.LandControlAPI;
import com.ipixelmon.poketournament.Arena;
import com.ipixelmon.poketournament.Match;
import com.ipixelmon.poketournament.Team;
import com.ipixelmon.poketournament.TournamentAPI;
import com.pixelmonmod.pixelmon.api.enums.BattleResults;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.pixelmonmod.pixelmon.api.events.SpectateEvent;
import com.pixelmonmod.pixelmon.api.events.BattleStartedEvent;
import com.pixelmonmod.pixelmon.api.events.PlayerBattleEndedEvent;

public class BattleListener {

    @SubscribeEvent
    public void onStart(BattleStartedEvent event) {

    }

    // TODO: Fix loser crashing...
    // TODO: Need to advance round once all matches are over
    @SubscribeEvent
    public void onEnd(PlayerBattleEndedEvent event) throws Exception {
        Arena arena = TournamentAPI.Server.getArena(event.battleController);
        if(arena == null) return;

        Match match = null;
        Team team = null;

        for(Match m : arena.getTournament().getMatchesForRound(arena.getTournament().getRound())) {

            for(EntityPlayerMP playerMP : m.team1.players) {
                if(playerMP.getUniqueID().equals(event.player.getUniqueID())) {
                    match = m;
                    team = m.team1;
                    break;
                }
            }

            for(EntityPlayerMP playerMP : m.team2.players) {
                if(playerMP.getUniqueID().equals(event.player.getUniqueID())) {
                    match = m;
                    team = m.team2;
                    break;
                }
            }

        }

        if(match == null || team == null) return;

        if(event.result == BattleResults.VICTORY){
            arena.getTournament().setWinner(match, team);
        } else if (event.result == BattleResults.FLEE){
            if(match.team1.equals(team)) {
                arena.getTournament().setWinner(match, match.team2);
            } else {
                arena.getTournament().setWinner(match, match.team1);
            }
        }

    }

}
