package com.ipixelmon.tablet.client.apps.pixelbay;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.pixelbay.gui.buy.GuiSearch;
import com.ipixelmon.tablet.client.App;
import net.minecraft.client.Minecraft;

/**
 * Created by colby on 12/13/2016.
 */
public class Pixelbay extends App {

    public Pixelbay(String name) {
        super(name, true);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
//        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void initGui() {
        super.initGui();
        Minecraft.getMinecraft().thePlayer.openGui(iPixelmon.instance, GuiSearch.ID, null, 0, 0, 0);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

}
