package com.ipixelmon.tablet.client.apps.mail;

import com.ipixelmon.tablet.client.App;

/**
 * Created by colby on 12/4/2016.
 */
public class GuiConversation extends App {

    private Conversation conversation;

    // TODO: Got multiline text fields with scrolling to work, now you need to work on the Conversation/Messaging App here.

    public GuiConversation(Conversation conversation) {
        super("conversation", false);
        this.conversation = conversation;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

}
