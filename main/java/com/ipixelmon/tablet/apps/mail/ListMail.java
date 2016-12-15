package com.ipixelmon.tablet.apps.mail;

import com.ipixelmon.GuiScrollList;

/**
 * Created by colby on 12/14/2016.
 */
public class ListMail extends GuiScrollList {

    public ListMail(int xPosition, int yPosition, int width, int height) {
        super(xPosition, yPosition, width, height);
    }

    @Override
    public int getObjectHeight(int index) {
        return 0;
    }

    @Override
    public void drawObject(int index) {

    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public void elementClicked(int index, boolean doubleClick) {

    }
}
