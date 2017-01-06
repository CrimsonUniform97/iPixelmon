package com.ipixelmon.tablet.app.pixelbay.gui.buy;

import com.google.common.collect.Lists;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.app.pixelbay.ItemListing;
import com.ipixelmon.tablet.app.pixelbay.packet.buy.PacketPurchaseRequest;
import com.ipixelmon.util.GuiUtil;
import com.ipixelmon.util.ItemUtil;
import com.ipixelmon.util.PixelmonAPI;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;

/**
 * Created by colby on 1/2/2017.
 */
public class BuyGuiItem extends BuyGuiPopup {

    private ItemListing itemListing;
    private List<String> details = Lists.newArrayList();

    public BuyGuiItem(Object[] objects) {
        super(objects);
        this.itemListing = (ItemListing) objects[0];
        details.add(EnumChatFormatting.YELLOW + "Seller: " + itemListing.getPlayerName());

        boolean hasEnough = PixelmonAPI.Client.getBalance() >= itemListing.getPrice();

        details.add((hasEnough ? EnumChatFormatting.GREEN : EnumChatFormatting.RED) + "Price: " + String.valueOf(itemListing.getPrice()));
    }

    @Override
    public void drawObject(int mouseX, int mouseY, int dWheel, float partialTicks) {
        ItemUtil.Client.renderItem3D(itemListing.getItem(), screenBounds.getX() + (screenBounds.getWidth() / 2),
                screenBounds.getY() + 90, 80, rotY);

        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableBlend();
        int[] bounds = ItemUtil.Client.renderToolTip(itemListing.getItem(), screenBounds.getX() - 7, screenBounds.getY() + 17,
                screenBounds.getWidth(), screenBounds.getHeight());

        int height = bounds[1];

        GuiUtil.drawHoveringText(details, screenBounds.getX() - 7, screenBounds.getY() + 17 + height + 10,
                screenBounds.getWidth(), screenBounds.getHeight());

        int stringWidth = mc.fontRendererObj.getStringWidth(String.valueOf(itemListing.getItem().stackSize));
        GlStateManager.pushMatrix();
        GlStateManager.translate(screenBounds.getX() + (screenBounds.getWidth() / 2) + 40, screenBounds.getY() + 130, 0);
        GlStateManager.translate(stringWidth / 2, mc.fontRendererObj.FONT_HEIGHT / 2, 0);
        GlStateManager.scale(2F, 2F, 2F);
        GlStateManager.translate(-(stringWidth / 2), -(mc.fontRendererObj.FONT_HEIGHT / 2), 0);
        mc.fontRendererObj.drawString(String.valueOf(itemListing.getItem().stackSize), 0, 0, 0xFFFFFF, true);
        GlStateManager.popMatrix();
    }


    @Override
    public void doPurchase() {
        iPixelmon.network.sendToServer(new PacketPurchaseRequest(itemListing));
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        this.buttonList.get(0).enabled = PixelmonAPI.Client.getBalance() >= itemListing.getPrice();
    }
}
