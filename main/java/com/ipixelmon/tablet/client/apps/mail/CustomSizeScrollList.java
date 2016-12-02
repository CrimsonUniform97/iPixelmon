package com.ipixelmon.tablet.client.apps.mail;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;

/**
 * Created by colby on 11/21/2016.
 */
public abstract class CustomSizeScrollList extends Gui {

    public int xPosition, yPosition, width, height;

    private float scrollY = 0;
    private Rectangle bounds, scrollbarBounds;
    protected Minecraft mc = Minecraft.getMinecraft();


    public CustomSizeScrollList(int xPosition, int yPosition, int width, int height) {
        bounds = new Rectangle(this.xPosition = xPosition, this.yPosition = yPosition, this.width = width, this.height = height);
        scrollbarBounds = new Rectangle();
    }

    public void draw(int mouseX, int mouseY) {
        int contentHeight = getContentHeight();
        contentHeight = contentHeight == 0 ? 1 : contentHeight;

        float ratio = bounds.getHeight() / contentHeight;
        float gripSize = bounds.getHeight() * ratio;
        float trackSize = bounds.getHeight();

//The minimal size of our grip
        float maximumGripSize = trackSize;

//If the grip is too large, set it so that it is at our maximum size!
        if (gripSize > maximumGripSize)
            gripSize = maximumGripSize;


//The minimal size of our grip
        float minimalGripSize = 20;

//If the grip is too small, set it so that it is at our predetermined minimal size!
        if (gripSize < minimalGripSize)
            gripSize = minimalGripSize;

        float trackScrollAreaSize = trackSize - gripSize;

        float windowScrollAreaSize = contentHeight - bounds.getHeight();
        float windowPositionRatio = scrollY / windowScrollAreaSize;
        float gripPosition = trackScrollAreaSize * windowPositionRatio;

        boolean isHovering = mouseX >= bounds.getX() && mouseX <= bounds.getX() + bounds.getWidth() &&
                mouseY >= bounds.getY() && mouseY <= bounds.getY() + bounds.getHeight();

        if (Mouse.isButtonDown(0) && isHovering) {
            // TODO: Figure out how to click inside scroll bar and then move the scrollbar without the top of the scrollbar being right at your mouse
            float newGripPosition = mouseY - bounds.getY();

            newGripPosition = newGripPosition < 0 ? 0 : newGripPosition > trackScrollAreaSize ? trackScrollAreaSize : newGripPosition;
            float newGripPositionRatio = newGripPosition / trackScrollAreaSize;
            scrollY = newGripPositionRatio * windowScrollAreaSize;
        }


        GlStateManager.disableTexture2D();
        GL11.glDisable(GL11.GL_CULL_FACE);
        GlStateManager.color(0f, 0f, 0f, 1f);
        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glVertex2f(bounds.getX(), bounds.getY());
            GL11.glVertex2f(bounds.getX() + bounds.getWidth(), bounds.getY());
            GL11.glVertex2f(bounds.getX() + bounds.getWidth(), bounds.getY() + bounds.getHeight());
            GL11.glVertex2f(bounds.getX(), bounds.getY() + bounds.getHeight());
        }
        GL11.glEnd();

        GlStateManager.color(128f / 255f, 128f / 255f, 128f / 255f, 1f);

        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glVertex2f(bounds.getX() + bounds.getWidth() - 5, bounds.getY() + gripPosition);
            GL11.glVertex2f(bounds.getX() + bounds.getWidth(), bounds.getY() + gripPosition);
            GL11.glVertex2f(bounds.getX() + bounds.getWidth(), bounds.getY() + gripPosition + gripSize);
            GL11.glVertex2f(bounds.getX() + bounds.getWidth() - 5, bounds.getY() + gripPosition + gripSize);
        }
        GL11.glEnd();

        GlStateManager.enableTexture2D();
        GL11.glEnable(GL11.GL_CULL_FACE);

        ScaledResolution res = new ScaledResolution(mc);
        double scaleW = mc.displayWidth / res.getScaledWidth_double();
        double scaleH = mc.displayHeight / res.getScaledHeight_double();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((int) (bounds.getX() * scaleW), (int) (mc.displayHeight - (bounds.getY() + bounds.getHeight() * scaleH)),
                (int) (bounds.getWidth() * scaleW), (int) (bounds.getHeight() * scaleH));

        int totalHeight = 0;
        for (int i = 0; i < getSize(); i++) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(bounds.getX(), bounds.getY() + totalHeight - scrollY, 0);
            drawObject(i, 0, 0);
            GlStateManager.popMatrix();
            totalHeight += getObjectHeight(i);
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public void keyTyped(char keycode, int keynum) {
//The variable aSingleUnit here, can mean any one unit, the unit is undetermined
//and is specific to the implementation
        float aSingleUnit = 10; //10 unknown units

/////////////////////////////////////////////////////////////////////////////
//On pressing the Up Button (Up Directional Arrow)
        if (keynum == Keyboard.KEY_UP) {
            scrollY = scrollY - aSingleUnit;
            if (scrollY < 0)
                scrollY = 0;
        }

//Update the Scrollbar
//        updateScrollbar();

/////////////////////////////////////////////////////////////////////////////
//On pressing the Down Button (Down Directional Arrow)
        if (keynum == Keyboard.KEY_DOWN) {
            float windowScrollAreaSize = getContentHeight() - bounds.getHeight();

            scrollY = scrollY + aSingleUnit;
            if (scrollY > windowScrollAreaSize)
                scrollY = windowScrollAreaSize;
        }
    }

    private int getContentHeight() {
        int contentHeight = 0;

        for (int i = getSize(); i > 0; i--)
            contentHeight += getObjectHeight(i);

        return contentHeight;
    }

    public abstract int getObjectHeight(int index);

    public abstract void drawObject(int index, int x, int y);

    public abstract int getSize();

}
