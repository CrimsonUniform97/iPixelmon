package com.ipixelmon.tablet.app.pixelbay.lists;

import com.ipixelmon.GuiScrollList;
import com.ipixelmon.tablet.app.pixelbay.ItemListing;
import com.ipixelmon.tablet.app.pixelbay.PixelbayGui;

/**
 * Created by colby on 12/31/2016.
 */
public class GuiItemListingList extends GuiScrollList {

    public GuiItemListingList(int xPosition, int yPosition, int width, int height) {
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
