package com.ipixelmon.tablet.apps.friends;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.mysql.SelectionForm;
import com.ipixelmon.mysql.UpdateForm;
import com.ipixelmon.tablet.Tablet;
import com.ipixelmon.util.ArrayUtil;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by colbymchenry on 12/28/16.
 */
public class FriendsAPI {

    private static final Map<UUID, Object[]> friends = new HashMap<>();
    private static final Map<UUID, String> friendRequests = new HashMap<>();

    public static class Client {
        /**
         * Friend List handling
         */
        public static void addFriend(UUID id, String name, boolean online) {
            friends.remove(id);
            friends.put(id, new Object[]{name, online});
        }

        public static void removeFriend(UUID id) {
            friends.remove(id);
        }

        public static boolean isFriendOnline(UUID id) {
            return (boolean) friends.get(id)[1];
        }

        public static String getFriendName(UUID id) {
            return (String) friends.get(id)[0];
        }

        public static Set<UUID> getFriends() {
            return friends.keySet();
        }


        /**
         * Friend Request handling
         */
        public static void addFriendRequest(UUID id, String name) {
            friendRequests.put(id, name);
        }

        public static void removeFriendRequest(UUID id) {
            friendRequests.remove(id);
        }

        public static String getFriendRequestName(UUID id) {
            return friendRequests.get(id);
        }

        public static Set<UUID> getFriendRequests() {
            return friendRequests.keySet();
        }
    }

    public static class Server {
        public static void addFriend(UUID player, UUID friend) throws SQLException {
            ResultSet result = iPixelmon.mysql.selectAllFrom(Tablet.class, new SelectionForm("Friends")
                    .where("player", player.toString()));

            List<String> friends = Arrays.asList(iPixelmon.util.array.fromString(result.getString("friends")));
            friends.add(friend.toString());

            iPixelmon.mysql.update(Tablet.class, new UpdateForm("Friends")
                    .set("friends", iPixelmon.util.array.toString((String[]) friends.toArray())).where("player", player.toString()));
        }

        public static void removeFriend(UUID player, UUID friend) throws SQLException {
            ResultSet result = iPixelmon.mysql.selectAllFrom(Tablet.class, new SelectionForm("Friends")
                    .where("player", player.toString()));

            List<String> friends = Arrays.asList(iPixelmon.util.array.fromString(result.getString("friends")));
            friends.remove(friend.toString());

            iPixelmon.mysql.update(Tablet.class, new UpdateForm("Friends")
                    .set("friends", iPixelmon.util.array.toString((String[]) friends.toArray())).where("player", player.toString()));
        }

        public static List<UUID> getFriends(UUID player) {
            ResultSet result = iPixelmon.mysql.selectAllFrom(Tablet.class, new SelectionForm("Friends")
                    .where("player", player.toString()));

            List<UUID> friends = new ArrayList<>();
            try {
                for (String s : iPixelmon.util.array.fromString(result.getString("friends")))
                    friends.add(UUID.fromString(s));

            } catch (SQLException e) {
                e.printStackTrace();
            }

            return friends;
        }
    }
}
