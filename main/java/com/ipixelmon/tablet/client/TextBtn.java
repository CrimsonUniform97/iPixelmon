package com.ipixelmon.tablet.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;

/**
 * Created by colby on 11/13/2016.
 */
public class TextBtn extends GuiButton {

    public TextBtn(int buttonId, int x, int y, int width, int height, String buttonText) {
        super(buttonId, x, y, width, height, buttonText);
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        GlStateManager.disableTexture2D();
        GlStateManager.color(0, 0, 0, 1);
        drawTexturedModalRect(xPosition - 2, yPosition, 0, 0, mc.fontRendererObj.getStringWidth(displayString) + 3, mc.fontRendererObj.FONT_HEIGHT);

        GlStateManager.enableTexture2D();
        mc.fontRendererObj.drawString((!enabled ? TextFormatting.GRAY : mousePressed(mc, mouseX, mouseY) ? TextFormatting.YELLOW : TextFormatting.WHITE) + displayString, xPosition, yPosition, 0xFFFFFF, true);
    }
}
