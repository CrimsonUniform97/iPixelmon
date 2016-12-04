package com.ipixelmon.tablet.client.apps.mail;

import com.google.common.collect.Lists;
import com.ipixelmon.TimedMessage;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.client.App;
import com.ipixelmon.GuiMultiLineTextField;
import com.ipixelmon.tablet.client.CustomGuiTextField;
import com.ipixelmon.tablet.client.TextBtn;
import com.ipixelmon.tablet.client.apps.friends.GuiFriendsList;
import com.ipixelmon.uuidmanager.UUIDManager;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import java.io.*;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by colby on 10/28/2016.
 */
public class Mail extends App {

    private static ResourceLocation icon, icon_new;

    public static TimedMessage message = new TimedMessage("", 0);
    private static CustomGuiTextField playerTxtField;
    private static GuiFriendsList guiFriendsList;
    private static GuiMultiLineTextField messageTxtField;

    public static Set<Conversation> conversations = new TreeSet<>();

    public Mail(String name) {
        super(name, true);
        icon = getIcon(false);
        icon_new = getIcon(true);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        playerTxtField.drawTextBox();
        guiFriendsList.drawScreen(mouseX, mouseY, partialTicks);
        messageTxtField.drawTextField();

        // TODO: Position this message
        if(!message.getMessage().isEmpty())
            mc.fontRendererObj.drawString(message.getMessage(), 0, 0, 0xFFFFFF, true);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        playerTxtField.mouseClicked(mouseX, mouseY, mouseButton);
        messageTxtField.mouseClicked(mouseX, mouseY);

        if (mouseX > guiFriendsList.xPosition && mouseX < guiFriendsList.xPosition + guiFriendsList.width
                && mouseY > guiFriendsList.yPosition && mouseY < guiFriendsList.yPosition + guiFriendsList.height) {
            if (guiFriendsList.getSelected() != null) playerTxtField.setText(guiFriendsList.getSelected().name);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        if (button.id == 0) {
            if(UUIDManager.getUUID(playerTxtField.getText()) != null) {
//                iPixelmon.network.sendToServer(new PacketSendMail(messageTxtField.getText(), playerTxtField.getText()));
            }  else {
                message.setMessage("Player does not exist...", 5);
            }
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        playerTxtField.textboxKeyTyped(typedChar, keyCode);
        messageTxtField.keyTyped(typedChar, keyCode);
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();

        guiFriendsList = new GuiFriendsList(mc, screenBounds.getX() + 2, screenBounds.getY() + 2, 65, 100, 12, this);

        FontRenderer fontRenderer = fontRendererObj;
        fontRenderer.setUnicodeFlag(true);

        playerTxtField = new CustomGuiTextField(0, fontRenderer, guiFriendsList.xPosition + guiFriendsList.width + 5, guiFriendsList.yPosition, 65, 10);
        messageTxtField = new GuiMultiLineTextField(playerTxtField.xPosition, playerTxtField.yPosition + playerTxtField.height + 2, 100, 100);
        messageTxtField.setUnicodeFlag(true);

        buttonList.add(new TextBtn(0, messageTxtField.getBounds().getX(),
                messageTxtField.getBounds().getY() + messageTxtField.getBounds().getHeight(), 50, 10, "Send"));
    }

    @Override
    public void updateScreen() {
        playerTxtField.updateCursorCounter();
        buttonList.get(0).enabled = !messageTxtField.getText().isEmpty() && !playerTxtField.getText().isEmpty();
    }

    @Override
    public ResourceLocation getIcon() {
        return icon;
    }

    private ResourceLocation getIcon(boolean isNew) {
        return new ResourceLocation(iPixelmon.id, "textures/apps/" + name + "/" + (isNew ? "icon_new.png" : "icon.png"));
    }

    public static void clearFields() {
        playerTxtField.setText("");
        messageTxtField.setText("");
    }

}
