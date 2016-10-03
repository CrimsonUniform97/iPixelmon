package com.ipixelmon.tablet.client;

/**
 * Created by colbymchenry on 10/3/16.
 */
public abstract class Notification {

    protected final int width;
    protected final long startTime;

    public Notification() {
        width = NotificationOverlay.instance.maxNotificationWidth;
        startTime = System.currentTimeMillis();
    }

    public abstract void draw();

    public abstract int getHeight();

    public abstract long getDuration();

    public void actionPerformed() {}

}
