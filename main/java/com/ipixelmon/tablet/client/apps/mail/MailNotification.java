package com.ipixelmon.tablet.client.apps.mail;

import com.ipixelmon.tablet.notification.Notification;
import com.ipixelmon.tablet.notification.SimpleTextNotification;
import net.minecraft.client.Minecraft;

/**
 * Created by colbymchenry on 11/16/16.
 */
public class MailNotification extends SimpleTextNotification {

    public MailNotification(String text) {
        super(text);
    }

    @Override
    public void actionPerformed() {
        Minecraft.getMinecraft().displayGuiScreen(new GuiQuickResponse());
    }

    @Override
    public void draw() {

    }

    @Override
    public int getHeight() {
        return 0;
    }

    @Override
    public int getWidth() {
        return 0;
    }

    @Override
    public long getDuration() {
        return 0;
    }
}
