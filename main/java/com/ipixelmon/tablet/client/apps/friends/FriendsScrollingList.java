package com.ipixelmon.tablet.client.apps.friends;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.GuiScrollingList;

import java.util.Set;

/**
 * Created by colbymchenry on 11/4/16.
 */
public class FriendsScrollingList extends GuiScrollingList {

    private Friend[] friends;
    private Minecraft mc;

    public FriendsScrollingList(Set<Friend> friends, Minecraft client, int width, int height, int top, int bottom, int left, int entryHeight, int screenWidth, int screenHeight) {
        super(client, width, height, top, bottom, left, entryHeight, screenWidth, screenHeight);
        mc = client;
        this.friends = (Friend[]) friends.toArray();
    }

    @Override
    protected int getSize() {
        return friends.length;
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
        mc.fontRendererObj.drawString(friends[slotIdx].name, left, slotTop, 0xFFFFFF, false);
        mc.fontRendererObj.drawString(String.valueOf(friends[slotIdx].online), entryRight - (mc.fontRendererObj.getStringWidth(String.valueOf(friends[slotIdx].online))), slotTop, 0xFFFFFF, false);
    }
}
