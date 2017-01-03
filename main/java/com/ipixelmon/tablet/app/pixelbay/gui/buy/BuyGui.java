package com.ipixelmon.tablet.app.pixelbay.gui.buy;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.AppGui;
import com.ipixelmon.tablet.app.pixelbay.gui.sell.SellGui;
import com.ipixelmon.tablet.app.pixelbay.lists.buy.GuiItemListingList;
import com.ipixelmon.tablet.app.pixelbay.lists.buy.GuiPixelmonListingList;
import com.ipixelmon.tablet.app.pixelbay.packet.buy.PacketRequestPage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;

import java.io.IOException;

/**
 * Created by colby on 1/3/2017.
 */
public class BuyGui extends AppGui {

    private GuiItemListingList itemList;
    private GuiPixelmonListingList pixelmonList;

    private GuiTextField itemSearchField, pixelmonSearchField;

    public BuyGui(Object[] objects) {
        super(objects);
        iPixelmon.network.sendToServer(new PacketRequestPage(0, "", true));
        iPixelmon.network.sendToServer(new PacketRequestPage(0, "", false));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, int dWheel, float partialTicks) {
        itemList.draw(mouseX, mouseY, dWheel);
        pixelmonList.draw(mouseX, mouseY, dWheel);

        itemSearchField.drawTextBox();
        pixelmonSearchField.drawTextBox();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        itemSearchField.mouseClicked(mouseX, mouseY, mouseButton);
        pixelmonSearchField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        Minecraft.getMinecraft().displayGuiScreen(new SellGui(null));
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        itemList.keyTyped(typedChar, keyCode);
        pixelmonList.keyTyped(typedChar, keyCode);

        itemSearchField.textboxKeyTyped(typedChar, keyCode);
        pixelmonSearchField.textboxKeyTyped(typedChar, keyCode);
    }

    // TODO: Work on popups
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

        itemList = new GuiItemListingList(xPos - 5, yPos + 10, listWidth, listHeight - 5);
        pixelmonList = new GuiPixelmonListingList(xPos + listWidth + 5, yPos + 10, listWidth, listHeight - 5);

        itemSearchField = new GuiTextField(0, mc.fontRendererObj, itemList.xPosition + (itemList.width - 100) / 2,
                itemList.yPosition - 27, 100, 20);
        pixelmonSearchField = new GuiTextField(1, mc.fontRendererObj, pixelmonList.xPosition + (pixelmonList.width - 100) / 2,
                pixelmonList.yPosition - 27, 100, 20);

        this.buttonList.add(new GuiButton(0, (this.width - 50) / 2,
                pixelmonList.yPosition - 27, 50, 20, "Sale"));
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        itemSearchField.updateCursorCounter();
        pixelmonSearchField.updateCursorCounter();
    }
}