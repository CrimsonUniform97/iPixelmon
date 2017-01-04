package com.ipixelmon.tablet.app.pixelbay.lists.buy;

import com.ipixelmon.tablet.app.pixelbay.ItemListing;
import com.ipixelmon.tablet.app.pixelbay.PixelbayAPI;
import com.ipixelmon.tablet.app.pixelbay.gui.buy.BuyGuiItem;
import com.ipixelmon.tablet.app.pixelbay.lists.IScrollListWithDesign;
import com.ipixelmon.util.ItemUtil;
import com.ipixelmon.util.PixelmonAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;

/**
 * Created by colby on 12/31/2016.
 */
public class GuiItemListingList extends IScrollListWithDesign {

    public GuiItemListingList(int xPosition, int yPosition, int width, int height) {
        super(xPosition, yPosition, width, height);
    }

    @Override
    public int getObjectHeight(int index) {
        return 30;
    }

    @Override
    public void drawObject(int index, int mouseX, int mouseY, boolean isHovering) {
        ItemListing itemListing = PixelbayAPI.Client.itemListings.get(index);
        ItemUtil.Client.renderItem(itemListing.getItem(), 4, (30 - 16) / 2, this.width, this.height, mouseX, mouseY);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);

        mc.fontRendererObj.drawString("Seller: " + itemListing.getPlayerName(), 4 + 10 + 16, 4, 0xFFFFFF);

        PixelmonAPI.Client.renderPokeDollar(mc, 4 + 16 + 16 + 24, getObjectHeight(index) - 13, 1, 0xFFFFFF);

        boolean hasEnough = PixelmonAPI.Client.getBalance() >= itemListing.getPrice();

        mc.fontRendererObj.drawString("Price:   "
                        + (hasEnough ? EnumChatFormatting.GREEN : EnumChatFormatting.RED) + itemListing.getPrice(),
                4 + 10 + 16, getObjectHeight(index) - 12, 0xFFFFFF);
    }

    @Override
    public int getSize() {
        return PixelbayAPI.Client.itemListings.size();
    }

    @Override
    public void elementClicked(int index, int mouseX, int mouseY, boolean doubleClick) {
        if(doubleClick) {
            Minecraft.getMinecraft().displayGuiScreen(new BuyGuiItem(new Object[]{PixelbayAPI.Client.itemListings.get(index)}));
        }
    }

}
