package com.ipixelmon.tablet.apps.friends;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.EnumChatFormatting;

/**
 * Created by colbymchenry on 11/4/16.
 */
public class GuiFriendsList extends CustomScrollList {

    private Minecraft mc;

    public GuiFriendsList(Minecraft client, int x, int y, int width, int height, int entryHeight, GuiScreen screen) {
        super(client, x, y, width, height, entryHeight, screen);
        mc = client;
        FriendsAPI.getFriends(true);
    }

    @Override
    protected int getSize() {
        return FriendsAPI.getFriends(false).size();
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
        drawRect(this.xPosition - 1, this.yPosition - 1, this.xPosition + this.width + 1, this.yPosition + this.height + 1, -6250336);
        drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, -16777216);
    }

    @Override
    protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
        Friend friend = (Friend) FriendsAPI.getFriends(false).toArray()[slotIdx];
        mc.fontRendererObj.setUnicodeFlag(true);
        mc.fontRendererObj.drawString((friend.online ? EnumChatFormatting.GREEN : EnumChatFormatting.RED) + friend.name, left + 2, slotTop, 0xFFFFFF, false);
        mc.fontRendererObj.setUnicodeFlag(false);
    }

    public Friend getSelected() {
        if(selectedIndex > -1) {
            return (Friend) Friends.friends.toArray()[selectedIndex];
        } else {
            return null;
        }
    }
}
