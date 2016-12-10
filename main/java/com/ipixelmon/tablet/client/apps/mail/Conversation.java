package com.ipixelmon.tablet.client.apps.mail;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.uuidmanager.UUIDManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by colby on 12/9/2016.
 */
public class Conversation implements Comparable {

    public final UUID messageID;

    private Map<UUID, String> players = Maps.newHashMap();
    private List<String> messages = Lists.newArrayList();

    public Conversation(UUID messageID) throws SQLException {
        this.messageID = messageID;

        ResultSet result = iPixelmon.clientDb.query("SELECT * FROM tabletMessages WHERE messageID='" + messageID.toString() + "';");

        if (result.next()) {
            for (String s : result.getString("players").split(","))
                if (s != null && !s.isEmpty())
                    players.put(UUID.fromString(s), UUIDManager.getPlayerName(UUID.fromString(s)));

            for (String s : result.getString("messages").split("\\\\u2665")) {
                if (s != null && !s.isEmpty()) {
                    messages.add(s);
                    System.out.println(s);
                }
            }
        }
    }

    public List<String> getMessages() {
        return messages;
    }

    public Map<UUID, String> getPlayers() {
        return players;
    }

    public void sync() {
        try {
            String s = "";

            for (String message : messages) s += message + "\\u2665";

            iPixelmon.clientDb.query("UPDATE tabletMessages SET messages='" + s + "' WHERE messageID='" + messageID.toString() + "';");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Conversation) {
            Conversation c = (Conversation) o;
            if (c.messageID.equals(messageID)) return 0;
        }
        return -999;
    }
}
