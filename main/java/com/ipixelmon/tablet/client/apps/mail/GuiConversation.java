package com.ipixelmon.tablet.client.apps.mail;

import com.ipixelmon.GuiScrollList;
import com.ipixelmon.pixelbay.gui.ColorPicker;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;

import java.awt.*;
import java.util.UUID;

/**
 * Created by colby on 12/10/2016.
 */
public class GuiConversation extends GuiScrollList {

    private Conversation conversation;
    private int maxWidth;

    public GuiConversation(int xPosition, int yPosition, int width, int height, Conversation conversation) {
        super(xPosition, yPosition, width, height);
        this.conversation = conversation;
        maxWidth = (width / 2) + 25;
    }

    @Override
    public int getObjectHeight(int index) {
        String[] data = conversation.getMessages().get(index).split("\\\\u2666");
        int totalHeight = mc.fontRendererObj.listFormattedStringToWidth(data[1], maxWidth).size() * mc.fontRendererObj.FONT_HEIGHT;
        return totalHeight + mc.fontRendererObj.FONT_HEIGHT + ((conversation.getPlayers().size() > 2) ? mc.fontRendererObj.FONT_HEIGHT : 0);
    }

    @Override
    public void drawObject(int index) {
        String[] data = conversation.getMessages().get(index).split("\\\\u2666");
        String player = conversation.getPlayers().get(UUID.fromString(data[0]));
        String message = data[1];

        if (UUID.fromString(data[0]).equals(mc.thePlayer.getUniqueID())) {

            int maxLength = 0;
            for (String s : mc.fontRendererObj.listFormattedStringToWidth(message, maxWidth)) {
                if (mc.fontRendererObj.getStringWidth(s) > maxLength) maxLength = mc.fontRendererObj.getStringWidth(s);
            }


            int x = width - maxLength - 10;

            drawRect(new Rectangle(x, 0, maxLength + 4, getObjectHeight(index) - 2), ColorPicker.color(0f, 145f, 234f, 255f));
            mc.fontRendererObj.drawSplitString(message, x + 2, 4, maxWidth, 0xFFFFFF);

            // TODO: Need to test multiple people in one chat
            if(conversation.getPlayers().size() > 2) {
                mc.fontRendererObj.drawString(player, 0, getObjectHeight(index) - mc.fontRendererObj.FONT_HEIGHT, 0xFFFFFF);
            }


        } else {
            int maxLength = 0;
            for (String s : mc.fontRendererObj.listFormattedStringToWidth(message, maxWidth)) {
                if (mc.fontRendererObj.getStringWidth(s) > maxLength) maxLength = mc.fontRendererObj.getStringWidth(s);
            }
            drawRect(new Rectangle(0, 0, maxLength + 4, getObjectHeight(index) - 2), ColorPicker.color(66f, 66f, 66f, 255f));
            mc.fontRendererObj.drawSplitString(message, 2, 4, maxWidth, 0xFFFFFF);

            // TODO: Need to test multiple people in one chat
            if(conversation.getPlayers().size() > 2) {
                mc.fontRendererObj.drawString(player, 0, getObjectHeight(index) - mc.fontRendererObj.FONT_HEIGHT, 0xFFFFFF);
            }
        }
    }

    @Override
    public int getSize() {
        return conversation.getMessages().size();
    }

    @Override
    public void elementClicked(int index, boolean doubleClick) {

    }

    @Override
    public void drawSelectionBox(int index, int width, int height) {
//        super.drawSelectionBox(index, width, height);
    }

    @Override
    public void drawBackground() {
        GL11.glDisable(GL11.GL_CULL_FACE);

        int x = bounds.getX() - 2;
        int y = bounds.getY() - 2;
        int width = bounds.getX() + bounds.getWidth() + 2;
        int height = bounds.getY() + bounds.getHeight() + 2;

        GlStateManager.color(128f/255f, 128f/255f, 128f/255f, 1f);
        GL11.glBegin(GL11.GL_QUADS);

        {
            GL11.glVertex2f(x, y);
            GL11.glVertex2f(width, y);
            GL11.glVertex2f(width, height);
            GL11.glVertex2f(x, height);
        }
        GL11.glEnd();

        GlStateManager.color(20f/255f, 20f/255f, 20f/255f, 1f);
        GL11.glBegin(GL11.GL_QUADS);

        {
            GL11.glVertex2f(x + 1, y + 1);
            GL11.glVertex2f(width - 1, y + 1);
            GL11.glVertex2f(width - 1, height - 1);
            GL11.glVertex2f(x + 1, height - 1) ;
        }
        GL11.glEnd();

        GL11.glEnable(GL11.GL_CULL_FACE);
    }

    public void drawRect(Rectangle rect, Color bgColor) {
        int x = rect.getX(), y = rect.getY(), w = rect.getWidth(), h = rect.getHeight();
        x += 4;
        y += 4;
        w -= 8;
        h -= 8;
        int l = bgColor.getRGB();
        this.drawGradientRect(x - 3, y - 4, x + w + 3, y - 3, l, l);
        this.drawGradientRect(x - 3, y + h + 3, x + w + 3, y + h + 4, l, l);
        this.drawGradientRect(x - 3, y - 3, x + w + 3, y + h + 3, l, l);
        this.drawGradientRect(x - 4, y - 3, x - 3, y + h + 3, l, l);
        this.drawGradientRect(x + w + 3, y - 3, x + w + 4, y + h + 3, l, l);
    }
}
