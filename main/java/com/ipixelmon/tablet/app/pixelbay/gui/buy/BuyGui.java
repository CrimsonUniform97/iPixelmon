package com.ipixelmon.tablet.app.pixelbay.gui.buy;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.AppGui;
import com.ipixelmon.tablet.app.pixelbay.PixelbayAPI;
import com.ipixelmon.tablet.app.pixelbay.gui.sell.SellGui;
import com.ipixelmon.tablet.app.pixelbay.lists.buy.GuiItemListingList;
import com.ipixelmon.tablet.app.pixelbay.lists.buy.GuiPixelmonListingList;
import com.ipixelmon.tablet.app.pixelbay.packet.buy.PacketRequestPage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

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
        if(button.id == 0) {
            Minecraft.getMinecraft().displayGuiScreen(new SellGui(null));
            return;
        }

        if(isItemPageBtn(button)) {
            int page = Integer.parseInt(button.displayString);
            page -=1;
            iPixelmon.network.sendToServer(new PacketRequestPage(page, itemSearchField.getText(), true));
        } else {
            int page = Integer.parseInt(button.displayString);
            page -=1;
            iPixelmon.network.sendToServer(new PacketRequestPage(page, pixelmonSearchField.getText(), false));
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        itemList.keyTyped(typedChar, keyCode);
        pixelmonList.keyTyped(typedChar, keyCode);

        itemSearchField.textboxKeyTyped(typedChar, keyCode);
        pixelmonSearchField.textboxKeyTyped(typedChar, keyCode);

        boolean pressedEnter = keyCode == Keyboard.KEY_RETURN;

        if(pressedEnter) {
            if (itemSearchField.isFocused()) {
                iPixelmon.network.sendToServer(new PacketRequestPage());
            } else if (pixelmonSearchField.isFocused()) {

            }
        }
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

        itemList = new GuiItemListingList(xPos - 5, yPos + 10, listWidth, listHeight - 20);
        pixelmonList = new GuiPixelmonListingList(xPos + listWidth + 5, yPos + 10, listWidth, itemList.height);

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

        if(this.buttonList.size() > 1) return;

        if(PixelbayAPI.Client.maxItemPages <= 0 && PixelbayAPI.Client.maxPixelmonPages <= 0) return;

        int xOffsetItem = itemList.width / PixelbayAPI.Client.maxItemPages;
        int xOffsetPixelmon = pixelmonList.width / PixelbayAPI.Client.maxPixelmonPages;

        for(int i = 0; i < PixelbayAPI.Client.maxItemPages; i++) {
            this.buttonList.add(new PageBtn(this.buttonList.size() + i,
                    itemList.xPosition + (xOffsetItem * i) + (xOffsetItem / 2),
                    itemList.yPosition + itemList.height + 8, String.valueOf(i + 1)));
        }

        for(int i = 0; i < PixelbayAPI.Client.maxPixelmonPages; i++) {
            this.buttonList.add(new PageBtn(this.buttonList.size() + i,
                    pixelmonList.xPosition + (xOffsetPixelmon * i) + (xOffsetPixelmon / 2),
                    pixelmonList.yPosition + pixelmonList.height + 8, String.valueOf(i + 1)));
        }
    }

    private boolean isItemPageBtn(GuiButton button) {
        if(button.xPosition >= itemList.xPosition && button.xPosition <= itemList.xPosition + itemList.width) {
            return true;
        } else {
            return false;
        }
    }
}