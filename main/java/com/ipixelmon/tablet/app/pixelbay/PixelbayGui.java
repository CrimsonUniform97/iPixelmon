package com.ipixelmon.tablet.app.pixelbay;

import com.google.common.collect.Lists;
import com.ipixelmon.tablet.AppGui;
import com.ipixelmon.tablet.app.pixelbay.lists.GuiInventoryList;
import com.ipixelmon.tablet.app.pixelbay.lists.GuiPixelmonList;
import net.minecraft.client.gui.GuiButton;

import java.io.IOException;
import java.util.List;

/**
 * Created by colby on 12/31/2016.
 */
public class PixelbayGui extends AppGui {

    public static List<ItemListing> itemListings = Lists.newArrayList();
    private GuiInventoryList inventoryList;
    private GuiPixelmonList pixelmonList;

    public PixelbayGui(Object[] objects) {
        super(objects);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, int dWheel) {
        inventoryList.draw(mouseX, mouseY, dWheel);
        pixelmonList.draw(mouseX, mouseY, dWheel);
        drawHowTo();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        inventoryList.keyTyped(typedChar, keyCode);
        pixelmonList.keyTyped(typedChar, keyCode);
    }
    // TODO: Test
    @Override
    public void initGui() {
        super.initGui();

        int listWidth = screenBounds.getWidth() - 20;
        int listHeight = screenBounds.getHeight() - 30;
        int xPos = screenBounds.getX() + (screenBounds.getWidth() - listWidth) / 2;
        int yPos = screenBounds.getY() + (screenBounds.getHeight() - listHeight) / 2;
        listWidth /= 2;

        inventoryList = new GuiInventoryList(xPos - 5, yPos + 10, listWidth, listHeight);
        pixelmonList = new GuiPixelmonList(xPos + listWidth + 5, yPos + 10, listWidth, listHeight);
    }

    private void drawHowTo() {
        String message = "Double-Click selection to sell.";
    }


}
