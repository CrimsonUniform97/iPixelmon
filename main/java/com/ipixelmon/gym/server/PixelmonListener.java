package com.ipixelmon.gym.server;

import com.ipixelmon.gym.*;
import com.ipixelmon.landcontrol.LandControl;
import com.ipixelmon.team.TeamMod;
import com.ipixelmon.util.PixelmonAPI;
import com.pixelmonmod.pixelmon.api.events.*;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.TrainerParticipant;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import net.minecraft.util.BlockPos;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PixelmonListener {

    @SubscribeEvent
    public void onInteract(PlayerInteractEvent event) {
        System.out.println("CALLED");
    }

    @SubscribeEvent
    public void battleStarted(BattleStartedEvent event) {
        try {
            List<BattleParticipant> participants = new ArrayList<>();
            participants.addAll(Arrays.asList(event.participant1));
            participants.addAll(Arrays.asList(event.participant2));

            for(BattleParticipant participant : participants) {
                if (participant instanceof TrainerParticipant) {
                    if (GymAPI.Server.getGym(participant.getEntity().getPosition()) != null) {
                        event.setCanceled(true);
                        return;
                    }
                }
            }

            boolean foundPlayer = false;
            for(BattleParticipant participant : participants) {
                if(participant instanceof PlayerParticipant) {
                    foundPlayer = true;
                }
            }

            if(!foundPlayer) {
                event.setCanceled(true);
                return;
            }
        }catch(Exception e) {}
    }

    @SubscribeEvent
    public void onWin(BeatTrainerEvent event) {
        if (!(event.trainer instanceof EntityTrainer)) return;

        try {
            Gym gym = GymAPI.Server.getGym(event.trainer.getPosition());

            List<PixelmonWrapper> pokemon = event.trainer.getBattleController().getActivePokemon();

            double playerPokemonCP = PixelmonAPI.getCP(new PixelmonData(pokemon.get(0).pokemon));
            double trainerPokemonCP = PixelmonAPI.getCP(new PixelmonData(pokemon.get(1).pokemon));

            if (gym.getTeam() == TeamMod.getPlayerTeam(event.player.getUniqueID())) {
                gym.setPrestige(gym.getPrestige() + (trainerPokemonCP <= playerPokemonCP ? 100 : 500));
            } else {
                gym.setPrestige(gym.getPrestige() - 500);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
