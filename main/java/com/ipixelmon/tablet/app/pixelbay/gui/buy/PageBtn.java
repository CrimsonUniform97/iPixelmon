package com.ipixelmon.tablet.app.pixelbay.gui.buy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;

/**
 * Created by colby on 1/4/2017.
 */
public class PageBtn extends GuiButton {

    public PageBtn(int buttonId, int x, int y, String buttonText) {
        super(buttonId, x, y, buttonText);
        this.width = Minecraft.getMinecraft().fontRendererObj.getStringWidth(buttonText);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        int color = this.mousePressed(mc, mouseX, mouseY) ? 16777120 : 16777215;
        mc.fontRendererObj.drawString(displayString, xPosition, yPosition, color);
    }
}
