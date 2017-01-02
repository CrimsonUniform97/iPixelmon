package com.ipixelmon.tablet.app.pixelbay.lists;

import com.ipixelmon.GuiScrollList;
import com.ipixelmon.util.GuiUtil;

import java.awt.*;

/**
 * Created by colby on 1/1/2017.
 */
public abstract class IScrollListWithDesign extends GuiScrollList {

    public IScrollListWithDesign(int xPosition, int yPosition, int width, int height) {
        super(xPosition, yPosition, width, height);
    }

    @Override
    public final void drawSelectionBox(int index, int width, int height) {
        Color bgColor = new Color(70f / 255f, 0f / 255f, 80f / 255f, 250f / 255f);
        Color trimColor = new Color(90f / 255f, 0f / 255f, 100f / 255f, 250f / 255f);

        int l = bgColor.getRGB();
        int x = 4;
        int y = 4;
        int zLevel = 0;
        width -= 12;
        height -= 8;

        GuiUtil.drawGradientRect(x - 3, y - 4, x + width + 3, y - 3, l, l, zLevel);
        GuiUtil.drawGradientRect(x - 3, y + height + 3, x + width + 3, y + height + 4, l, l, zLevel);
        GuiUtil.drawGradientRect(x - 3, y - 3, x + width + 3, y + height + 3, l, l, zLevel);
        GuiUtil.drawGradientRect(x - 4, y - 3, x - 3, y + height + 3, l, l, zLevel);
        GuiUtil.drawGradientRect(x + width + 3, y - 3, x + width + 4, y + height + 3, l, l, zLevel);

        int i1 = trimColor.getRGB();
        int j1 = (i1 & 16711422/* white */) >> 1 | i1 & -16777216/* black */;

        GuiUtil.drawGradientRect(x - 3, y - 3 + 1, x - 3 + 1, y + height + 3 - 1, i1, j1, zLevel);
        GuiUtil.drawGradientRect(x + width + 2, y - 3 + 1, x + width + 3, y + height + 3 - 1, i1, j1, zLevel);
        GuiUtil.drawGradientRect(x - 3, y - 3, x + width + 3, y - 3 + 1, i1, i1, zLevel);
        GuiUtil.drawGradientRect(x - 3, y + height + 2, x + width + 3, y + height + 3, j1, j1, zLevel);
    }

    @Override
    public final void drawBackground() {

        Color bgColor = new Color(16f / 255f, 0f / 255f, 16f / 255f, 250f / 255f);
        Color trimColor = new Color(29f / 255f, 0f / 255f, 102f / 255f, 250f / 255f);

        int l = -267386864;
        int x = bounds.getX();
        int y = bounds.getY();
        int height = bounds.getHeight();
        int width = bounds.getWidth();
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

    @Override
    public final void drawScrollbar(float gripSize) {
        int l = -267386864;
        int x = 5;
        int y = 2;
        float height = gripSize - 4;
        int width = -1;
        GuiUtil.drawGradientRect(x - 3, y - 4, x + width + 3, y - 3, l, l);
        GuiUtil.drawGradientRect(x - 3, y + height + 3, x + width + 3, y + height + 4, l, l);
        GuiUtil.drawGradientRect(x - 3, y - 3, x + width + 3, y + height + 3, l, l);
        GuiUtil.drawGradientRect(x - 4, y - 3, x - 3, y + height + 3, l, l);
        GuiUtil.drawGradientRect(x + width + 3, y - 3, x + width + 4, y + height + 3, l, l);
        int i1 = 1347420415;
        int j1 = (i1 & 16711422/* white */) >> 1 | i1 & -16777216/* black */;
        GuiUtil.drawGradientRect(x - 3, y - 3 + 1, x - 3 + 1, y + height + 3 - 1, i1, j1);
        GuiUtil.drawGradientRect(x + width + 2, y - 3 + 1, x + width + 3, y + height + 3 - 1, i1, j1);
        GuiUtil.drawGradientRect(x - 3, y - 3, x + width + 3, y - 3 + 1, i1, i1);
        GuiUtil.drawGradientRect(x - 3, y + height + 2, x + width + 3, y + height + 3, j1, j1);
    }
}
