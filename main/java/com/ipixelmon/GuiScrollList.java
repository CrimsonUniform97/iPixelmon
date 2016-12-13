package com.ipixelmon;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;

public abstract class GuiScrollList extends Gui {

    public int xPosition, yPosition, width, height;

    private float scrollY = 0, initialMouseClickY = -1f;
    protected Rectangle bounds;
    protected Minecraft mc = Minecraft.getMinecraft();
    private int selected = -1;
    private long lastClickTime = 0L;

    public GuiScrollList(int xPosition, int yPosition, int width, int height) {
        bounds = new Rectangle(this.xPosition = xPosition, this.yPosition = yPosition, this.width = width, this.height = height);
    }

    public int getGripWidth() {
        return 5;
    }

    public int getSingleUnit() {
        return 10;
    }

    public int getMaximumGripSize() {
        return bounds.getHeight();
    }

    public int getMinimumGripSize() {
        return 20;
    }

    public void draw(int mouseX, int mouseY) {
        int contentHeight = getContentHeight();
        contentHeight = contentHeight == 0 ? 1 : contentHeight;

        float ratio = (float) bounds.getHeight() / (float) contentHeight;
        float gripSize = bounds.getHeight() * ratio;
        float trackSize = bounds.getHeight();

        gripSize = gripSize > getMaximumGripSize() ? getMaximumGripSize() : gripSize < getMinimumGripSize() ? getMinimumGripSize() : gripSize;

        float trackScrollAreaSize = trackSize - gripSize;
        float windowScrollAreaSize = getMaxScrollY();
        float windowPositionRatio = scrollY / windowScrollAreaSize;
        float gripPosition = trackScrollAreaSize * windowPositionRatio;

        boolean isHovering = mouseX >= bounds.getX() && mouseX <= bounds.getX() + bounds.getWidth() &&
                mouseY >= bounds.getY() && mouseY <= bounds.getY() + bounds.getHeight();

        float scrollBarLeft = bounds.getX() + bounds.getWidth() - getGripWidth();
        float scrollBarRight = bounds.getX() + bounds.getWidth();

        if (Mouse.isButtonDown(0)) {
            if (this.initialMouseClickY == -1.0F) {
                if (isHovering) {

                    if (mouseX >= scrollBarLeft && mouseX <= scrollBarRight && (mouseY - bounds.getY()) < gripPosition) {
                        scrollY -= getSingleUnit();
                    } else if (mouseX >= scrollBarLeft && mouseX <= scrollBarRight && (mouseY - bounds.getY()) > gripPosition + gripSize) {
                        scrollY += getSingleUnit();
                    } else {
                        int totalHeight = 0;
                        for (int i = 0; i < getSize(); i++) {
                            if (mouseY >= bounds.getY() + totalHeight - scrollY && mouseY <= (bounds.getY() + totalHeight - scrollY) + getObjectHeight(i)) {
                                this.elementClicked(i, i == this.selected && System.currentTimeMillis() - this.lastClickTime < 250L);
                                this.selected = i;
                                this.lastClickTime = System.currentTimeMillis();
                            }
                            totalHeight += getObjectHeight(i);
                        }
                        this.initialMouseClickY = Math.abs((mouseY - bounds.getY()) - gripPosition);
                    }

                }
            } else if (this.initialMouseClickY >= 0.0F) {
                float newGripPosition = (mouseY - bounds.getY()) - initialMouseClickY;
                newGripPosition = newGripPosition < 0 ? 0 : newGripPosition > trackScrollAreaSize ? trackScrollAreaSize : newGripPosition;
                float newGripPositionRatio = newGripPosition / trackScrollAreaSize;
                scrollY = newGripPositionRatio * windowScrollAreaSize;
                gripPosition = newGripPosition;
            }
        } else {
            int scroll = Mouse.getDWheel();
            if (isHovering && scroll != 0) {
                if (scroll > 0) scroll = -1;
                else if (scroll < 0) scroll = 1;
                this.scrollY += (float) (scroll * 10 / 2);
            }

            initialMouseClickY = -1f;
        }

        if (scrollY > windowScrollAreaSize) scrollY = windowScrollAreaSize;
        if (scrollY < 0) scrollY = 0;
        if (Float.isNaN(scrollY)) scrollY = 0;

        GlStateManager.disableTexture2D();
        drawBackground();
        drawScrollbar(scrollBarLeft, scrollBarRight, gripPosition, gripSize);
        GlStateManager.enableTexture2D();

        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        double scaleW = Minecraft.getMinecraft().displayWidth / res.getScaledWidth_double();
        double scaleH = Minecraft.getMinecraft().displayHeight / res.getScaledHeight_double();

        int left = bounds.getX();
        int bottom = bounds.getY() + bounds.getHeight();
        int listWidth = bounds.getWidth();
        int viewHeight = bounds.getHeight();

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((int) (left * scaleW), (int) (mc.displayHeight - (bottom * scaleH)),
                (int) (listWidth * scaleW), (int) (viewHeight * scaleH));

        int totalHeight = 0;
        for (int i = 0; i < getSize(); i++) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(bounds.getX(), (bounds.getY() + totalHeight) - scrollY, 0);

            if (selected == i) {
                drawSelectionBox(i, bounds.getWidth(), getObjectHeight(i));
            }

            drawObject(i);
            GlStateManager.popMatrix();
            totalHeight += getObjectHeight(i);
        }

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public void keyTyped(char keycode, int keynum) {
        float aSingleUnit = 10; //10 unknown units

        if (keynum == Keyboard.KEY_UP) {
            scrollY = scrollY - aSingleUnit;
            if (scrollY < 0)
                scrollY = 0;
        }

        if (keynum == Keyboard.KEY_DOWN) {
            float windowScrollAreaSize = getContentHeight() - bounds.getHeight();

            scrollY = scrollY + aSingleUnit;
            if (scrollY > windowScrollAreaSize)
                scrollY = windowScrollAreaSize;
        }
    }

