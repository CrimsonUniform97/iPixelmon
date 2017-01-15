package com.ipixelmon.tablet.app.pixelbay.gui.sell;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.app.pixelbay.packet.sell.PacketSellPixelmon;
import com.ipixelmon.util.PixelmonAPI;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;

import java.io.IOException;

/**
 * Created by colby on 1/1/2017.
 */
public class SellGuiPixelmon extends SellGuiPopup {

    private EntityPixelmon pixelmon;
    private PixelmonAPI.Client.PixelmonRenderer pixelmonRenderer;

    public SellGuiPixelmon(Object[] objects) {
        super(objects);
        pixelmon = (EntityPixelmon) objects[0];
        pixelmonRenderer = PixelmonAPI.Client.renderPixelmon3D(pixelmon, true, this);
        new Thread(pixelmonRenderer).start();
    }

    @Override
    public void drawObject(int mouseX, int mouseY, float partialTicks) {
        this.amountField.setText("1");
        pixelmonRenderer.render(screenBounds.getX() + (screenBounds.getWidth() / 2) - 5, priceField.yPosition - 2, 50);
        PixelmonAPI.Client.renderPixelmonTip(pixelmon, screenBounds.getX() - 7, screenBounds.getY() + 17, this.width, this.height);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        this.priceField.mouseClicked(mouseX, mouseY, mouseButton);
        if(this.buttonList.get(0).mousePressed(mc, mouseX, mouseY))
            super.actionPerformed(this.buttonList.get(0));
    }

    @Override
    public void init() {
        this.amountField.setText("1");
        this.amountField.setEnabled(false);
        this.priceField.width += this.amountField.width;
    }

    @Override
    public void doSale() {
        iPixelmon.network.sendToServer(new PacketSellPixelmon(pixelmon.getPokemonId(), Integer.parseInt(priceField.getText())));
    }

}
