package com.ipixelmon.tablet.app.pixelbay.lists.buy;

import com.ipixelmon.tablet.app.pixelbay.ItemListing;
import com.ipixelmon.tablet.app.pixelbay.PixelbayAPI;
import com.ipixelmon.tablet.app.pixelbay.lists.IScrollListWithDesign;
import com.ipixelmon.util.ItemUtil;
import org.lwjgl.opengl.GL11;

/**
 * Created by colby on 12/31/2016.
 */
public class GuiItemListingList extends IScrollListWithDesign {

    public GuiItemListingList(int xPosition, int yPosition, int width, int height) {
        super(xPosition, yPosition, width, height);
    }

    @Override
    public int getObjectHeight(int index) {
        return 30;
    }

    @Override
    public void drawObject(int index, int mouseX, int mouseY, boolean isHovering) {
        ItemListing itemListing = PixelbayAPI.Client.itemListings.get(index);
        ItemUtil.Client.renderItem(itemListing.getItem(), 4, (30 - 16) / 2, this.width, this.height, mouseX, mouseY);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
    }

    @Override
    public int getSize() {
        return PixelbayAPI.Client.itemListings.size();
    }

    @Override
    public void elementClicked(int index, int mouseX, int mouseY, boolean doubleClick) {

    }

}
