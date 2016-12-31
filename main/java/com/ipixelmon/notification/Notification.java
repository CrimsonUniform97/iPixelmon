package com.ipixelmon.notification;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

import java.util.LinkedList;

/**
 * Created by colbymchenry on 10/3/16.
 */
public abstract class Notification extends Gui {

    protected final int maxWidth;
    protected final long startTime;
    protected Minecraft mc = Minecraft.getMinecraft();

    public static LinkedList<NotificationOverlay.NotificationProperties> notificationsForRendering = new LinkedList<>();
    public static LinkedList<NotificationOverlay.NotificationProperties> notifications = new LinkedList<>();

    public Notification() {
        maxWidth = NotificationOverlay.instance.maxNotificationWidth;
        startTime = System.currentTimeMillis();
        NotificationOverlay.instance.addNotification(this);
    }

    public abstract void draw(int mouseX, int mouseY);

    public abstract int getHeight();

    public abstract int getWidth();

    public abstract long getDuration();

    public void actionPerformed() {}

    public void mouseClicked(int mouseX, int mouseY){}


}
