package com.ipixelmon.gym.server;

import com.ipixelmon.PixelmonUtility;
import com.ipixelmon.gym.*;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.LandControl;
import com.ipixelmon.landcontrol.Region;
import com.ipixelmon.team.TeamMod;
import com.pixelmonmod.pixelmon.api.events.*;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.TrainerParticipant;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by colby on 10/10/2016.
 */
public class PixelmonListener {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onClick(PlayerInteractEvent event) {
        if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) return;

        try {
            Gym gym = GymMod.getGym(LandControl.getRegion(event.world, event.pos));

            if(event.world.getBlockState(event.pos).getBlock() == Blocks.standing_sign) {
                gym.startBattle((EntityPlayerMP) event.entityPlayer);
                return;
            }

            if (!gym.getSeats().contains(event.pos)) return;

            if (gym.getAvailableSlots() < 1) {
                event.entityPlayer.addChatComponentMessage(new ChatComponentText("There are no available slots."));
                return;
            }

            iPixelmon.network.sendTo(new PacketOpenClaimGui(), (EntityPlayerMP) event.entityPlayer);
        } catch (Exception e) {
        }
    }

    @SubscribeEvent
    public void battleStarted(BattleStartedEvent event) {
        try {
            List<BattleParticipant> participants = new ArrayList<>();
            participants.addAll(Arrays.asList(event.participant1));
            participants.addAll(Arrays.asList(event.participant2));

            for(BattleParticipant participant : participants) {
                if (participant instanceof TrainerParticipant) {
                    if (GymMod.getGym(LandControl.getRegion(participant.getEntity().worldObj,
                            new BlockPos(participant.getEntity().posX, participant.getEntity().posY, participant.getEntity().posZ))) != null) {
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
        if (!(event.trainer instanceof EntityGymLeader)) return;

        try {
            Region region = LandControl.getRegion(event.trainer.worldObj,
                    new BlockPos(event.trainer.posX, event.trainer.posY, event.trainer.posZ));

            Gym gym = GymMod.getGym(region);

            List<PixelmonWrapper> pokemon = event.trainer.getBattleController().getActivePokemon();

            double playerPokemonCP = PixelmonUtility.getBP(pokemon.get(0).pokemon);
            double trainerPokemonCP = PixelmonUtility.getBP(pokemon.get(1).pokemon);

            if (gym.getTeam() == TeamMod.getPlayerTeam(event.player.getUniqueID())) {
                gym.setPower(gym.getPower() + (trainerPokemonCP <= playerPokemonCP ? 100 : 500));
            } else {
                gym.setPower(gym.getPower() - 500);
            }

            gym.sync();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
