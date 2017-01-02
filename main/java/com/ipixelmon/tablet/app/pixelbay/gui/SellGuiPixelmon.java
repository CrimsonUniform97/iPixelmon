package com.ipixelmon.tablet.app.pixelbay.gui;

import com.pixelmonmod.pixelmon.comm.PixelmonData;

/**
 * Created by colby on 1/1/2017.
 */
public class SellGuiPixelmon extends SellGui {

    private PixelmonData pixelmonData;

    public SellGuiPixelmon(Object[] objects) {
        super(objects);
        pixelmonData = (PixelmonData) objects[0];
    }

    @Override
    public void drawObject() {

    }
}
