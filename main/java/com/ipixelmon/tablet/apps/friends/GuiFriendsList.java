package com.ipixelmon.tablet.apps.friends;

import com.ipixelmon.GuiScrollList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.EnumChatFormatting;

/**
 * Created by colbymchenry on 11/4/16.
 */
public class GuiFriendsList extends GuiScrollList {


    public GuiFriendsList(int xPosition, int yPosition, int width, int height) {
        super(xPosition, yPosition, width, height);
    }

    @Override
    public int getSize() {
        return FriendsAPI.getFriends(false).size();
    }

    @Override
    public void elementClicked(int index, boolean doubleClick) {

    }

    @Override
    public void drawBackground() {
        drawRect(this.xPosition - 1, this.yPosition - 1, this.xPosition + this.width + 1, this.yPosition + this.height + 1, -6250336);
        drawRect(this.xPosition, this.yPosition, this.xPosition + this.width, this.yPosition + this.height, -16777216);
    }

    @Override
    public int getObjectHeight(int index) {
        return 10;
    }

    @Override
    public void drawObject(int index, int mouseX, int mouseY) {
        Friend friend = (Friend) FriendsAPI.getFriends(false).toArray()[index];
        mc.fontRendererObj.setUnicodeFlag(true);
        mc.fontRendererObj.drawString((friend.online ? EnumChatFormatting.GREEN : EnumChatFormatting.RED) + friend.name, 2, 0, 0xFFFFFF, false);
        mc.fontRendererObj.setUnicodeFlag(false);
    }

    public Friend getSelectedFriend() {
        if(getSelected() > -1) {
            return (Friend) Friends.friends.toArray()[getSelected()];
        } else {
            return null;
        }
    }
}
