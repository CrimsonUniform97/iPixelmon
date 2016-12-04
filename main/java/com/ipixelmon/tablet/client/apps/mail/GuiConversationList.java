package com.ipixelmon.tablet.client.apps.mail;

import com.ipixelmon.GuiScrollList;
import com.ipixelmon.tablet.client.App;

/**
 * Created by colby on 12/4/2016.
 */
public class GuiConversationList extends GuiScrollList {

    private App parentApp;

    public GuiConversationList(int xPosition, int yPosition, int width, int height, App parentApp) {
        super(xPosition, yPosition, width, height);
        this.parentApp = parentApp;
    }

    @Override
    public int getObjectHeight(int index) {
        return 20;
    }

    @Override
    public void drawObject(int index) {
        Conversation conversation = (Conversation) Mail.conversations.toArray()[index];

        String playerName = conversation.getParticipant1Name().equalsIgnoreCase(mc.thePlayer.getName()) ?
                conversation.getParticipant2Name() : conversation.getParticipant1Name();

        mc.fontRendererObj.drawString(playerName, 0, 0, 0xFFFFFF);

        String message = conversation.getMessages().get(conversation.getMessages().size() - 1);
        String displayMessage = message;
        if (mc.fontRendererObj.getStringWidth(message) > width)
            displayMessage = mc.fontRendererObj.trimStringToWidth(message.substring(0, message.length() - 4), width) + "...";


        mc.fontRendererObj.drawString(displayMessage, 0, 10, 0xFFFFFF);
    }

    @Override
    public int getSize() {
        return Mail.conversations.size();
    }

    @Override
    public void elementClicked(int index, boolean doubleClick) {
        if (doubleClick) {
            App.activeApp = new GuiConversation((Conversation) Mail.conversations.toArray()[index]);
            App.activeApp.screenBounds = parentApp.screenBounds;
            App.activeApp.setWorldAndResolution(parentApp.mc, parentApp.width, parentApp.height);
            App.activeApp.initGui();
        }
    }
}
