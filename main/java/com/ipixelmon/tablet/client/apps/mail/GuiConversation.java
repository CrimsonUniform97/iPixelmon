package com.ipixelmon.tablet.client.apps.mail;

import com.ipixelmon.GuiScrollList;
import net.minecraft.client.renderer.GlStateManager;

import java.util.UUID;

/**
 * Created by colby on 12/10/2016.
 */
public class GuiConversation extends GuiScrollList {

    private Conversation conversation;

    public GuiConversation(int xPosition, int yPosition, int width, int height, Conversation conversation) {
        super(xPosition, yPosition, width, height);
        this.conversation = conversation;
    }

    // TODO: Fix index out of bounds exception. But fix the scroll list flicker first.
    @Override
    public int getObjectHeight(int index) {
        String[] data = conversation.getMessages().get(index).split("\\u2666");
        String message = conversation.getPlayers().get(data[1]);

        int totalHeight = mc.fontRendererObj.listFormattedStringToWidth(message, width / 2).size() * mc.fontRendererObj.FONT_HEIGHT;
        return totalHeight + mc.fontRendererObj.FONT_HEIGHT;
    }

    @Override
    public void drawObject(int index) {
        String[] data = conversation.getMessages().get(index).split("\\u2666");
        String player = conversation.getPlayers().get(UUID.fromString(data[0]));
        String message = conversation.getPlayers().get(data[1]);

        if(UUID.fromString(data[0]).equals(mc.thePlayer.getUniqueID())) {
            GlStateManager.disableTexture2D();
            GlStateManager.color(0, 0, 1, 1);
            drawTexturedModalRect(width / 2, 0, 0, 0, width / 2, getObjectHeight(index));
            GlStateManager.color(1, 1, 1, 1);
            GlStateManager.enableTexture2D();
            mc.fontRendererObj.drawSplitString(message, width / 2, 0, width / 2, 0xFFFFFF);
        } else {
            GlStateManager.disableTexture2D();
            GlStateManager.color(1, 0, 0, 1);
            drawTexturedModalRect(0, 0, 0, 0, width / 2, getObjectHeight(index));
            GlStateManager.color(1, 1, 1, 1);
            GlStateManager.enableTexture2D();
            mc.fontRendererObj.drawSplitString(message, 0, 0, width / 2, 0xFFFFFF);
        }
    }

    @Override
    public int getSize() {
        return conversation.getMessages().size();
    }

    @Override
    public void elementClicked(int index, boolean doubleClick) {

    }
}
