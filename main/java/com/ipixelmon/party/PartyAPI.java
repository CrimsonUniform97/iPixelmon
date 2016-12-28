package com.ipixelmon.party;

import com.google.common.collect.Maps;

import java.util.*;

/**
 * Created by colbymchenry on 12/28/16.
 */
public class PartyAPI {

    public static class Client {

        private static UUID partyID;
        private static Map<UUID, String> players = Maps.newHashMap();

        public static UUID getPartyID() { return partyID; }

        public static void setPartyID(UUID partyID) {
            Client.partyID = partyID;
        }

        public static void addPlayerToParty(UUID id, String name) {
            players.put(id, name);
        }

        public static void removePlayerFromParty(UUID id) {
            players.remove(id);
        }

        public static String getPlayerName(UUID id) {
            return players.get(id);
        }

        public static Set<UUID> getPlayersInPartyID() {
            return players.keySet();
        }

        public static Collection<String> getPlayersInPartyName() {
            return players.values();
        }

        public static void resetParty() {
            players.clear();
            partyID = null;
        }

    }

    public static class Server {

        private static final Map<UUID, TreeSet<UUID>> parties = Maps.newHashMap();

        public static UUID getPlayersParty(UUID player) {
            for (UUID partyUUID : parties.keySet()) {
                if (parties.get(partyUUID).contains(player)) return partyUUID;
            }

            return null;
        }

        public static TreeSet<UUID> getPlayersInParty(UUID party) {
            return parties.get(party);
        }

        public static void addPlayerToParty(UUID party, UUID player) {
            for (UUID partyUUID : parties.keySet()) {
                parties.get(partyUUID).remove(player);
            }

            if (!parties.containsKey(party)) {
                TreeSet<UUID> players = new TreeSet<>();
                players.add(player);
                parties.put(party, players);
            } else {
                parties.get(party).add(player);
            }
        }

        public static void removePlayerFromParty(UUID party, UUID player) {
            parties.get(party).remove(player);
            if (parties.get(party).isEmpty()) parties.remove(party);
        }

    }

}
