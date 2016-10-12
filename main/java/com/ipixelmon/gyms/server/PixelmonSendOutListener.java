package com.ipixelmon.gyms.server;

import com.ipixelmon.PixelmonUtility;
import com.ipixelmon.gyms.EntityGymLeader;
import com.ipixelmon.gyms.Gym;
import com.ipixelmon.gyms.PacketOpenClaimGui;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.Region;
import com.ipixelmon.teams.Teams;
import com.pixelmonmod.pixelmon.api.events.BeatTrainerEvent;
import com.pixelmonmod.pixelmon.api.events.LostToTrainerEvent;
import com.pixelmonmod.pixelmon.api.events.PixelmonSendOutEvent;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.storage.PixelmonStorage;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Created by colby on 10/10/2016.
 */
public class PixelmonSendOutListener {

    @SubscribeEvent
    public void onDelivery(PixelmonSendOutEvent event) {
        try {
            EntityPixelmon pixelmon = PixelmonStorage.PokeballManager.getPlayerStorage((EntityPlayerMP) event.player).getPokemon(event.pixelmonID, event.player.worldObj);
            Region region = Region.instance.getRegion(event.player.worldObj, new BlockPos(event.player.posX, event.player.posY, event.player.posZ));

            if (region == null) return;

            Gym gym = Gym.instance.getGym(region);

            if (gym == null) return;

            gym.initiateBattle((EntityPlayerMP) event.player);
        } catch (Exception e) {
        }
    }

    @SubscribeEvent
    public void onClick(PlayerInteractEvent event) {
        if (event.action != PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK) return;

        try {
            Gym gym = Gym.instance.getGym(Region.instance.getRegion(event.world, event.pos));

            if (!gym.getDisplayBlocks().contains(event.pos)) return;

            if (gym.getAvailableSlots() < 1) {
                event.entityPlayer.addChatComponentMessage(new ChatComponentText("There are no available slots."));
                return;
            }

            iPixelmon.network.sendTo(new PacketOpenClaimGui(), (EntityPlayerMP) event.entityPlayer);
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
            Gym gym = Gym.instance.getGym(Region.instance.getRegion(gymLeader.getEntityWorld(), new BlockPos(gymLeader.posX, gymLeader.posY, gymLeader.posZ)));
            double trainerBP = PixelmonUtility.getBP(gymLeader.getPokemonStorage().getFirstAblePokemon(event.player.worldObj));
            double playerBP = PixelmonUtility.getBP(PixelmonStorage.PokeballManager.getPlayerStorage((EntityPlayerMP) event.player).getFirstAblePokemon(event.player.worldObj));

            gym.setPower(sameTeam ? (gym.getPower() + (trainerBP <= playerBP ? 100 : 500)) : (gym.getPower() - (trainerBP <= playerBP ? 100 : 500)));

            // TODO: Work on power if gym wins a battle, and work on loss

            gym.initiateBattle((EntityPlayerMP) event.player);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onLose(LostToTrainerEvent event) {

    }

}
