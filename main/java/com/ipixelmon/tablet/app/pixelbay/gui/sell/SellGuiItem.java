package com.ipixelmon.tablet.app.pixelbay.gui.sell;

import com.ipixelmon.util.ItemUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

/**
 * Created by colby on 1/1/2017.
 */
public class SellGuiItem extends SellGui {

    private ItemStack itemStack;

    public SellGuiItem(Object[] objects) {
        super(objects);
        itemStack = (ItemStack) objects[0];
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
        if (keyCode == Keyboard.KEY_ESCAPE) super.keyTyped(typedChar, keyCode);

        if (keyCode != Keyboard.KEY_BACK && keyCode != Keyboard.KEY_LEFT && keyCode != Keyboard.KEY_RIGHT) {
            if (!isInt("" + typedChar)) return;
            if (!isInt(amountField.getText() + typedChar)) return;
            if (!isInt(priceField.getText() + typedChar)) return;

            if (amountField.isFocused()) {

                int amount = Integer.parseInt(amountField.getText() + typedChar);
                if (amount > itemStack.stackSize || amount <= 0) return;
            }

            if (priceField.isFocused()) {

                try {
                    Integer.parseInt("" + typedChar);
                } catch (NumberFormatException e) {
                    return;
                }


                int amount = Integer.parseInt(priceField.getText() + typedChar);
                if (amount <= 0) return;
            }
        }
        priceField.textboxKeyTyped(typedChar, keyCode);
        amountField.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    public void init() {

    }

    private boolean isInt(String str) {
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

}
