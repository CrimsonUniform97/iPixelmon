package com.ipixelmon.tablet.app.friends;

import com.ipixelmon.GuiScrollList;

/**
 * Created by colby on 1/5/2017.
 */
public class FriendRequestsList extends GuiScrollList {

    public FriendRequestsList(int xPosition, int yPosition, int width, int height) {
        super(xPosition, yPosition, width, height);
    }

    @Override
    public int getObjectHeight(int index) {
        return 9;
    }

    @Override
    public void drawObject(int index, int mouseX, int mouseY, boolean isHovering) {
        FriendRequest friendRequest = (FriendRequest) FriendsAPI.Client.friendRequests.toArray()[index];

        mc.fontRendererObj.drawString(friendRequest.getSenderName(), 0, 0, 0xFFFFFF);
    }

    @Override
    public int getSize() {
        return FriendsAPI.Client.friendRequests.size();
    }

    @Override
    public void elementClicked(int index, int mouseX, int mouseY, boolean doubleClick) {

    }

    public FriendRequest getSelectedFriendRequest() {
        return (FriendRequest) FriendsAPI.Client.friendRequests.toArray()[getSelected()];
    }
}
