package com.ipixelmon.tablet.app.pixelbay.gui;

import com.ipixelmon.tablet.AppGui;

/**
 * Created by colby on 1/1/2017.
 */
public abstract class SellGui extends AppGui {

    public SellGui(Object[] objects) {
        super(objects);
    }

    @Override
    public final void drawScreen(int mouseX, int mouseY, int dWheel) {
        // TODO: Draw a textbox to enter the price and then place the position for drawObject with GlTranslate
    }

    public abstract void drawObject();

}
