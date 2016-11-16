package com.ipixelmon.tablet.client.apps.mail;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.client.App;
import com.ipixelmon.GuiMultiLineTextField;
import com.ipixelmon.tablet.client.GuiTextField;
import com.ipixelmon.tablet.client.apps.friends.GuiFriends;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;

import java.io.*;

/**
 * Created by colby on 10/28/2016.
 */
public class Mail extends App {

    private static ResourceLocation icon, icon_new;

    private GuiTextField playerTxtField;
    private GuiFriends guiFriends;
    private GuiMultiLineTextField messageTxtField;

    public Mail(String name) {
        super(name);
        icon = getIcon(false);
        icon_new = getIcon(true);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        playerTxtField.drawTextBox();
        guiFriends.drawScreen(mouseX, mouseY, partialTicks);
        messageTxtField.drawTextField(mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        playerTxtField.mouseClicked(mouseX, mouseY, mouseButton);
        messageTxtField.mouseClicked(mouseX, mouseY);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        playerTxtField.textboxKeyTyped(typedChar, keyCode);
        messageTxtField.keyTyped(typedChar, keyCode);
    }

    // TODO: Work on this app.

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();
        guiFriends = new GuiFriends(mc, screenBounds.getX() + 2, screenBounds.getY() + 2, 65, 100, 12, this);

        FontRenderer fontRenderer = fontRendererObj;
        fontRenderer.setUnicodeFlag(true);

        playerTxtField = new GuiTextField(0, fontRenderer, guiFriends.xPosition + guiFriends.width + 5, guiFriends.yPosition, 65, 10);
        messageTxtField = new GuiMultiLineTextField(playerTxtField.xPosition, playerTxtField.yPosition + playerTxtField.height + 2, 100, 100);
        messageTxtField.setUnicodeFlag(true);
    }

    @Override
    public ResourceLocation getIcon() {
        return icon;
    }

    private ResourceLocation getIcon(boolean isNew) {
        return new ResourceLocation(iPixelmon.id, "textures/apps/" + name + "/" + (isNew ? "icon_new.png" : "icon.png"));
    }
}
