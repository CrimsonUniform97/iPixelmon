package com.ipixelmon.tablet.client.apps.mail;

import com.ipixelmon.tablet.client.apps.friends.CustomScrollList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;

/**
 * Created by colby on 11/18/2016.
 */
public class InboxScrollList extends CustomScrollList {

    public InboxScrollList(Minecraft mc, int x, int y, int width, int height, int entryHeight, GuiScreen screen) {
        super(mc, x, y, width, height, entryHeight, screen);
    }

    @Override
    protected int getSize() {
        return 0;
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick) {
        selectedIndex = index;
    }

    @Override
    protected boolean isSelected(int index) {
        return index == selectedIndex;
    }

    @Override
    protected void drawBackground() {

    }

    @Override
    protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {

    }
}
