package com.ipixelmon;

import net.minecraft.client.renderer.GlStateManager;

/**
 * Created by colby on 12/4/2016.
 */
public class GuiScrollingTextField extends GuiScrollList {

    private GuiTextField textField;

    public GuiScrollingTextField(int x, int y, int width, int height) {
        super(x, y, width, height);
        textField = new GuiTextField(x, y, width - 5, height) {
            @Override
            public void drawBackground() {
//                super.drawBackground();
            }
        };
    }

    @Override
    public int getObjectHeight(int index) {
        int totalHeight = mc.fontRendererObj.FONT_HEIGHT * (mc.fontRendererObj.listFormattedStringToWidth(textField.getText(), textField.getBounds().getWidth()).size());

        if (totalHeight >= height) {
            return height + (totalHeight - height);
        } else {
            return height;
        }
    }

    @Override
    public void drawObject(int index, int mouseX, int mouseY, boolean isHovering) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(-xPosition, -yPosition, 0);
        textField.drawTextField();
        GlStateManager.popMatrix();
        textField.setBounds(xPosition, yPosition, width - 5, getObjectHeight(0));
    }

    @Override
    public int getSize() {
        return 1;
    }

    @Override
    public void elementClicked(int index, int mouseX, int mouseY, boolean doubleClick) {

    }

    @Override
    public void drawSelectionBox(int index, int width, int height) {
//        super.drawSelectionBox(index, width, height);
    }

    @Override
    public void drawBackground() {
        drawRect(bounds.getX() - 2, bounds.getY() - 2, bounds.getX() + bounds.getWidth() + 2, bounds.getY() + bounds.getHeight() + 2, -6250336);
        drawRect(bounds.getX() - 1, bounds.getY() - 1, bounds.getX() + bounds.getWidth() + 1, bounds.getY() + bounds.getHeight() + 1, -16777216);
    }

    @Override
    public void keyTyped(char keycode, int keynum) {
        super.keyTyped(keycode, keynum);
        textField.keyTyped(keycode, keynum);

        // move the window view to wherever the cursor is
        int y = textField.getCursorLine() * mc.fontRendererObj.FONT_HEIGHT;
        setScrollY(y);
    }

    public void mouseClicked(int mouseX, int mouseY) {
        textField.setFocused(textField.getBounds().contains(mouseX, mouseY));

        if (mouseY >= yPosition && mouseY <= yPosition + height && mouseX >= textField.getBounds().getX() &&
                mouseX <= textField.getBounds().getX() + textField.getBounds().getWidth()) {
            int y = (mouseY + (int) (getScrollY()));
            textField.mouseClicked(mouseX, y);
        }
    }

    public GuiTextField getTextField() {
        return textField;
    }
}
