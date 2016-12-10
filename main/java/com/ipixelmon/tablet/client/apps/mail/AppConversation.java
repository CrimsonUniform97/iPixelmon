package com.ipixelmon.tablet.client.apps.mail;

import com.ipixelmon.GuiScrollingTextField;
import com.ipixelmon.tablet.client.App;

import java.io.IOException;

/**
 * Created by colby on 12/10/2016.
 */
public class AppConversation extends App {

    GuiConversation guiConversation;
    Conversation conversation;
    GuiScrollingTextField textField;

    public AppConversation(Conversation conversation) {
        super("conversation", false);
        this.conversation = conversation;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        guiConversation.draw(mouseX, mouseY);
        textField.draw(mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        textField.mouseClicked(mouseX, mouseY);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        guiConversation.keyTyped(typedChar, keyCode);
        textField.keyTyped(typedChar, keyCode);
    }

    @Override
    public void initGui() {
        super.initGui();

        int listWidth = screenBounds.getWidth() - 50;
        int listHeight = screenBounds.getHeight() - 10;

        guiConversation = new GuiConversation((width - listWidth) / 2, (height - listHeight) / 2, listWidth, listHeight - 50, conversation);
        textField = new GuiScrollingTextField(guiConversation.xPosition, guiConversation.yPosition + guiConversation.height + 4, guiConversation.width, 50);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

}
