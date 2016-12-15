package com.ipixelmon.tablet.apps.friends;

import com.ipixelmon.PlayerUtil;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.apps.friends.packet.PacketAddFriendRes;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.util.UUID;

/**
 * Created by colbymchenry on 11/9/16.
 */
public class PlayerJoinListener {

    @SubscribeEvent
    public void onJoin(PlayerEvent.PlayerLoggedInEvent event) {
        for (UUID uuid : FriendsAPI.getFriendsUUIDOnly(event.player.getUniqueID())) {
            EntityPlayerMP player = PlayerUtil.getPlayer(uuid);

            if (player != null)
                iPixelmon.network.sendTo(new PacketAddFriendRes(PacketAddFriendRes.ResponseType.UPDATE, "none"), player);
        }
    }

}
