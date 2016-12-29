package com.ipixelmon.tablet.apps.friends;

import com.ipixelmon.GuiScrollList;
import com.ipixelmon.util.GuiUtil;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.util.Utils;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import scala.actors.threadpool.Arrays;

import java.util.UUID;

/**
 * Created by colbymchenry on 12/27/16.
 */
public class GuiFriendsList extends GuiScrollList {

    private static ResourceLocation cross = new ResourceLocation(iPixelmon.id, "textures/apps/friends/cross.png");

    public GuiFriendsList(int xPosition, int yPosition, int width, int height) {
        super(xPosition, yPosition, width, height);
    }

    @Override
    public int getObjectHeight(int index) {
        return 20;
    }

    @Override
    public void drawObject(int index, int mouseX, int mouseY, boolean isHovering) {
        UUID id = (UUID) FriendsAPI.Client.getFriends().toArray()[index];
        mc.fontRendererObj.drawString((FriendsAPI.Client.isFriendOnline(id) ? EnumChatFormatting.GREEN : EnumChatFormatting.RED) +
                FriendsAPI.Client.getFriendName(id), 0, 0, 0xFFFFFF);
        if(isHovering) {
            mc.getTextureManager().bindTexture(cross);
            Utils.Client.gui.drawImage(width - 24, 2, 16, 16);

            if(mouseX > width - 24 && mouseX < width - 24 + 16 && mouseY > 2 && mouseY < 2 + 16) {
                Utils.Client.gui.drawHoveringText(Arrays.asList(new String[]{"Remove"}), mouseX, mouseY);
            }
        }
    }

    @Override
    public int getSize() {
        return FriendsAPI.Client.getFriends().size();
    }

    @Override
    public void elementClicked(int index, int mouseX, int mouseY, boolean doubleClick) {
    }

    public UUID getSelectedID() {
        if(getSelected() > -1) {
            return (UUID) FriendsAPI.Client.getFriends().toArray()[getSelected()];
        }

        return null;
    }
}
