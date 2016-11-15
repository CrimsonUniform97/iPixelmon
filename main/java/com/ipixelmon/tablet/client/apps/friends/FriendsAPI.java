package com.ipixelmon.tablet.client.apps.friends;

import com.ipixelmon.PlayerUtil;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.mysql.SelectionForm;
import com.ipixelmon.tablet.Tablet;
import com.ipixelmon.tablet.client.apps.friends.packet.PacketFriendsListReq;
import com.ipixelmon.uuidmanager.UUIDManager;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

/**
 * Created by colbymchenry on 11/9/16.
 */
public final class FriendsAPI {

    @SideOnly(Side.SERVER)
    public static final Set<Friend> getFriends(UUID player) {
        ResultSet result = iPixelmon.mysql.selectAllFrom(Tablet.class, new SelectionForm("Friends").where("player", player.toString()));

        Set<Friend> friends = new TreeSet<>();

        try {
            if (result.next()) {
                String[] data = result.getString("friends").split(",");
                UUID uuid;

                for (String friend : data) {
                    uuid = UUID.fromString(friend);
                    friends.add(new Friend(uuid, UUIDManager.getPlayerName(uuid), PlayerUtil.isPlayerOnline(uuid)));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return friends;
    }

    @SideOnly(Side.SERVER)
    public static final Set<UUID> getFriendsUUIDOnly(UUID player) {
        ResultSet result = iPixelmon.mysql.selectAllFrom(Tablet.class, new SelectionForm("Friends").where("player", player.toString()));

        Set<UUID> friends = new TreeSet<>();

        try {
            if (result.next()) {
                String[] data = result.getString("friends").split(",");
                for (String friend : data) friends.add(UUID.fromString(friend));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return friends;
    }

    @SideOnly(Side.CLIENT)
    public static final Set<Friend> getFriends(boolean withUpdate) {
        if (withUpdate)
            iPixelmon.network.sendToServer(new PacketFriendsListReq());
        return Friends.friends;
    }

    @SideOnly(Side.CLIENT)
    public static final Set<FriendRequest> getFriendRequests() {
        return Friends.requests;
    }

    @SideOnly(Side.CLIENT)
    public static void populateFriendRequests() {
        ResultSet result = iPixelmon.mysql.selectAllFrom(Tablet.class, new SelectionForm("FriendReqs").where("receiver", Minecraft.getMinecraft().thePlayer.getUniqueID().toString()));
        Friends.requests.clear();
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            while(result.next())
                Friends.requests.add(new FriendRequest(UUID.fromString(result.getString("sender")), formatter.parse(result.getString("sentDate"))));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
