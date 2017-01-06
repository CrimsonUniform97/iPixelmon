package com.ipixelmon.tablet.app.pixelbay.gui.sell;

import com.ipixelmon.tablet.AppGui;
import com.ipixelmon.tablet.app.pixelbay.gui.buy.BuyGui;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

/**
 * Created by colby on 1/1/2017.
 */
public abstract class SellGuiPopup extends AppGui {

    public SellGuiPopup(Object[] objects) {
        super(objects);
    }

    protected GuiTextField priceField, amountField;
    protected float rotY;

    @Override
    public final void drawScreen(int mouseX, int mouseY, int dWheel, float partialTicks) {
        GlStateManager.color(1f, 1f, 1f, 1f);
        GlStateManager.enableBlend();
        drawObject(mouseX, mouseY, partialTicks);

        GlStateManager.color(1f, 1f, 1f, 1f);
        amountField.drawTextBox();
        priceField.drawTextBox();
    }

    @Override
    protected final void actionPerformed(GuiButton button) throws IOException {
        doSale();
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        if(keyCode == Keyboard.KEY_ESCAPE) {
            Minecraft.getMinecraft().displayGuiScreen(new SellGui(null));
            return;
        }

        super.keyTyped(typedChar, keyCode);
        if(keyCode != Keyboard.KEY_BACK && keyCode != Keyboard.KEY_LEFT && keyCode != Keyboard.KEY_RIGHT) {
            if (!isInt("" + typedChar)) return;

            if (priceField.isFocused()) {
                if (!isInt(priceField.getText() + typedChar)) return;
                int amount = Integer.parseInt(priceField.getText() + typedChar);
                if (amount <= 0) return;
            } else if (amountField.isFocused()) {
                if (!isInt(amountField.getText() + typedChar)) return;
            }
        }
        priceField.textboxKeyTyped(typedChar, keyCode);
        amountField.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        priceField.mouseClicked(mouseX, mouseY, mouseButton);
        amountField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public final void initGui() {
        super.initGui();
        this.buttonList.clear();

        int width = 100;
        priceField = new GuiTextField(0, mc.fontRendererObj,
                (this.width - width) / 2,
                screenBounds.getY() + screenBounds.getHeight() - 45,
                100 - 20,
                20);
        amountField = new GuiTextField(1, mc.fontRendererObj,
                priceField.xPosition + priceField.width,
                priceField.yPosition,
                20,
                20);

        this.buttonList.add(new GuiButton(2, (this.width - 50) / 2, priceField.yPosition + priceField.height + 3,
                50, 20, "Sale"));

        init();
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        rotY = rotY >= 360.0F ? 0.0F : rotY + 4.0F;
        this.buttonList.get(0).enabled = !priceField.getText().isEmpty() && !amountField.getText().isEmpty();
        priceField.updateCursorCounter();
        amountField.updateCursorCounter();
    }

    public abstract void drawObject(int mouseX, int mouseY, float partialTicks);

    public abstract void init();

    public abstract void doSale();

    protected boolean isInt(String str) {
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

}
