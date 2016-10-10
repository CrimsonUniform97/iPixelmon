package com.ipixelmon.gyms.server;

import com.ipixelmon.gyms.EntityGymLeader;
import com.ipixelmon.gyms.Gym;
import com.ipixelmon.gyms.PacketOpenClaimGui;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.Region;
import com.ipixelmon.teams.EnumTeam;
import com.ipixelmon.teams.Teams;
import com.pixelmonmod.pixelmon.api.events.BeatTrainerEvent;
import com.pixelmonmod.pixelmon.api.events.LostToTrainerEvent;
import com.pixelmonmod.pixelmon.api.events.PixelmonSendOutEvent;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.storage.PixelmonStorage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;
import java.util.UUID;

/**
 * Created by colby on 10/10/2016.
 */
public class PixelmonSendOutListener {

    @SubscribeEvent
    public void onDelivery(PixelmonSendOutEvent event) {
        try {
            EntityPixelmon pixelmon = PixelmonStorage.PokeballManager.getPlayerStorage((EntityPlayerMP) event.player).getPokemon(event.pixelmonID, event.player.worldObj);
            Region region = new Region(event.player.worldObj, new BlockPos(event.player.posX, event.player.posY, event.player.posZ));

            if (region == null) return;

            Gym gym = new Gym(region.getUUID());

            if (gym == null) return;

            if (gym.getTeam() == EnumTeam.None) {
                event.setCanceled(true);
                // TODO: Check gyms points before adding pokemon
                iPixelmon.network.sendTo(new PacketOpenClaimGui(), (EntityPlayerMP) event.player);
            } else {
                gym.initiateBattle((EntityPlayerMP) event.player);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onWin(BeatTrainerEvent event) {
        if (!(event.trainer instanceof EntityGymLeader)) return;

        EntityGymLeader gymLeader = (EntityGymLeader) event.trainer;

        boolean sameTeam = Teams.getPlayerTeam(gymLeader.getPlayerUUID()).equals(Teams.getPlayerTeam(event.player.getUniqueID()));

        try {
            Region region = new Region(event.player.worldObj, event.player.playerLocation);
            Gym gym = new Gym(region.getUUID());

//            if (sameTeam)
//                gym.initiateTraining((EntityPlayerMP) event.player);
//            else
//                gym.initiateBattle((EntityPlayerMP) event.player);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onLos(LostToTrainerEvent event) {

    }

}
