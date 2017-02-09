package com.ipixelmon.poketournament.server;

import com.ipixelmon.poketournament.*;
import com.pixelmonmod.pixelmon.api.enums.BattleResults;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import com.pixelmonmod.pixelmon.api.events.BattleStartedEvent;
import com.pixelmonmod.pixelmon.api.events.PlayerBattleEndedEvent;

public class BattleListener {

    @SubscribeEvent
    public void onStart(BattleStartedEvent event) {

    }

    // TODO: Fix loser crashing... FIX THIS, THE GAMES ARE NOT SETTING UP PROPERLY!!!!!!
    // TODO: Need to advance round once all matches are over
    // TODO: What to do if a player leaves
    @SubscribeEvent
    public void onEnd(PlayerBattleEndedEvent event) throws Exception {
        Arena arena = TournamentAPI.Server.getArena(event.battleController);
        if(arena == null) return;

        Match match = null;
        Team team = null;

        SingleEliminationTournament tournament = arena.getTournament();

        /**
         * Goes through all matches and fins which match and which team the player is on
         */
        for(Match m : tournament.getMatchesForRound(tournament.getRound())) {

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

        /* If victory, set their team as the winner of the match */
        if(event.result == BattleResults.VICTORY ){
            tournament.setWinner(match, team);
        } else if (event.result == BattleResults.FLEE) {
            // TODO: Test if player disconnects, what happens
            if(match.team1.equals(team)) {
                tournament.setWinner(match, match.team2);
            } else {
                tournament.setWinner(match, match.team1);
            }
        }

        if(!tournament.isRoundOver(tournament.getRound())) return;

        tournament.setRound(tournament.getRound() + 1);

        arena.start();
    }

}
