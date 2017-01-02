package com.ipixelmon.tablet.app.pixelbay.gui.sell;

import com.ipixelmon.tablet.AppGui;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

/**
 * Created by colby on 1/1/2017.
 */
public abstract class SellGui extends AppGui {

    public SellGui(Object[] objects) {
        super(objects);
    }

    protected GuiTextField priceField, amountField;
    protected float rotY;

    @Override
    public final void drawScreen(int mouseX, int mouseY, int dWheel, float partialTicks) {
        // TODO: Draw a textbox to enter the price and then place the position for drawObject with GlTranslate
        GlStateManager.color(1f, 1f, 1f, 1f);
        GlStateManager.enableBlend();
        drawObject(mouseX, mouseY, partialTicks);

        GlStateManager.color(1f, 1f, 1f, 1f);
        priceField.drawTextBox();
        amountField.drawTextBox();
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if(keyCode != Keyboard.KEY_BACK && keyCode != Keyboard.KEY_LEFT && keyCode != Keyboard.KEY_RIGHT) {
            try {
                Integer.parseInt("" + typedChar);
            } catch (NumberFormatException e) {
                return;
            }
        }
        priceField.textboxKeyTyped(typedChar, keyCode);
        amountField.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    public final void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        priceField.mouseClicked(mouseX, mouseY, mouseButton);
        amountField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public final void initGui() {
        super.initGui();
        int width = 100;
        priceField = new GuiTextField(0, mc.fontRendererObj,
                (this.width - width) / 2,
                screenBounds.getY() + screenBounds.getHeight() - 40,
                100 - 20,
                20);
        amountField = new GuiTextField(1, mc.fontRendererObj,
                priceField.xPosition + priceField.width,
                priceField.yPosition,
                20,
                20);
        init();
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        rotY = rotY >= 360.0F ? 0.0F : rotY + 4.0F;
        priceField.updateCursorCounter();
        amountField.updateCursorCounter();
    }

    public abstract void drawObject(int mouseX, int mouseY, float partialTicks);

    public abstract void init();

}
