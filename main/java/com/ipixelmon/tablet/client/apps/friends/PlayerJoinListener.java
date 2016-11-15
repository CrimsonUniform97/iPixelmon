package com.ipixelmon.tablet.client.apps.friends;

import com.ipixelmon.PlayerUtil;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.mysql.SelectionForm;
import com.ipixelmon.tablet.Tablet;
import com.ipixelmon.tablet.client.apps.friends.packet.PacketAddFriendRes;
import com.ipixelmon.tablet.notification.PacketNotification;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by colbymchenry on 11/9/16.
 */
public class PlayerJoinListener {

    @SubscribeEvent
    public void onJoin(PlayerEvent.PlayerLoggedInEvent event) {
        ResultSet result = iPixelmon.mysql.selectAllFrom(Tablet.class, new SelectionForm("Friends").where("player", event.player.getUniqueID().toString()));

        try {
            if (result.next())
                for (String s : result.getString("friends").split(","))
                    if (PlayerUtil.isPlayerOnline(UUID.fromString(s)))
                        iPixelmon.network.sendTo(new PacketAddFriendRes(PacketAddFriendRes.ResponseType.UPDATE, "none"), PlayerUtil.getPlayer(UUID.fromString(s)));
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
