package com.ipixelmon.tablet.client.apps.mail;

import com.ipixelmon.GuiMultiLineTextField;
import com.ipixelmon.tablet.client.CustomGuiTextField;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;

import java.io.IOException;

/**
 * Created by colbymchenry on 11/16/16.
 */
public class GuiQuickResponse extends GuiScreen {

    private CustomGuiTextField sender;
    private GuiMultiLineTextField msgTxtField, resTxtField;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        sender.drawTextBox();
        msgTxtField.drawTextField(mouseX, mouseY);
        resTxtField.drawTextField(mouseX, mouseY);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        resTxtField.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        resTxtField.mouseClicked(mouseX, mouseY);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
    }

    @Override
    public void initGui() {
        super.initGui();

        int txtFieldWidth = 100, txtFieldHeight = 60;
        int centerX = (this.width - txtFieldWidth) / 2, centerY = (this.height = txtFieldHeight) / 2;

        msgTxtField = new GuiMultiLineTextField(centerX, centerY - (txtFieldHeight / 2), txtFieldWidth, txtFieldHeight);
        resTxtField = new GuiMultiLineTextField(centerX, centerY + (txtFieldHeight / 2), txtFieldWidth, txtFieldHeight);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        sender.updateCursorCounter();
    }

}
