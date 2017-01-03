package com.ipixelmon.tablet.app.pixelbay.gui.buy;

import com.ipixelmon.tablet.AppGui;

/**
 * Created by colby on 1/2/2017.
 */
public abstract class BuyGuiPopup extends AppGui {

    protected float rotY;

    public BuyGuiPopup(Object[] objects) {
        super(objects);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, int dWheel, float partialTicks) {

    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();

    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        rotY = rotY >= 360.0F ? 0.0F : rotY + 4.0F;
    }

    public abstract void doPurchase();
}
