package com.ipixelmon.tablet.client;

import net.minecraft.client.gui.Gui;

/**
 * Created by colbymchenry on 10/3/16.
 */
public abstract class Notification extends Gui {

    protected final int maxWidth;
    protected final long startTime;

    public Notification() {
        maxWidth = NotificationOverlay.instance.maxNotificationWidth;
        startTime = System.currentTimeMillis();
    }

    public abstract void draw();

    public abstract int getHeight();

    public abstract int getWidth();

    public abstract long getDuration();

    public void actionPerformed() {}

}
