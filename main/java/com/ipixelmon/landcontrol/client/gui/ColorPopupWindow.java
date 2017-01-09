package com.ipixelmon.landcontrol.client.gui;

import net.minecraft.client.gui.Gui;
import net.minecraft.util.EnumChatFormatting;

/**
 * Created by colbymchenry on 1/9/17.
 */
public class ColorPopupWindow extends Gui {

    private int xPosition, yPosition;
    private EnumChatFormatting selectedColor;

    public void draw(int xPosition, int yPosition) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;

        // TODO:
    }

    public EnumChatFormatting getSelectedColor() {
        return selectedColor;
    }



}
