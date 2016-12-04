package com.ipixelmon.tablet.client.apps.mail;

import com.google.common.collect.Lists;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.uuidmanager.UUIDManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

/**
 * Created by colby on 12/3/2016.
 */
public class Conversation implements Comparable {

    private UUID participant1, participant2;
    private List<String> messages = Lists.newArrayList();
    private String participant1Name, participant2Name;

    public Conversation(UUID participant1, UUID participant2) throws SQLException {
        ResultSet result = iPixelmon.mysql.query("SELECT * FROM tabletConversations WHERE participant1='" + participant1.toString() + "' AND participant2='" + participant2.toString() + "';");

        if (result.next()) {
            this.participant1 = participant1;
            this.participant2 = participant2;

            for (String convoPortion : result.getString("messages").split(";")) {
                if (!convoPortion.isEmpty()) messages.add(convoPortion);
            }

            participant1Name = UUIDManager.getPlayerName(participant1);
            participant2Name = UUIDManager.getPlayerName(participant2);
        }
    }

    public static List<Conversation> getConversations(UUID participant) throws SQLException {
        ResultSet result = iPixelmon.mysql.query("SELECT * FROM tabletConversations WHERE participant1='" + participant.toString() + "' OR participant2='" + participant.toString() + "';");

        List<Conversation> conversations = Lists.newArrayList();

        while (result.next())
            conversations.add(new Conversation(UUID.fromString(result.getString("participant1")), UUID.fromString(result.getString("participant2"))));

        return conversations;
    }

    public void addMessage(UUID participant, String message) {
        messages.add((participant.equals(participant1) ? 1 : 2) + "," + message);
    }

    public List<String> getMessages() {
        return messages;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof Conversation) {
            Conversation conversation = (Conversation) o;
            if (conversation.participant1.equals(participant1) && conversation.participant2.equals(participant2))
                return 0;
        }
        return -1;
    }

    public UUID getParticipant1() {
        return participant1;
    }

    public UUID getParticipant2() {
        return participant2;
    }

    public String getParticipant1Name() {
        return participant1Name;
    }

    public String getParticipant2Name() {
        return participant2Name;
    }

}
