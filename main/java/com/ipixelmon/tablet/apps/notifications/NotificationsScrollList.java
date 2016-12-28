package com.ipixelmon.tablet.apps.notifications;

import com.ipixelmon.GuiScrollList;
import com.ipixelmon.tablet.notification.Notification;

/**
 * Created by colby on 12/18/2016.
 */
public class NotificationsScrollList extends GuiScrollList {

    public NotificationsScrollList(int xPosition, int yPosition, int width, int height) {
        super(xPosition, yPosition, width, height);
    }

    @Override
    public int getObjectHeight(int index) {
        return Notification.notifications.get(index).getNotification().getHeight();
    }

    @Override
    public void drawObject(int index, int mouseX, int mouseY, boolean isHovering) {
        mc.fontRendererObj.setUnicodeFlag(true);
        Notification.notifications.get(index).getNotification().draw(mouseX, mouseY);
        mc.fontRendererObj.setUnicodeFlag(false);
    }

    @Override
    public int getSize() {
        return Notification.notifications.size();
    }

    @Override
    public void elementClicked(int index, int mouseX, int mouseY, boolean doubleClick) {

    }
}
