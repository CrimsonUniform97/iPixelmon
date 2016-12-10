package com.ipixelmon.tablet.client.apps.mail;

import com.ipixelmon.GuiScrollList;
import com.ipixelmon.tablet.client.App;

/**
 * Created by colby on 12/9/2016.
 */
public class GuiMessages extends GuiScrollList {

    public GuiMessages(int xPosition, int yPosition, int width, int height) {
        super(xPosition, yPosition, width, height);
    }

    @Override
    public int getObjectHeight(int index) {
        return 30;
    }

    @Override
    public void drawObject(int index) {
        Conversation conversation = (Conversation) Mail.messages.toArray()[index];

        String senders = "";

        for (String s : conversation.getPlayers().values()) {
            if (!s.equalsIgnoreCase(mc.thePlayer.getName())) {
                if (senders.isEmpty()) {
                    senders += s;
                } else {
                    senders += ", " + s;
                }
            }
        }

        mc.fontRendererObj.drawString(senders, 2, 2, 0xFFFFFF);
    }

    @Override
    public int getSize() {
        return Mail.messages.size();
    }

    @Override
    public void elementClicked(int index, boolean doubleClick) {
        if (doubleClick)
            App.getApp(Mail.class).setActiveApp(new AppConversation((Conversation) Mail.messages.toArray()[index]));
    }
}
