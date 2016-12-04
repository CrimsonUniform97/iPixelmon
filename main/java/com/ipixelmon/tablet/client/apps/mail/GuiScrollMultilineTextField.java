package com.ipixelmon.tablet.client.apps.mail;

import com.ipixelmon.GuiMultiLineTextField;
import com.ipixelmon.GuiScrollList;
import net.minecraft.client.renderer.GlStateManager;

/**
 * Created by colby on 12/4/2016.
 */
public class GuiScrollMultilineTextField extends GuiScrollList {

    private GuiMultiLineTextField textField;

    public GuiScrollMultilineTextField(int x, int y, int width, int height) {
        super(x, y, width, height);
        textField = new GuiMultiLineTextField(x, y, width - 5, height){
            @Override
            public void drawBackground() {
//                super.drawBackground();
            }
        };
    }

    @Override
    public int getObjectHeight(int index) {
        int totalHeight = mc.fontRendererObj.FONT_HEIGHT * (mc.fontRendererObj.listFormattedStringToWidth(textField.getText(), textField.getBounds().getWidth()).size());

        if(totalHeight >= height) {
            return height + (totalHeight - height);
        } else {
            return height;
        }
    }

    @Override
    public void drawObject(int index) {
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
    public void elementClicked(int index, boolean doubleClick) {

    }

    @Override
    public void drawSelectionBox(int index, int width, int height) {
//        super.drawSelectionBox(index, width, height);
    }

    @Override
    public void drawBackground() {
        super.drawBackground();
    }

    @Override
    public void keyTyped(char keycode, int keynum) {
        super.keyTyped(keycode, keynum);
        textField.keyTyped(keycode, keynum);
    }

    public void mouseClicked(int mouseX, int mouseY) {
        if(mouseY >= yPosition && mouseY <= yPosition + height && mouseX >= textField.getBounds().getX() &&
                mouseX <= textField.getBounds().getX() + textField.getBounds().getWidth()) {
            int y = (mouseY + (int) (getScrollY()));
            textField.mouseClicked(mouseX, y);
        }
    }

    public GuiMultiLineTextField getTextField() {
        return textField;
    }
}
