package com.ipixelmon.tablet.client.apps.mail;

import com.ipixelmon.TimedMessage;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.client.App;
import com.ipixelmon.GuiMultiLineTextField;
import com.ipixelmon.tablet.client.CustomGuiTextField;
import com.ipixelmon.tablet.client.TextBtn;
import com.ipixelmon.tablet.client.apps.friends.GuiFriends;
import com.ipixelmon.tablet.client.apps.mail.packet.PacketSendMail;
import com.ipixelmon.uuidmanager.UUIDManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import java.io.*;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by colby on 10/28/2016.
 */
public class Mail extends App {

    private static ResourceLocation icon, icon_new;
    public static final Set<MailObject> messages = new TreeSet<>();

    public static TimedMessage message = new TimedMessage("", 0);
    private static CustomGuiTextField playerTxtField;
    private static GuiFriends guiFriends;
    private static GuiMultiLineTextField messageTxtField;
    private static InboxScrollList inboxScrollList;

    public Mail(String name) {
        super(name, true);
        icon = getIcon(false);
        icon_new = getIcon(true);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        playerTxtField.drawTextBox();
        guiFriends.drawScreen(mouseX, mouseY, partialTicks);
        messageTxtField.drawTextField(mouseX, mouseY);
        inboxScrollList.drawScreen(mouseX, mouseY, partialTicks);

        // TODO: Position this message
        if(!message.getMessage().isEmpty())
            mc.fontRendererObj.drawString(message.getMessage(), 0, 0, 0xFFFFFF, true);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        playerTxtField.mouseClicked(mouseX, mouseY, mouseButton);
        messageTxtField.mouseClicked(mouseX, mouseY);
        if (mouseX > guiFriends.xPosition && mouseX < guiFriends.xPosition + guiFriends.width
                && mouseY > guiFriends.yPosition && mouseY < guiFriends.yPosition + guiFriends.height) {
            if (guiFriends.getSelected() != null) playerTxtField.setText(guiFriends.getSelected().name);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        if (button.id == 0) {
            if(UUIDManager.getUUID(playerTxtField.getText()) != null) {
                iPixelmon.network.sendToServer(new PacketSendMail(messageTxtField.getText(), playerTxtField.getText()));
                // TODO: Clear text fields if sent successfully
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


    // TODO: Tried to scale and it failed. Would have been nice...
    // TODO: Work on this app.
    // TODO: Implement 'mailbox is full'
    // TODO: Implement quick response by clicking on the notification

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();
        guiFriends = new GuiFriends(mc, screenBounds.getX() + 2, screenBounds.getY() + 2, 65, 100, 12, this);

        FontRenderer fontRenderer = fontRendererObj;
        fontRenderer.setUnicodeFlag(true);

        playerTxtField = new CustomGuiTextField(0, fontRenderer, guiFriends.xPosition + guiFriends.width + 5, guiFriends.yPosition, 65, 10);
        messageTxtField = new GuiMultiLineTextField(playerTxtField.xPosition, playerTxtField.yPosition + playerTxtField.height + 2, 100, 100);
        messageTxtField.setUnicodeFlag(true);

        if (messages.isEmpty()) {
            ResultSet result = iPixelmon.mysql.query("SELECT mailID AS id FROM tabletMail WHERE" +
                    " receiver='" + Minecraft.getMinecraft().thePlayer.getUniqueID().toString() + "';");
            try {
                while (result.next()) messages.add(new MailObject(UUID.fromString(result.getString("id"))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        inboxScrollList = new InboxScrollList(mc,screenBounds.getX() +  200,screenBounds.getY() +  10, 115, screenBounds.getHeight() - 20, 30, this);

        this.buttonList.add(new TextBtn(0, messageTxtField.getBounds().getX(),
                messageTxtField.getBounds().getY() + messageTxtField.getBounds().getHeight(), 50, 10, "Send"));
    }

    @Override
    public void updateScreen() {
        playerTxtField.updateCursorCounter();
        this.buttonList.get(0).enabled = !messageTxtField.getText().isEmpty() && !playerTxtField.getText().isEmpty();
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
