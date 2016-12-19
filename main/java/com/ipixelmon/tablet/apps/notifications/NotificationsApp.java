package com.ipixelmon.tablet.apps.notifications;

import com.ipixelmon.tablet.apps.App;
import org.lwjgl.input.Mouse;

import java.io.IOException;

/**
 * Created by colby on 12/18/2016.
 */
public class NotificationsApp extends App {

    private NotificationsScrollList scrollList;

    public NotificationsApp(String name, boolean register) {
        super(name, register);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        scrollList.draw(mouseX, mouseY, Mouse.getDWheel());
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        scrollList.keyTyped(typedChar, keyCode);
    }

    @Override
    public void initGui() {
        super.initGui();
        int listWidth = getScreenBounds().getWidth() - 100;
        int listHeight = getScreenBounds().getHeight() - 40;
        scrollList = new NotificationsScrollList((getScreenBounds().getWidth() - listWidth) / 2, (getScreenBounds().getHeight() - listHeight) / 2, listWidth, listHeight);
    }
}
