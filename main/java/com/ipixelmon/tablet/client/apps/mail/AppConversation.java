package com.ipixelmon.tablet.client.apps.mail;

import com.ipixelmon.GuiScrollingTextField;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.client.App;
import com.ipixelmon.tablet.client.apps.mail.packets.PacketSendMessage;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;

/**
 * Created by colby on 12/10/2016.
 */
public class AppConversation extends App {

    private GuiConversation guiConversation;
    private Conversation conversation;
    private GuiScrollingTextField textField;

    // TODO: Test conversation between two people.

    public AppConversation(Conversation conversation) {
        super("conversation", false);
        this.conversation = conversation;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        int dWheel = Mouse.getDWheel();

        textField.draw(mouseX, mouseY, dWheel);
        guiConversation.draw(mouseX, mouseY, dWheel);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        textField.mouseClicked(mouseX, mouseY);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        if (keyCode != Keyboard.KEY_RETURN)
            textField.keyTyped(typedChar, keyCode);

        if (keyCode == Keyboard.KEY_RETURN) {
            iPixelmon.network.sendToServer(new PacketSendMessage(textField.getTextField().getText(), conversation.messageID));
            textField.getTextField().setText("");
            textField.getTextField().mouseClicked(textField.xPosition, textField.yPosition);
        }

    }

    @Override
    public void initGui() {
        super.initGui();

        int listWidth = screenBounds.getWidth() - 50;
        int listHeight = screenBounds.getHeight() - 10;

        guiConversation = new GuiConversation((width - listWidth) / 2, (height - listHeight) / 2, listWidth, listHeight - 50, conversation);
        textField = new GuiScrollingTextField(guiConversation.xPosition, guiConversation.yPosition + guiConversation.height + 4, guiConversation.width, 50);
        textField.getTextField().setEnabled(true);
        guiConversation.setScrollY(guiConversation.getMaxScrollY());
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    public GuiConversation getGuiConversation() {
        return guiConversation;
    }
}
