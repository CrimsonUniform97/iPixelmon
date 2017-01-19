package com.ipixelmon.tablet.app.pixelbay.gui.buy;

import com.google.common.collect.Lists;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.app.pixelbay.PixelmonListing;
import com.ipixelmon.tablet.app.pixelbay.packet.buy.PacketPurchaseRequest;
import com.ipixelmon.util.GuiUtil;
import com.ipixelmon.util.PixelmonAPI;
import net.minecraft.util.text.TextFormatting;

import java.util.List;

/**
 * Created by colby on 1/2/2017.
 */
public class BuyGuiPixelmon extends BuyGuiPopup {

    private PixelmonListing pixelmonListing;
    private float pixelmonDisplayRotY = 0.0F;
    private List<String> details = Lists.newArrayList();

    public BuyGuiPixelmon(Object[] objects) {
        super(objects);
        this.pixelmonListing = (PixelmonListing) objects[0];
        details.add(TextFormatting.YELLOW + "Seller: " + pixelmonListing.getPlayerName());
        boolean hasEnough = PixelmonAPI.Client.getBalance() >= pixelmonListing.getPrice();
        details.add((hasEnough ? TextFormatting.GREEN : TextFormatting.RED) + "Price: " + String.valueOf(pixelmonListing.getPrice()));
    }

    @Override
    public void drawObject(int mouseX, int mouseY, int dWheel, float partialTicks) {

        PixelmonAPI.Client.renderPixelmon3D(pixelmonListing.getPixelmon(),
                screenBounds.getX() + (screenBounds.getWidth() / 2) - 5, screenBounds.getY() + 150, 400, 40.0F,
                pixelmonDisplayRotY += 0.66F);


        int[] bounds = PixelmonAPI.Client.renderPixelmonTip(pixelmonListing.getPixelmon(), screenBounds.getX() - 7,
                screenBounds.getY() + 17, this.width, this.height);

        int width = bounds[0];
        int height = bounds[1];

        GuiUtil.drawHoveringText(details, screenBounds.getX() - 7, screenBounds.getY() + 17 + height + 10,
                screenBounds.getWidth(), screenBounds.getHeight());
    }

    @Override
    public void doPurchase() {
        iPixelmon.network.sendToServer(new PacketPurchaseRequest(pixelmonListing));
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        this.buttonList.get(0).enabled = PixelmonAPI.Client.getBalance() >= pixelmonListing.getPrice();
    }
}
