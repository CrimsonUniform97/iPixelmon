package com.ipixelmon.gym.server;

import com.ipixelmon.gym.*;
import com.ipixelmon.landcontrol.LandControl;
import com.ipixelmon.landcontrol.regions.EnterRegionEvent;
import com.ipixelmon.team.TeamMod;
import com.ipixelmon.util.PixelmonAPI;
import com.ipixelmon.util.PlayerUtil;
import com.pixelmonmod.pixelmon.api.events.*;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.TrainerParticipant;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.Vec3i;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PixelmonListener {

    @SubscribeEvent
    public void enterGym(EnterRegionEvent event) {
        Gym gym = GymAPI.Server.getGym(event.region.getCenter());

        if (gym == null) return;

        event.player.addChatComponentMessage(new ChatComponentText(
                EnumChatFormatting.BOLD.toString() + EnumChatFormatting.GOLD.toString() + "You entered a " +
                        EnumChatFormatting.RED.toString() + "gym" + EnumChatFormatting.GOLD.toString() + "! " +
                        "Press " + EnumChatFormatting.BLUE.toString() + "G" +
                        EnumChatFormatting.GOLD.toString() + " to view gym info."
        ));
    }

    @SubscribeEvent
    public void battleStarted(BattleStartedEvent event) {
        try {
            List<BattleParticipant> participants = new ArrayList<>();
            participants.addAll(Arrays.asList(event.participant1));
            participants.addAll(Arrays.asList(event.participant2));

            for (BattleParticipant participant : participants) {
                if (participant instanceof TrainerParticipant) {
                    if (GymAPI.Server.getGym(participant.getEntity().getPosition()) != null) {
                        event.setCanceled(true);
                        return;
                    }
                }
            }

            boolean foundPlayer = false;
            for (BattleParticipant participant : participants) {
                if (participant instanceof PlayerParticipant) {
                    foundPlayer = true;
                }
            }

            if (!foundPlayer) {
                event.setCanceled(true);
                return;
            }
        } catch (Exception e) {
        }
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

            gym.getQue().remove(event.player.getUniqueID());

            EntityPlayerMP player = PlayerUtil.getPlayer(gym.getNextInQue());

            while(player == null && !gym.getQue().isEmpty()) {
                gym.getQue().remove(gym.getNextInQue());
                player = PlayerUtil.getPlayer(gym.getNextInQue());
            }

            if(player != null) gym.battle(player);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.WorldTickEvent event) {
        for(Gym gym : GymAPI.Server.gyms) {
            if(gym.getRegion().getWorld().equals(event.world)) {
                List<Entity> entities = event.world.getEntitiesWithinAABB(EntityTrainer.class, gym.getRegion().getBounds());

                if(entities.size() != gym.getTrainers().size()) gym.reloadLivingEntities();

            }
        }
    }

}
