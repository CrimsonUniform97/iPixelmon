package com.ipixelmon.tablet.app.pixelbay.gui.sell;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.app.pixelbay.packet.sell.PacketSellItem;
import com.ipixelmon.util.ItemUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

/**
 * Created by colby on 1/1/2017.
 */
public class SellGuiItem extends SellGuiPopup {

    private ItemStack itemStack;
    private int slot;

    public SellGuiItem(Object[] objects) {
        super(objects);
        itemStack = (ItemStack) objects[0];
        slot = (int) objects[1];
    }

    @Override
    public void drawObject(int mouseX, int mouseY, float partialTicks) {
        ItemUtil.Client.renderItem3D(itemStack, screenBounds.getX() + (screenBounds.getWidth() / 2),
                screenBounds.getY() + 70, 80, rotY);

        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableBlend();
        ItemUtil.Client.renderToolTip(itemStack, screenBounds.getX() - 7, screenBounds.getY() + 17, screenBounds.getWidth(), screenBounds.getHeight());
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        if (keyCode != Keyboard.KEY_BACK && keyCode != Keyboard.KEY_LEFT && keyCode != Keyboard.KEY_RIGHT) {

            if (amountField.isFocused()) {
                if (!isInt("" + typedChar)) return;
                int amount = Integer.parseInt(amountField.getText());
                if (amount > itemStack.stackSize || amount <= 0) {
                    amountField.setText(amountField.getText().substring(0, amountField.getText().length() - 1));
                }
            }
        }
    }

    @Override
    public void init() {

    }

    @Override
    public void doSale() {
        iPixelmon.network.sendToServer(new PacketSellItem(slot, Integer.parseInt(priceField.getText()),
                Integer.parseInt(amountField.getText())));
    }

}
