package com.ipixelmon.tablet.client.apps.mail;

import com.ipixelmon.GuiScrollList;
import com.ipixelmon.tablet.client.App;
import net.minecraft.client.renderer.GlStateManager;

import java.util.UUID;

/**
 * Created by colby on 12/9/2016.
 */
public class GuiMessages extends GuiScrollList {

    public GuiMessages(int xPosition, int yPosition, int width, int height) {
        super(xPosition, yPosition, width, height);
    }

    @Override
    public int getObjectHeight(int index) {
        return 25;
    }

    @Override
    public void drawObject(int index) {
        Conversation conversation = (Conversation) Mail.messages.toArray()[index];

        String senders = "";

        for (String s : conversation.getPlayers().values()) {
            if (!s.equalsIgnoreCase(mc.thePlayer.getName())) {
                if (senders.isEmpty()) {
                    senders += s;
                } else {
                    senders += ", " + s;
                }
            }
        }

        mc.fontRendererObj.drawString(senders, 2, 2, 0xFFFFFF);

        String[] data = conversation.getMessages().get(conversation.getMessages().size() - 1).split("\\\\u2666");
        String message = data[1];

        if(mc.fontRendererObj.getStringWidth(message) > width - 25)
            message = mc.fontRendererObj.trimStringToWidth(message, width - 25) + "...";

        mc.fontRendererObj.drawString(message, 2, 14, 0x747475);

        if(index != getSize() - 1) {
            GlStateManager.disableTexture2D();
            GlStateManager.color(128f / 255f, 128f / 255f, 128f / 255f, 1f);
            this.drawTexturedModalRect(2, getObjectHeight(index) - 1, 0, 0, width - 4 - 5, 1);
            GlStateManager.color(1, 1, 1, 1);
            GlStateManager.enableTexture2D();
        }
    }

    @Override
    public int getSize() {
        return Mail.messages.size();
    }

    @Override
    public void drawSelectionBox(int index, int width, int height) {
        GlStateManager.disableTexture2D();
        GlStateManager.color(128f/255f, 128f/255f, 128f/255f, 1);
        this.drawTexturedModalRect(0,0,0,0,width - 7, getObjectHeight(index));
        GlStateManager.color(0,0,0,1);
        this.drawTexturedModalRect(1, 1,0,0, width - 9, getObjectHeight(index) - 2);
        GlStateManager.enableTexture2D();
    }

    @Override
    public void elementClicked(int index, boolean doubleClick) {
        if (doubleClick)
            App.getApp(Mail.class).setActiveApp(new AppConversation((Conversation) Mail.messages.toArray()[index]));
    }
}
