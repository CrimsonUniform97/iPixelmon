package com.ipixelmon.tablet.client.apps.friends;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.client.GuiScrollingList;

import java.util.List;
import java.util.Set;

/**
 * Created by colbymchenry on 11/4/16.
 */
public class FriendsScrollingList extends GuiScrollingList {

    private List<Friend> friends;
    private Minecraft mc;

    public FriendsScrollingList(Set<Friend> friends, Minecraft client, int width, int height, int top, int bottom, int left, int entryHeight, int screenWidth, int screenHeight) {
        super(client, width, height, top, bottom, left, entryHeight, screenWidth, screenHeight);
        mc = client;
        this.friends = Lists.newArrayList();
        for(Friend friend : friends) this.friends.add(friend);
    }

    @Override
    protected int getSize() {
        return friends.size();
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
        Friend friend = friends.get(slotIdx);
        mc.fontRendererObj.drawString((friend.online ? EnumChatFormatting.GREEN : EnumChatFormatting.RED) + friend.name, left, slotTop, 0xFFFFFF, false);
    }
}
