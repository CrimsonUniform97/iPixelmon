package com.ipixelmon.tablet.client.apps.mail;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Rectangle;

/**
 * Created by colby on 11/21/2016.
 */
public abstract class CustomSizeScrollList extends Gui {

    public int xPosition, yPosition, width, height;

    private float scrollY = 0f;
    private Rectangle bounds, scrollbarBounds;
    protected Minecraft mc = Minecraft.getMinecraft();

    public CustomSizeScrollList(int xPosition, int yPosition, int width, int height) {
        bounds = new Rectangle(this.xPosition = xPosition, this.yPosition = yPosition, this.width = width, this.height = height);
        scrollbarBounds = new Rectangle();
    }

    public void draw(int mouseX, int mouseY) {
        GlStateManager.disableTexture2D();
        {
            GlStateManager.color(0, 0, 0, 1);
            this.drawTexturedModalRect(xPosition, yPosition, 0, 0, width, height);
            GlStateManager.color(1, 1, 1, 1);
        }
        GlStateManager.enableTexture2D();

        int contentSize = 0;
        for (int i = 0; i < getSize(); i++) contentSize += getObjectHeight(i);

        GlStateManager.pushMatrix();
        {
            int heightSoFar = 0;

            GlStateManager.translate(xPosition, yPosition + heightSoFar + scrollY, 0);

            for (int i = 0; i < getSize(); i++) drawObject(i, 0, heightSoFar += getObjectHeight(i));

        }
        GlStateManager.popMatrix();

        int windowSize = height;
        float trackSize = windowSize - 20;
        float windowContentRatio = windowSize / contentSize;
        float gripSize = trackSize * windowContentRatio;
        gripSize = gripSize < 20 ? 20 : gripSize > trackSize ? trackSize : gripSize;
        float windowScrollAreaSize = contentSize - windowSize;
        float windowPositionRatio = scrollY / windowScrollAreaSize;
        float trackScrollAreaSize = trackSize - gripSize;
        float gripPositionOnTrack = trackScrollAreaSize * windowPositionRatio;

        scrollbarBounds.setBounds(xPosition + width - 5, yPosition + (int) gripPositionOnTrack, 5, (int) gripSize);

        GlStateManager.pushMatrix();
        {
            GlStateManager.translate(scrollbarBounds.getX(), scrollbarBounds.getY(), 0);
            GlStateManager.disableTexture2D();
            {
                GlStateManager.color(128f / 255f, 128f / 255f, 128f / 255f, 1);
                // TODO: Draw grip size with float
                this.drawTexturedModalRect(0, 0, 0, 0, scrollbarBounds.getWidth(), scrollbarBounds.getHeight());
            }
            GlStateManager.enableTexture2D();
        }
        GlStateManager.popMatrix();


        scrollY += Mouse.getDWheel() * -1;

        if (Mouse.isButtonDown(0) && scrollbarBounds.contains(mouseX, mouseY)) {
            scrollY = (mouseY - yPosition) / windowContentRatio;
            System.out.println(scrollY + "," + height);
//            scrollY = scrollY < 0 ? 0 : scrollY + scrollbarBounds.getHeight() > height ? scrollY + scrollbarBounds.getHeight() : scrollY;
        }
    }

    public abstract int getObjectHeight(int index);

    public abstract void drawObject(int index, int x, int y);

    public abstract int getSize();

}
