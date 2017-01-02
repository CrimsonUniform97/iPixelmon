package com.ipixelmon.tablet.app.pixelbay.gui;

import net.minecraft.item.ItemStack;

/**
 * Created by colby on 1/1/2017.
 */
public class SellGuiItem extends SellGui {

    private ItemStack itemStack;

    public SellGuiItem(Object[] objects) {
        super(objects);
        itemStack = (ItemStack) objects[0];
    }

    @Override
    public void drawObject() {

    }
}
