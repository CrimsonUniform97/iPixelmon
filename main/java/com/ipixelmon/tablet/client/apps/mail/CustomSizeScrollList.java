package com.ipixelmon.tablet.client.apps.mail;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;

/**
 * Created by colby on 11/21/2016.
 */
public abstract class CustomSizeScrollList extends Gui {

    public int xPosition, yPosition, width, height;

    private float scrollY = 0f;

    public void draw(int mouseX, int mouseY) {
        int contentSize = 0;
        for(int i = 0; i < getSize(); i++) contentSize += getObjectHeight(i);

        GlStateManager.pushMatrix();
        {
            int heightSoFar = 0;

            GlStateManager.translate(xPosition, yPosition + heightSoFar + scrollY, 0);

            for(int i = 0; i < getSize(); i++) drawObject(i, 0, heightSoFar += getObjectHeight(i));

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


        GlStateManager.pushMatrix();
        {
            GlStateManager.translate(xPosition + width - 5, yPosition + gripPositionOnTrack, 0);
            GlStateManager.disableTexture2D();
            {
                GlStateManager.color(128f / 255f, 128f / 255f, 128f / 255f, 1);
                // TODO: Draw grip size with float
                this.drawTexturedModalRect(0, 0, 0, 0, 5, (int) gripSize);
            }
            GlStateManager.enableTexture2D();
        }
        GlStateManager.popMatrix();
    }

    public abstract int getObjectHeight(int index);
    public abstract void drawObject(int index, int x, int y);
    public abstract int getSize();

}
