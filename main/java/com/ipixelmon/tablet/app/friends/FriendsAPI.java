package com.ipixelmon.tablet.app.friends;

import com.google.common.collect.Lists;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.mysql.DeleteForm;
import com.ipixelmon.mysql.InsertForm;
import com.ipixelmon.mysql.SelectionForm;
import com.ipixelmon.mysql.UpdateForm;
import com.ipixelmon.tablet.Tablet;
import com.ipixelmon.tablet.app.friends.packet.PacketFriendRequestToServer;
import com.ipixelmon.util.ArrayUtil;
import com.ipixelmon.util.PlayerUtil;
import com.ipixelmon.uuidmanager.UUIDManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by colby on 1/5/2017.
 */
public class FriendsAPI {

    @SideOnly(Side.CLIENT)
    public static class Client {

        public static FriendsSet friends = new FriendsSet();
        public static Set<FriendRequest> friendRequests = new TreeSet<>();

        public static void sendFriendRequest(String player) {
            iPixelmon.network.sendToServer(new PacketFriendRequestToServer(player));
        }

    }

    @SideOnly(Side.SERVER)
    public static class Server {

        public static void storeFriendRequest(FriendRequest friendRequest) throws Exception {
            SelectionForm selectionForm = new SelectionForm("FriendRequests");
            selectionForm.where("sender", friendRequest.getSenderUUID().toString());
            selectionForm.where("receiver", friendRequest.getReceiverUUID().toString());
            ResultSet result = iPixelmon.mysql.selectAllFrom(Tablet.class, selectionForm);

            if (result.next()) throw new Exception("Friend request already pending.");

            if (areFriends(friendRequest.getSenderUUID(), friendRequest.getReceiverUUID())) {
                throw new Exception("Already friends.");
            }

            InsertForm insertForm = new InsertForm("FriendRequests");
            insertForm.add("sender", friendRequest.getSenderUUID().toString());
            insertForm.add("receiver", friendRequest.getReceiverUUID().toString());

            iPixelmon.mysql.insert(Tablet.class, insertForm);
        }

        public static boolean makeFriends(FriendRequest friendRequest) {
            if (areFriends(friendRequest.getSenderUUID(), friendRequest.getReceiverUUID())) return false;
            addFriend(friendRequest.getSenderUUID(), friendRequest.getReceiverUUID());
            addFriend(friendRequest.getReceiverUUID(), friendRequest.getSenderUUID());
            return true;
        }

        public static FriendsSet getFriends(UUID player) {
            ResultSet result = iPixelmon.mysql.selectAllFrom(Tablet.class, new SelectionForm("Friends")
                    .where("player", player.toString()));

            FriendsSet friends = new FriendsSet();
            try {
                if (result.next()) {
                    if (!result.getString("friends").isEmpty())
                        friends.loadFromString(result.getString("friends"));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return friends;
        }

        public static void addFriend(UUID player1, UUID player2) {
            UpdateForm updateForm = new UpdateForm("Friends");
            Set<Friend> friends = getFriends(player1);

            friends.add(new Friend(UUIDManager.getPlayerName(player2), player2,
                    PlayerUtil.isPlayerOnline(player2)));

            updateForm.set("friends", friends.toString());
            updateForm.where("player", player1.toString());
            iPixelmon.mysql.update(Tablet.class, updateForm);
        }

        public static void removeFriend(UUID player1, UUID player2) {
            UpdateForm updateForm = new UpdateForm("Friends");
            Set<Friend> friends = getFriends(player1);

            friends.remove(new Friend(UUIDManager.getPlayerName(player2), player2,
                    PlayerUtil.isPlayerOnline(player2)));

            updateForm.set("friends", friends.toString());
            updateForm.where("player", player1.toString());
            iPixelmon.mysql.update(Tablet.class, updateForm);
        }

        public static boolean deleteFriendRequest(FriendRequest friendRequest) {
            SelectionForm selectionForm = new SelectionForm("FriendRequests");
            selectionForm.where("sender", friendRequest.getSenderUUID().toString());
            selectionForm.where("receiver", friendRequest.getReceiverUUID().toString());
            ResultSet result = iPixelmon.mysql.selectAllFrom(Tablet.class, selectionForm);

            try {
                if (result.next()) {
                    DeleteForm deleteForm = new DeleteForm("FriendRequests");
                    deleteForm.add("sender", friendRequest.getSenderUUID().toString());
                    deleteForm.add("receiver", friendRequest.getReceiverUUID().toString());
                    iPixelmon.mysql.delete(Tablet.class, deleteForm);
                    return true;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return false;
        }

        public static boolean areFriends(UUID player1, UUID player2) {
            if (getFriends(player1).contains(new Friend("", player2, false))) return true;
            if (getFriends(player2).contains(new Friend("", player1, false))) return true;
            return false;
        }

        public static List<FriendRequest> getFriendRequest(UUID player) {
            ResultSet result = iPixelmon.mysql.selectAllFrom(Tablet.class,
                    new SelectionForm("FriendRequests").where("receiver", player.toString()));

            List<FriendRequest> friendRequests = Lists.newArrayList();

            try {
                while (result.next()) {
                    UUID sender = UUID.fromString(result.getString("sender"));
                    UUID receiver = UUID.fromString(result.getString("receiver"));

                    friendRequests.add(new FriendRequest(sender, receiver, UUIDManager.getPlayerName(sender),
                            UUIDManager.getPlayerName(receiver)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return friendRequests;
        }

    }

    public static class FriendsSet extends TreeSet<Friend> {

        @Override
        public String toString() {
            List<String> array = Lists.newArrayList();

            for (Object object : toArray()) {
                Friend friend = (Friend) object;
                array.add(friend.toString());
            }

            return ArrayUtil.toString(array.toArray(new String[array.size()]));
        }

        public void loadFromString(String arg) {
            String[] array = ArrayUtil.fromString(arg);
            for (String s : array) {
                if (Friend.fromString(s) != null)
                    add(Friend.fromString(s));
            }
        }

        public Friend get(Friend friend) {
            Friend f;
            Object[] array = toArray();
            for (int i = 0; i < size(); i++) {
                f = (Friend) array[i];
                if (f.compareTo(friend) == 0) return f;
            }

            return null;
        }

        @Override
        public boolean add(Friend friend) {
            if (get(friend) != null) {
                get(friend).setOnline(friend.isOnline());
                get(friend).setName(friend.getName());
                return true;
            } else {
                return super.add(friend);
            }
        }
    }
}
