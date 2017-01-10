package com.ipixelmon.landcontrol.client.gui;

import com.google.common.collect.Maps;
import com.ipixelmon.util.GuiUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.util.Rectangle;

import java.awt.*;
import java.util.Map;

/**
 * Created by colbymchenry on 1/9/17.
 */
public class ColorPopupWindow extends Gui {

    private int xPosition, yPosition;
    private EnumChatFormatting selectedColor = EnumChatFormatting.RESET;
    private Map<Rectangle, EnumChatFormatting> colors = Maps.newHashMap();
    private Map<EnumChatFormatting, Integer> colorHexs = Maps.newHashMap();
    private boolean enabled = false;

    public ColorPopupWindow() {
        int x = 0;
        int y = 0;
        int width = 8 * 8;
        for(EnumChatFormatting color : EnumChatFormatting.values()) {
            if(color.isColor()) {
                if (x >= width) {
                    y += 10;
                    x = 0;
                }
            } else {
                y += 10;
                x = 0;
            }


            colors.put(new Rectangle(x, y, 8, 8), color);
            x += 8;
        }

        colorHexs.put(EnumChatFormatting.BLACK, 0x000000);
        colorHexs.put(EnumChatFormatting.DARK_BLUE, 0x0000AA);
        colorHexs.put(EnumChatFormatting.DARK_GREEN, 0x00AA00);
        colorHexs.put(EnumChatFormatting.DARK_AQUA, 0x00AAAA);
        colorHexs.put(EnumChatFormatting.DARK_RED, 0xAA0000);
        colorHexs.put(EnumChatFormatting.DARK_PURPLE, 0xAA00AA);
        colorHexs.put(EnumChatFormatting.GOLD, 0xFFAA00);
        colorHexs.put(EnumChatFormatting.GRAY, 0xAAAAAA);
        colorHexs.put(EnumChatFormatting.DARK_GRAY, 0x555555);
        colorHexs.put(EnumChatFormatting.BLUE, 0x5555FF);
        colorHexs.put(EnumChatFormatting.GREEN, 0x55FF55);
        colorHexs.put(EnumChatFormatting.AQUA, 0x55FFFF);
        colorHexs.put(EnumChatFormatting.RED, 0xFF5555);
        colorHexs.put(EnumChatFormatting.LIGHT_PURPLE, 0xFF55FF);
        colorHexs.put(EnumChatFormatting.YELLOW, 0xFFFF55);
        colorHexs.put(EnumChatFormatting.WHITE, 0xFFFFFF);
    }

    public void draw() {
        if(!enabled) return;

        drawWindowBg();

        GlStateManager.pushMatrix();
        GlStateManager.translate(xPosition, yPosition, 0);
        GlStateManager.disableTexture2D();
        for(Rectangle r : colors.keySet()) {
            EnumChatFormatting format = colors.get(r);
            if(format.isColor()) {
                Color color = new Color(colorHexs.get(format));
                GlStateManager.color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 1f);
                drawTexturedModalRect(r.getX(), r.getY(), 0, 0, r.getWidth(), r.getHeight());
            } else {
                GlStateManager.enableTexture2D();
                Minecraft.getMinecraft().fontRendererObj.drawString(format + format.name().substring(0,1) + format.name().substring(1).toLowerCase(), r.getX(), r.getY(), 0xFFFFFF);
                GlStateManager.disableTexture2D();
            }
        }

        GlStateManager.popMatrix();
    }

    public void mousePressed(int mouseX, int mouseY, int mouseBtn) {
        if(mouseBtn == 1) {
            this.xPosition = mouseX;
            this.yPosition = mouseY;
            enabled = true;
            return;
        }

        int x = mouseX - xPosition;
        int y = mouseY - yPosition;

        for(Rectangle r : colors.keySet()) {
            if(r.contains(x, y)) {
                selectedColor = colors.get(r);
                break;
            }
        }

        enabled = false;
    }

    public EnumChatFormatting getSelectedColor() {
        return selectedColor;
    }

    private void drawWindowBg() {
        int l = -267386864;
        int x = xPosition;
        int y = yPosition;
        int height = 77;
        int width = 69;
        int zLevel = 0;

        GuiUtil.drawGradientRect(x - 3, y - 4, x + width + 3, y - 3, l, l, zLevel);
        GuiUtil.drawGradientRect(x - 3, y + height + 3, x + width + 3, y + height + 4, l, l, zLevel);
        GuiUtil.drawGradientRect(x - 3, y - 3, x + width + 3, y + height + 3, l, l, zLevel);
        GuiUtil.drawGradientRect(x - 4, y - 3, x - 3, y + height + 3, l, l, zLevel);
        GuiUtil.drawGradientRect(x + width + 3, y - 3, x + width + 4, y + height + 3, l, l, zLevel);
        int i1 = 1347420415;
        int j1 = (i1 & 16711422/* white */) >> 1 | i1 & -16777216/* black */;
        GuiUtil.drawGradientRect(x - 3, y - 3 + 1, x - 3 + 1, y + height + 3 - 1, i1, j1, zLevel);
        GuiUtil.drawGradientRect(x + width + 2, y - 3 + 1, x + width + 3, y + height + 3 - 1, i1, j1, zLevel);
        GuiUtil.drawGradientRect(x - 3, y - 3, x + width + 3, y - 3 + 1, i1, i1, zLevel);
        GuiUtil.drawGradientRect(x - 3, y + height + 2, x + width + 3, y + height + 3, j1, j1, zLevel);
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
