package com.ipixelmon.tablet.app.pixelbay;

import com.ipixelmon.GuiScrollList;

/**
 * Created by colby on 12/31/2016.
 */
public class GuiPixelmonList extends GuiScrollList {

    public GuiPixelmonList(int xPosition, int yPosition, int width, int height) {
        super(xPosition, yPosition, width, height);
    }

    @Override
    public int getObjectHeight(int index) {
        return 0;
    }

    @Override
    public void drawObject(int index, int mouseX, int mouseY, boolean isHovering) {

    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public void elementClicked(int index, int mouseX, int mouseY, boolean doubleClick) {

    }
}
