package com.ipixelmon.tablet.notification;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

/**
 * Created by colby on 11/5/2016.
 */
public class SimpleTextNotification extends Notification {

    private String text;
    private static final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRendererObj;

    public SimpleTextNotification(String text) {
        super();
        this.text = text;
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        GlStateManager.disableTexture2D();
        GlStateManager.color(0, 0, 0, 1);
        drawTexturedModalRect(0, 0, 0, 0, maxWidth, getHeight());
        GlStateManager.enableTexture2D();
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.enableBlend();
        Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("minecraft:textures/font/ascii.png"));
        fontRenderer.setUnicodeFlag(true);
        fontRenderer.drawSplitString(text, 2, 0, maxWidth, 0xFFFFFF);
        fontRenderer.setUnicodeFlag(false);
    }

    @Override
    public int getHeight() {
        fontRenderer.setUnicodeFlag(true);
        int height = (fontRenderer.splitStringWidth(text, maxWidth - 2) / fontRenderer.FONT_HEIGHT) * fontRenderer.FONT_HEIGHT;
        fontRenderer.setUnicodeFlag(false);
        return height;
    }

    @Override
    public int getWidth() {
        return maxWidth;
    }

    @Override
    public long getDuration() {
        return 10 * 1000L;
    }
}
