package com.ipixelmon.gyms.server;

import com.ipixelmon.PixelmonUtility;
import com.ipixelmon.gyms.*;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.LandControl;
import com.ipixelmon.landcontrol.Region;
import com.ipixelmon.teams.Teams;
import com.pixelmonmod.pixelmon.api.events.BeatTrainerEvent;
import com.pixelmonmod.pixelmon.api.events.LostToTrainerEvent;
import com.pixelmonmod.pixelmon.api.events.PixelmonSendOutEvent;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.TrainerParticipant;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.listener.WorldLoaded;
import com.pixelmonmod.pixelmon.storage.PixelmonStorage;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;

/**
 * Created by colby on 10/10/2016.
 */
public class PixelmonSendOutListener {

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onClick(PlayerInteractEvent event) {
        if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) return;

        try {
            Gym gym = Gyms.getGym(LandControl.getRegion(event.world, event.pos));

            event.setCanceled(true);

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
    public void onWin(BeatTrainerEvent event) {
        System.out.println("WON");

        if(!(event.trainer instanceof EntityGymLeader)) return;

        try {
            Region region = LandControl.getRegion(event.trainer.worldObj,
                    new BlockPos(event.trainer.posX, event.trainer.posY, event.trainer.posZ));

            Gym gym = Gyms.getGym(region);

            List<PixelmonWrapper> pokemon = event.trainer.getBattleController().getActivePokemon();

            System.out.println(pokemon.get(0).pokemon.getName() + "." + pokemon.get(1).pokemon.getName());
            System.out.println(pokemon.get(1).pokemon.getName() + "." + pokemon.get(1).pokemon.getName());
            double playerPokemonCP = PixelmonUtility.getBP(pokemon.get(0).pokemon);
            double trainerPokemonCP = PixelmonUtility.getBP(pokemon.get(1).pokemon);

            if(gym.getTeam() == Teams.getPlayerTeam(event.player.getUniqueID())) {
                gym.setPower(gym.getPower() + (trainerPokemonCP <= playerPokemonCP ? 100 : 500));
            } else {
                gym.setPower(gym.getPower() - 500);
            }

            gym.sync();
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

}
