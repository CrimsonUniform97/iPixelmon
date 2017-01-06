package com.ipixelmon.tablet.app.pixelbay.gui.sell;

import com.ipixelmon.tablet.AppGui;
import com.ipixelmon.tablet.app.pixelbay.gui.buy.BuyGui;
import com.ipixelmon.tablet.app.pixelbay.lists.buy.GuiItemListingList;
import com.ipixelmon.tablet.app.pixelbay.lists.buy.GuiPixelmonListingList;
import com.ipixelmon.tablet.app.pixelbay.lists.sell.GuiInventoryList;
import com.ipixelmon.tablet.app.pixelbay.lists.sell.GuiPixelmonList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

/**
 * Created by colby on 1/3/2017.
 */
public class SellGui extends AppGui {

    private GuiInventoryList inventoryList;
    private GuiPixelmonList pixelmonList;

    public SellGui(Object[] objects) {
        super(objects);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, int dWheel, float partialTicks) {
        inventoryList.draw(mouseX, mouseY, dWheel);
        pixelmonList.draw(mouseX, mouseY, dWheel);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        Minecraft.getMinecraft().displayGuiScreen(new BuyGui(null));
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if(keyCode == Keyboard.KEY_ESCAPE) {
            Minecraft.getMinecraft().displayGuiScreen(new BuyGui(null));
            return;
        }

        super.keyTyped(typedChar, keyCode);
        inventoryList.keyTyped(typedChar, keyCode);
        pixelmonList.keyTyped(typedChar, keyCode);
    }

    @Override
    public void initGui() {
        super.initGui();

        this.buttonList.clear();

        int listWidth = screenBounds.getWidth() - 20;
        int listHeight = screenBounds.getHeight() - 30;
        int xPos = screenBounds.getX() + (screenBounds.getWidth() - listWidth) / 2;
        int yPos = screenBounds.getY() + (screenBounds.getHeight() - listHeight) / 2;
        yPos += 5;
        listWidth /= 2;

        inventoryList = new GuiInventoryList(xPos - 5, yPos + 10, listWidth, listHeight - 5);
        pixelmonList = new GuiPixelmonList(xPos + listWidth + 5, yPos + 10, listWidth, listHeight - 5);

        this.buttonList.add(new GuiButton(0, (this.width - 50) / 2,
                pixelmonList.yPosition - 27, 50, 20, "Buy"));
    }

}
