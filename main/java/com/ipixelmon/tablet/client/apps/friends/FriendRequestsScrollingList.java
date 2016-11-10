package com.ipixelmon.tablet.client.apps.friends;

import com.google.common.collect.Lists;
import com.ipixelmon.pixelbay.gui.BasicScrollList;
import com.ipixelmon.uuidmanager.UUIDManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraftforge.fml.client.GuiScrollingList;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by colby on 11/7/2016.
 */
public class FriendRequestsScrollingList extends GuiScrollingList {

    private Minecraft mc;
    private List<FriendRequest> friendRequests;
    private List<String> names;

    public FriendRequestsScrollingList(Set<FriendRequest> friendRequests, Minecraft client, int x, int y, int width, int height, int entryHeight, GuiScreen screen) {
        super(client, width, height, y, y + height, x, entryHeight, screen.width, screen.height);
        mc = client;
        this.friendRequests = Lists.newArrayList();
        for(FriendRequest request : friendRequests) this.friendRequests.add(request);

        this.names = new ArrayList<>();
        for(FriendRequest request : friendRequests)
            this.names.add(UUIDManager.getPlayerName(request.friend));
    }

    @Override
    protected int getSize() {
        return friendRequests.size();
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

    }

    @Override
    protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
//        FriendRequest request = friendRequests.get(slotIdx);
        mc.fontRendererObj.drawString(names.get(slotIdx), left, slotTop, 0xFFFFFF, false);
    }
}
