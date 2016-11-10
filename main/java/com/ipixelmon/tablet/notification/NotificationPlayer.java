package com.ipixelmon.tablet.notification;

import java.util.UUID;

/**
 * Created by colbymchenry on 11/9/16.
 */
public class NotificationPlayer extends SimpleTextNotification {

    private UUID player;
// TODO: Draw player head
    public NotificationPlayer(String text, UUID player) {
        super(text);
        this.player = player;
    }

    @Override
    public void draw() {
        super.draw();
    }

    @Override
    public int getHeight() {
        return super.getHeight();
    }

    @Override
    public int getWidth() {
        return super.getWidth();
    }

    @Override
    public long getDuration() {
        return super.getDuration();
    }
}
