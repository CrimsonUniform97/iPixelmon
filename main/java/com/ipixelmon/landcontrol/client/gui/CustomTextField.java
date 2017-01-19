package com.ipixelmon.landcontrol.client.gui;

import net.minecraft.client.gui.*;

/**
 * Created by colby on 1/9/2017.
 */
public class CustomTextField extends com.ipixelmon.GuiTextField {

    public CustomTextField(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void drawString(FontRenderer fontRendererIn, String text, int x, int y, int color) {
        super.drawString(fontRendererIn, text, x, y, color);
    }
}