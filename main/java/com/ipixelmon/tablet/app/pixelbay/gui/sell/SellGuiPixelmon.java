package com.ipixelmon.tablet.app.pixelbay.gui.sell;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.app.pixelbay.packet.sell.PacketSellPixelmon;
import com.ipixelmon.util.PixelmonAPI;
import com.pixelmonmod.pixelmon.client.render.RenderPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.client.renderer.GlStateManager;

import java.io.IOException;

/**
 * Created by colby on 1/1/2017.
 */
public class SellGuiPixelmon extends SellGuiPopup {

    private EntityPixelmon pixelmon;
    private float pixelmonDisplayRotY = 0.0F;

    public SellGuiPixelmon(Object[] objects) {
        super(objects);
        pixelmon = (EntityPixelmon) objects[0];
    }

    @Override
    public void drawObject(int mouseX, int mouseY, float partialTicks) {
        this.amountField.setText("1");

        PixelmonAPI.Client.renderPixelmon3D(pixelmon,screenBounds.getX() + (screenBounds.getWidth() / 2) - 5,
                priceField.yPosition - 2, 40.0F, pixelmonDisplayRotY += 0.66F, this);
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
