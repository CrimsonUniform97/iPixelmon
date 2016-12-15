package com.ipixelmon.tablet.apps.friends;

import java.util.UUID;

/**
 * Created by colbymchenry on 11/4/16.
 */
public class Friend implements Comparable {

    public boolean online;
    public String name;
    public UUID uuid;

    public Friend(UUID uuid, String name, boolean online) {
        this.uuid = uuid;
        this.name = name;
        this.online = online;
    }


    @Override
    public int compareTo(Object o) {
        if(o instanceof Friend) {
            Friend friend = (Friend) o;
            if(friend.uuid.equals(uuid)) return 0;
        }
        return -1;
    }

    @Override
    public String toString() {
        return uuid.toString() + "," + name + "," + online;
    }

    public static Friend fromString(String s) {
        String[] data = s.split(",");
        return new Friend(UUID.fromString(data[0]), data[1], Boolean.parseBoolean(data[2]));
    }


}
