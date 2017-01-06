package com.ipixelmon.tablet.app.friends;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.mysql.InsertForm;
import com.ipixelmon.mysql.SelectionForm;
import com.ipixelmon.tablet.Tablet;
import com.ipixelmon.tablet.app.friends.packet.PacketFriendInfo;
import com.ipixelmon.tablet.app.friends.packet.PacketFriendRequestInfo;
import com.ipixelmon.util.PlayerUtil;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by colby on 1/5/2017.
 */
public class PlayerListener {

    @SubscribeEvent
    public void onLogin(PlayerEvent.PlayerLoggedInEvent event) {
        ResultSet result = iPixelmon.mysql.selectAllFrom(Tablet.class, new SelectionForm("Friends")
                .where("player", event.player.getUniqueID().toString()));

        try {
            if (result.next()) {
                FriendsAPI.FriendsSet friends = FriendsAPI.Server.getFriends(event.player.getUniqueID());

                if (!friends.isEmpty()) {
                    Friend playerInfoAsFriend = new Friend(event.player.getName(), event.player.getUniqueID(), true);
                    for (Friend friend : friends) {

                        if (PlayerUtil.isPlayerOnline(friend.getUUID())) {
                            friend.setOnline(true);
                            iPixelmon.network.sendTo(new PacketFriendInfo(playerInfoAsFriend), PlayerUtil.getPlayer(friend.getUUID()));
                        }

                        iPixelmon.network.sendTo(new PacketFriendInfo(friend), (EntityPlayerMP) event.player);
                    }
                }

                List<FriendRequest> friendRequests = FriendsAPI.Server.getFriendRequest(event.player.getUniqueID());

                for (FriendRequest friendRequest : friendRequests) {
                    iPixelmon.network.sendTo(new PacketFriendRequestInfo(friendRequest), (EntityPlayerMP) event.player);
                }
            } else {
                InsertForm insertForm = new InsertForm("Friends");
                insertForm.add("player", event.player.getUniqueID().toString());
                insertForm.add("friends", "");
                iPixelmon.mysql.insert(Tablet.class, insertForm);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        FriendsAPI.FriendsSet friends = FriendsAPI.Server.getFriends(event.player.getUniqueID());
        Friend playerFriend = new Friend(event.player.getName(), event.player.getUniqueID(), false);

        if (!friends.isEmpty()) {
            for (Friend friend : friends) {
                if (PlayerUtil.isPlayerOnline(friend.getUUID())) {
                    iPixelmon.network.sendTo(new PacketFriendInfo(playerFriend), PlayerUtil.getPlayer(friend.getUUID()));
                }
            }
        }
    }

}
