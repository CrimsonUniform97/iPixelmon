package com.ipixelmon.party.server;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.party.PacketLeaveParty;
import com.ipixelmon.party.PartyAPI;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.UUID;

/**
 * Created by colbymchenry on 12/28/16.
 */
public class PlayerListener {

    @SubscribeEvent
    public void onLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        UUID partyID = PartyAPI.Server.getPlayersParty(event.player.getUniqueID());

        if(partyID == null) return;

        EntityPlayerMP player;
        for(UUID playerID : PartyAPI.Server.getPlayersInParty(partyID)) {
            player = iPixelmon.util.player.getPlayer(playerID);

            if(player != null) {
                iPixelmon.network.sendTo(new PacketLeaveParty(event.player.getName(), event.player.getUniqueID()), player);
            }
        }
    }

}
