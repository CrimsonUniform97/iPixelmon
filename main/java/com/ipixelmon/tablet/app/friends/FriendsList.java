package com.ipixelmon.tablet.app.friends;

import com.ipixelmon.GuiScrollList;
import net.minecraft.util.EnumChatFormatting;

/**
 * Created by colby on 1/5/2017.
 */
public class FriendsList extends GuiScrollList {

    public FriendsList(int xPosition, int yPosition, int width, int height) {
        super(xPosition, yPosition, width, height);
    }

    @Override
    public int getObjectHeight(int index) {
        return 10;
    }

    @Override
    public void drawObject(int index, int mouseX, int mouseY, boolean isHovering) {
        Friend friend = (Friend) FriendsAPI.Client.friends.toArray()[index];
        mc.fontRendererObj.drawString((friend.isOnline() ? EnumChatFormatting.GREEN : EnumChatFormatting.RED) +
                friend.getName(), 0, 0, 0xFFFFFF);
    }

    @Override
    public int getSize() {
        return FriendsAPI.Client.friends.size();
    }

    @Override
    public void elementClicked(int index, int mouseX, int mouseY, boolean doubleClick) {

    }

    public Friend getSelectedFriend() {
        return (Friend) FriendsAPI.Client.friends.toArray()[getSelected()];
    }

}
