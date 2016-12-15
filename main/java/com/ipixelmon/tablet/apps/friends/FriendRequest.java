package com.ipixelmon.tablet.apps.friends;

import com.ipixelmon.uuidmanager.UUIDManager;

import java.util.Date;
import java.util.UUID;

/**
 * Created by colby on 11/7/2016.
 */
public class FriendRequest implements Comparable {

    public UUID friend;
    public String name;
    public Date sentDate;

    public FriendRequest(UUID friend, Date sentDate) {
        this.friend = friend;
        this.name = UUIDManager.getPlayerName(friend);
        this.sentDate = sentDate;
    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof FriendRequest) {
            FriendRequest friendRequest = (FriendRequest) o;
            if(friendRequest.friend.equals(friend)) return 0;
        }
        return -1;
    }
}
