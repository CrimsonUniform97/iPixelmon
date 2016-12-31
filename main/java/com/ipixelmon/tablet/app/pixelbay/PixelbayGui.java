package com.ipixelmon.tablet.app.pixelbay;

import com.google.common.collect.Lists;
import com.ipixelmon.tablet.AppGui;
import net.minecraft.client.gui.GuiButton;

import java.io.IOException;
import java.util.List;

/**
 * Created by colby on 12/31/2016.
 */
public class PixelbayGui extends AppGui {

    public static List<ItemListing> itemListings = Lists.newArrayList();

    public PixelbayGui(Object[] objects) {
        super(objects);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, int dWheel) {

    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
    }

    @Override
    public void initGui() {
        super.initGui();
    }


}
