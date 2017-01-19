package com.ipixelmon.tablet.app.pixelbay.lists.buy;

import com.ipixelmon.tablet.app.pixelbay.PixelbayAPI;
import com.ipixelmon.tablet.app.pixelbay.PixelmonListing;
import com.ipixelmon.tablet.app.pixelbay.gui.buy.BuyGuiPixelmon;
import com.ipixelmon.tablet.app.pixelbay.lists.IScrollListWithDesign;
import com.ipixelmon.util.PixelmonAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.opengl.GL11;

/**
 * Created by colby on 12/31/2016.
 */
public class GuiPixelmonListingList extends IScrollListWithDesign {

    public GuiPixelmonListingList(int xPosition, int yPosition, int width, int height) {
        super(xPosition, yPosition, width, height);
    }

    @Override
    public int getObjectHeight(int index) {
        return 30;
    }

    @Override
    public void drawObject(int index, int mouseX, int mouseY, boolean isHovering) {
        PixelmonListing pixelmonListing = PixelbayAPI.Client.pixelmonListings.get(index);
        int x = 2;
        int y = 2;
        int width = 24;
        int height = 24;

        GL11.glEnable(GL11.GL_SCISSOR_TEST);

        mc.fontRendererObj.drawString("Seller: " + pixelmonListing.getPlayerName(), x + width + 16, 4, 0xFFFFFF);

        PixelmonAPI.Client.renderPokeDollar(mc, x + width + 16 + 30, getObjectHeight(index) - 13, 1, 0xFFFFFF);

        boolean hasEnough = PixelmonAPI.Client.getBalance() >= pixelmonListing.getPrice();

        mc.fontRendererObj.drawString("Price:   "
                        + (hasEnough ? TextFormatting.GREEN : TextFormatting.RED) + pixelmonListing.getPrice(),
                x + width + 16, getObjectHeight(index) - 12, 0xFFFFFF);

        PixelmonAPI.Client.renderPixelmon2D(pixelmonListing.getPixelmon(), x - 5, y - 12, width + 16, height + 16);

        if (mouseX >= x && mouseX <= x + width + 5 && mouseY >= y && mouseY <= y + height) {
            PixelmonAPI.Client.renderPixelmonTip(pixelmonListing.getPixelmon(), mouseX, mouseY, this.width, this.height);
        }
    }

    @Override
    public int getSize() {
        return PixelbayAPI.Client.pixelmonListings.size();
    }

    @Override
    public void elementClicked(int index, int mouseX, int mouseY, boolean doubleClick) {
        if(doubleClick) {
            Minecraft.getMinecraft().displayGuiScreen(new BuyGuiPixelmon(new Object[]{PixelbayAPI.Client.pixelmonListings.get(index)}));
        }
    }
}
