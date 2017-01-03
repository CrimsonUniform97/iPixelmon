package com.ipixelmon.tablet.app.pixelbay;

import com.ipixelmon.tablet.AppGui;
import com.ipixelmon.tablet.app.pixelbay.gui.buy.BuyGui;
import net.minecraft.client.Minecraft;

/**
 * Created by colby on 12/31/2016.
 */
public class PixelbayGui extends AppGui {

    public PixelbayGui(Object[] objects) {
        super(objects);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, int dWheel, float partialTicks) {
        Minecraft.getMinecraft().displayGuiScreen(new BuyGui(null));
    }


}
