package com.ipixelmon.tablet.app.pixelbay.gui.buy;

import com.ipixelmon.tablet.AppGui;
import net.minecraft.client.gui.GuiButton;

import java.io.IOException;

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
        drawObject(mouseX, mouseY, dWheel, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        doPurchase();
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();
        int btnWidth = mc.fontRendererObj.getStringWidth("Buy") + 10;
        this.buttonList.add(new GuiButton(0,
                (this.width - btnWidth) / 2,
                screenBounds.getY() + screenBounds.getHeight() - 30,
                btnWidth, 20, "Buy"));
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        rotY = rotY >= 360.0F ? 0.0F : rotY + 4.0F;
    }

    public abstract void drawObject(int mouseX, int mouseY, int dWheel, float partialTicks);
    public abstract void doPurchase();
}
