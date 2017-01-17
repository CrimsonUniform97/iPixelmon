package com.ipixelmon.tablet.app.friends;

import com.ipixelmon.notification.SimpleTextNotification;

public class FriendRequestNotification extends SimpleTextNotification {

    public FriendRequestNotification(String text, int seconds) {
        super(text, seconds);
    }

    @Override
    public String getSoundFile() {
        return "friendRequest";
    }
}
