package com.ipixelmon.tablet.client.apps.friends;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;

/**
 * Created by colby on 11/7/2016.
 */
public class GuiFriendRequests extends CustomScrollList {

    private Minecraft mc;

    public GuiFriendRequests(Minecraft client, int x, int y, int width, int height, int entryHeight, GuiScreen screen) {
        super(client, x, y, width, height, entryHeight, screen);
        mc = client;
    }

    @Override
    protected int getSize() {
        return FriendsAPI.getFriendRequests().size();
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick) {
        selectedIndex = index;
    }

    @Override
    protected boolean isSelected(int index) {
        return selectedIndex == index;
    }

    @Override
    protected void drawBackground() {
        drawRect(this.xPosition - 1, this.yPosition - 1, this.xPosition + this.width + 1, this.yPosition + this.height + 1, -6250336);
        drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, -16777216);
    }

    @Override
    protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
        String name = ((FriendRequest) FriendsAPI.getFriendRequests().toArray()[slotIdx]).name;
        mc.fontRendererObj.setUnicodeFlag(true);
        mc.fontRendererObj.drawString(name, left + 2, slotTop, 0xFFFFFF, false);
        mc.fontRendererObj.setUnicodeFlag(false);
    }
}
