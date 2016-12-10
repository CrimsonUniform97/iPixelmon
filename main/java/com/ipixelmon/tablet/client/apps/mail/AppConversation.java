package com.ipixelmon.tablet.client.apps.mail;

import com.ipixelmon.tablet.client.App;

import java.io.IOException;

/**
 * Created by colby on 12/10/2016.
 */
public class AppConversation extends App {

    GuiConversation guiConversation;
    Conversation conversation;

    public AppConversation(Conversation conversation) {
        super("conversation", false);
        this.conversation = conversation;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        guiConversation.draw(mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        guiConversation.keyTyped(typedChar, keyCode);
    }

    @Override
    public void initGui() {
        super.initGui();

        int listWidth = screenBounds.getWidth() - 50;
        int listHeight = screenBounds.getHeight() - 10;

        guiConversation = new GuiConversation((width - listWidth) / 2, (height - listHeight) / 2, listWidth, listHeight, conversation);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

}
