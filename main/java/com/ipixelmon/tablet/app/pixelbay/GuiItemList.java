package com.ipixelmon.tablet.app.pixelbay;

import com.ipixelmon.GuiScrollList;
import com.ipixelmon.util.GuiUtil;

/**
 * Created by colby on 12/31/2016.
 */
public class GuiItemList extends GuiScrollList {

    public GuiItemList(int xPosition, int yPosition, int width, int height) {
        super(xPosition, yPosition, width, height);
    }

    @Override
    public int getObjectHeight(int index) {
        return 20;
    }

    @Override
    public void drawObject(int index, int mouseX, int mouseY, boolean isHovering) {
        ItemListing itemListing = PixelbayGui.itemListings.get(index);
    }

    @Override
    public int getSize() {
        return PixelbayGui.itemListings.size();
    }

    @Override
    public void elementClicked(int index, int mouseX, int mouseY, boolean doubleClick) {

    }

}
