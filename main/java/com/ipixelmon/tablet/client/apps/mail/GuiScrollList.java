package com.ipixelmon.tablet.client.apps.mail;

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
    private Rectangle bounds;
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

    public int getMinimumGripSize(){
        return 20;
    }

    public void draw(int mouseX, int mouseY) {
        int contentHeight = getContentHeight();
        contentHeight = contentHeight == 0 ? 1 : contentHeight;

        float ratio = bounds.getHeight() / contentHeight;
        float gripSize = bounds.getHeight() * ratio;
        float trackSize = bounds.getHeight();

        gripSize = gripSize > getMaximumGripSize() ? getMaximumGripSize() : gripSize < getMinimumGripSize() ? getMinimumGripSize() : gripSize;

        float trackScrollAreaSize = trackSize - gripSize;
        float windowScrollAreaSize = contentHeight - bounds.getHeight();
        float windowPositionRatio = scrollY / windowScrollAreaSize;
        float gripPosition = trackScrollAreaSize * windowPositionRatio;

        boolean isHovering = mouseX >= bounds.getX() && mouseX <= bounds.getX() + bounds.getWidth() &&
                mouseY >= bounds.getY() && mouseY <= bounds.getY() + bounds.getHeight();

        float scrollBarLeft = bounds.getX() + bounds.getWidth() - getGripWidth();
        float scrollBarRight = bounds.getX() + bounds.getWidth();

        if (Mouse.isButtonDown(0)) {
            if (this.initialMouseClickY == -1.0F) {
                if (isHovering) {
                    if (mouseX >= scrollBarLeft && mouseX <= scrollBarRight && mouseY >= gripPosition && mouseY <= gripPosition + gripSize) {
                        this.initialMouseClickY = (mouseY - bounds.getY()) - gripPosition;
                    } else if (mouseX >= scrollBarLeft && mouseX <= scrollBarRight && mouseY < gripPosition) {
                        scrollY = scrollY - getSingleUnit();
                    } else if (mouseX >= scrollBarLeft && mouseX <= scrollBarRight && mouseY > gripPosition) {
                        scrollY = scrollY + getSingleUnit();
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
                    }
                    this.initialMouseClickY = (mouseY - bounds.getY()) - gripPosition;
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

        scrollY = scrollY < 0 ? 0 : scrollY > windowScrollAreaSize ? windowScrollAreaSize : scrollY;

        GlStateManager.disableTexture2D();
        GL11.glDisable(GL11.GL_CULL_FACE);
        drawBackground();
        drawScrollbar(scrollBarLeft, scrollBarRight, gripPosition, gripSize);
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

            if(selected == i) {
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

    private int getContentHeight() {
        int contentHeight = 0;

        for (int i = getSize(); i > 0; i--)
            contentHeight += getObjectHeight(i);

        return contentHeight;
    }

    public void drawBackground() {
        GlStateManager.color(0f, 0f, 0f, 1f);
        GL11.glBegin(GL11.GL_QUADS);

        {
            GL11.glVertex2f(bounds.getX(), bounds.getY());
            GL11.glVertex2f(bounds.getX() + bounds.getWidth(), bounds.getY());
            GL11.glVertex2f(bounds.getX() + bounds.getWidth(), bounds.getY() + bounds.getHeight());
            GL11.glVertex2f(bounds.getX(), bounds.getY() + bounds.getHeight());
        }
        GL11.glEnd();
    }

    public void drawScrollbar(float scrollBarLeft, float scrollBarRight, float gripPosition, float gripSize) {
        GlStateManager.color(128f / 255f, 128f / 255f, 128f / 255f, 1f);

        GL11.glBegin(GL11.GL_QUADS);

        {
            GL11.glVertex2f(scrollBarLeft, bounds.getY() + gripPosition);
            GL11.glVertex2f(scrollBarRight, bounds.getY() + gripPosition);
            GL11.glVertex2f(scrollBarRight, bounds.getY() + gripPosition + gripSize);
            GL11.glVertex2f(scrollBarLeft, bounds.getY() + gripPosition + gripSize);
        }
        GL11.glEnd();
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

}