    public int getSelected() {
        return selected;
    }

    public void setSelected(int selected) {
        this.selected = selected;
    }

    public int getContentHeight() {
        int contentHeight = 0;

        for (int i =0; i < getSize(); i++)
            contentHeight += getObjectHeight(i);

        return contentHeight;
    }

    public void drawBackground() {
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
        GL11.glEnable(GL11.GL_CULL_FACE);
    }

    public void drawScrollbar(float scrollBarLeft, float scrollBarRight, float gripPosition, float gripSize) {
        GL11.glDisable(GL11.GL_CULL_FACE);
        GlStateManager.color(128f / 255f, 128f / 255f, 128f / 255f, 1f);

        GL11.glBegin(GL11.GL_QUADS);

        {
            GL11.glVertex2f(scrollBarLeft, bounds.getY() + gripPosition);
            GL11.glVertex2f(scrollBarRight, bounds.getY() + gripPosition);
            GL11.glVertex2f(scrollBarRight, bounds.getY() + gripPosition + gripSize);
            GL11.glVertex2f(scrollBarLeft, bounds.getY() + gripPosition + gripSize);
        }
        GL11.glEnd();
        GL11.glEnable(GL11.GL_CULL_FACE);
    }

    public void drawSelectionBox(int index, int width, int height) {
        GlStateManager.disableTexture2D();
        GL11.glDisable(GL11.GL_CULL_FACE);

        GlStateManager.color(128f / 255f, 128f / 255f, 128f / 255f, 1f);

        GL11.glBegin(GL11.GL_QUADS);

        {
            GL11.glVertex2f(0, 0);
            GL11.glVertex2f(width, 0);
            GL11.glVertex2f(width, height);
            GL11.glVertex2f(0, height);
        }
        GL11.glEnd();

        GlStateManager.color(1, 1, 1, 1);

        GlStateManager.enableTexture2D();
        GL11.glEnable(GL11.GL_CULL_FACE);
    }

    public abstract int getObjectHeight(int index);

    public abstract void drawObject(int index);

    public abstract int getSize();

    public abstract void elementClicked(int index, boolean doubleClick);

    public float getScrollY() {
        return scrollY;
    }

    public void setScrollY(float scrollY) {
        float windowScrollAreaSize = getMaxScrollY();
        this.scrollY = scrollY < 0 ? 0 : scrollY > windowScrollAreaSize ? windowScrollAreaSize : scrollY;
    }

    public float getMaxScrollY() {
        return getContentHeight() - bounds.getHeight();
    }
}
