package com.ipixelmon.tablet.apps.friends;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.uuidmanager.UUIDManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.UUID;

/**
 * Created by colbymchenry on 12/27/16.
 */
public class PlayerListener {

    @SubscribeEvent
    public void onJoin(PlayerEvent.PlayerLoggedInEvent event) {
        UUID id = event.player.getUniqueID();

        EntityPlayerMP player;
        for (UUID uuid : FriendsAPI.Server.getFriends(id)) {
            player = iPixelmon.util.player.getPlayer(uuid);

            if (player != null) {
                iPixelmon.network.sendTo(new PacketFriendStatus(id, event.player.getName(), true), player);
            }

            iPixelmon.network.sendTo(new PacketFriendStatus(uuid, UUIDManager.getPlayerName(uuid), player != null),
                    (EntityPlayerMP) event.player);
        }
    }

    @SubscribeEvent
    public void onLeave(PlayerEvent.PlayerLoggedOutEvent event) {
        UUID id = event.player.getUniqueID();

        EntityPlayerMP player;
        for (UUID uuid : FriendsAPI.Server.getFriends(id)) {
            player = iPixelmon.util.player.getPlayer(uuid);

            if (player != null) {
                iPixelmon.network.sendTo(new PacketFriendStatus(id, event.player.getName(), false), player);
            }
        }
    }

}
