package com.ipixelmon.tablet.client.apps.friends;

import java.util.Date;
import java.util.UUID;

/**
 * Created by colby on 11/7/2016.
 */
public class FriendRequest implements Comparable {

    public UUID friend;
    public Date sentDate;

    public FriendRequest(UUID friend, Date sentDate) {
        this.friend = friend;
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
