package com.ipixelmon.tablet.app.pixelbay.gui.sell;

import com.ipixelmon.util.PixelmonAPI;
import com.pixelmonmod.pixelmon.comm.PixelmonData;

/**
 * Created by colby on 1/1/2017.
 */
public class SellGuiPixelmon extends SellGui {

    private PixelmonData pixelmonData;
    private PixelmonAPI.Client.PixelmonRenderer pixelmonRenderer;

    public SellGuiPixelmon(Object[] objects) {
        super(objects);
        pixelmonData = (PixelmonData) objects[0];
        pixelmonRenderer = PixelmonAPI.Client.renderPixelmon3D(pixelmonData, true, this);
        new Thread(pixelmonRenderer).start();
    }

    @Override
    public void drawObject(int mouseX, int mouseY, float partialTicks) {
        pixelmonRenderer.render(screenBounds.getX() + (screenBounds.getWidth() / 2) - 5, priceField.yPosition - 2);
    }

    @Override
    public void init() {
        // TODO: Remove amount field
    }

}
