package com.ipixelmon.tablet.apps.friends;

import com.ipixelmon.GuiScrollList;
import com.ipixelmon.GuiUtil;
import com.ipixelmon.iPixelmon;
import net.minecraft.util.ResourceLocation;
import scala.actors.threadpool.Arrays;

import java.util.UUID;

/**
 * Created by colbymchenry on 12/27/16.
 */
public class GuiFriendRequestsList extends GuiScrollList {

    private static ResourceLocation tick = new ResourceLocation(iPixelmon.id, "textures/apps/friends/tick.png");
    private static ResourceLocation cross = new ResourceLocation(iPixelmon.id, "textures/apps/friends/cross.png");
    private int tickX;
    private int crossX;

    public GuiFriendRequestsList(int xPosition, int yPosition, int width, int height) {
        super(xPosition, yPosition, width, height);
    }

    @Override
    public int getObjectHeight(int index) {
        return 20;
    }

    @Override
    public void drawObject(int index, int mouseX, int mouseY, boolean isHovering) {
        UUID id = (UUID) Friends.getFriendRequests().toArray()[index];
        mc.fontRendererObj.drawString(Friends.getFriendRequestName(id), 0, 0, 0xFFFFFF);

        crossX = width - 24;
        tickX = crossX - 24;

        if (isHovering) {
            mc.getTextureManager().bindTexture(tick);
            GuiUtil.drawImage(tickX, 2, 16, 16);
            mc.getTextureManager().bindTexture(cross);
            GuiUtil.drawImage(crossX, 2, 16, 16);

            if (mouseX > crossX && mouseX < crossX + 16 && mouseY > 2 && mouseY < 2 + 16) {
                GuiUtil.drawHoveringText(Arrays.asList(new String[]{"Deny"}), mouseX, mouseY);
            }

            if (mouseX > tickX && mouseX < tickX + 16 && mouseY > 2 && mouseY < 2 + 16) {
                GuiUtil.drawHoveringText(Arrays.asList(new String[]{"Accept"}), mouseX, mouseY);
            }
        }
    }

    @Override
    public int getSize() {
        return Friends.getFriendRequests().size();
    }

    @Override
    public void elementClicked(int index, int mouseX, int mouseY, boolean doubleClick) {
        UUID id = (UUID) Friends.getFriendRequests().toArray()[index];

        if (mouseX > crossX && mouseX < crossX + 16 && mouseY > 2 && mouseY < 2 + 16) {
            iPixelmon.network.sendToServer(new PacketFriendRequestToServer(id, true));
        }

        if (mouseX > tickX && mouseX < tickX + 16 && mouseY > 2 && mouseY < 2 + 16) {
            iPixelmon.network.sendToServer(new PacketFriendRequestToServer(id, false));
        }
    }

}
