package com.ipixelmon.tablet.client.apps.mail;

import com.ipixelmon.GuiScrollingTextField;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.client.App;
import com.ipixelmon.tablet.client.apps.mail.packets.PacketSendMessage;
import net.minecraft.client.Minecraft;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

/**
 * Created by colby on 12/10/2016.
 */
public class AppConversation extends App {

    GuiConversation guiConversation;
    Conversation conversation;
    GuiScrollingTextField textField;
    boolean setMax = false;

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

        if (keyCode != Keyboard.KEY_RETURN)
            textField.keyTyped(typedChar, keyCode);

        // TODO: Scroll down when new message comes through. Probably needs to be done in PacketReceiveMessage, not here. Figure it out.
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

        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(250L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                guiConversation.setScrollY(guiConversation.getMaxScrollY());
            }
        }.start();
    }

    @Override
    public void updateScreen() {
        super.updateScreen();

    }

}
