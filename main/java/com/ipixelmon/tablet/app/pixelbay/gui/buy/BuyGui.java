package com.ipixelmon.tablet.app.pixelbay.gui.buy;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.AppGui;
import com.ipixelmon.tablet.app.pixelbay.PixelbayAPI;
import com.ipixelmon.tablet.app.pixelbay.gui.sell.SellGui;
import com.ipixelmon.tablet.app.pixelbay.lists.buy.GuiItemListingList;
import com.ipixelmon.tablet.app.pixelbay.lists.buy.GuiPixelmonListingList;
import com.ipixelmon.tablet.app.pixelbay.packet.buy.PacketRequestPage;
import com.ipixelmon.util.PixelmonAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

/**
 * Created by colby on 1/3/2017.
 */
public class BuyGui extends AppGui {

    private GuiItemListingList itemList;
    private GuiPixelmonListingList pixelmonList;

    private GuiTextField itemSearchField, pixelmonSearchField;
    private GuiButton saleBtn;
    private int activePageItem = 0, activePagePixelmon = 0;

    private final int MAX_PAGES = 4;

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
        saleBtn.drawButton(mc, mouseX, mouseY);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        itemSearchField.mouseClicked(mouseX, mouseY, mouseButton);
        pixelmonSearchField.mouseClicked(mouseX, mouseY, mouseButton);

        if (saleBtn.mousePressed(mc, mouseX, mouseY)) {
            Minecraft.getMinecraft().displayGuiScreen(new SellGui(null));
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if (isItemPageBtn(button)) {
            int page = Integer.parseInt(EnumChatFormatting.getTextWithoutFormattingCodes(button.displayString.replaceAll("\\...", "")));
            page -= 1;
            activePageItem = page;
            iPixelmon.network.sendToServer(new PacketRequestPage(page, itemSearchField.getText(), true));
        } else {
            int page = Integer.parseInt(EnumChatFormatting.getTextWithoutFormattingCodes(button.displayString.replaceAll("\\...", "")));
            page -= 1;
            activePagePixelmon = page;
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

        if (pressedEnter) {
            if (itemSearchField.isFocused()) {
                iPixelmon.network.sendToServer(new PacketRequestPage(0, itemSearchField.getText(), true));
            } else if (pixelmonSearchField.isFocused()) {
                iPixelmon.network.sendToServer(new PacketRequestPage(0, pixelmonSearchField.getText(), false));
            }
        }
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

        itemList = new GuiItemListingList(xPos - 5, yPos + 10, listWidth, listHeight - 20);
        pixelmonList = new GuiPixelmonListingList(xPos + listWidth + 5, yPos + 10, listWidth, itemList.height);

        itemSearchField = new GuiTextField(0, mc.fontRendererObj, itemList.xPosition + (itemList.width - 100) / 2,
                itemList.yPosition - 27, 100, 20);
        pixelmonSearchField = new GuiTextField(1, mc.fontRendererObj, pixelmonList.xPosition + (pixelmonList.width - 100) / 2,
                pixelmonList.yPosition - 27, 100, 20);

        saleBtn = new GuiButton(0, (this.width - 50) / 2,
                pixelmonList.yPosition - 27, 50, 20, "Sale");
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        itemSearchField.updateCursorCounter();
        pixelmonSearchField.updateCursorCounter();

        this.buttonList.clear();

        if (PixelbayAPI.Client.maxItemPages <= 0 || PixelbayAPI.Client.maxPixelmonPages <= 0) return;

        int xOffsetItem = itemList.width /
                (PixelbayAPI.Client.maxItemPages < MAX_PAGES ? PixelbayAPI.Client.maxItemPages : MAX_PAGES);
        int xOffsetPixelmon = pixelmonList.width /
                (PixelbayAPI.Client.maxPixelmonPages < MAX_PAGES ? PixelbayAPI.Client.maxPixelmonPages : MAX_PAGES);

        int maxPageItem = getMaximumPage(activePageItem, true);
        int maxPagePixelmon = getMaximumPage(activePagePixelmon, false);
        int minPageItem = getMinimumPage(activePageItem, true);
        int minPagePixelmon = getMinimumPage(activePagePixelmon, false);

        int index = 0;
        for (int page = minPageItem + 1; page < maxPageItem + 1; page++) {
            this.buttonList.add(new PageBtn(this.buttonList.size() + index,
                    itemList.xPosition + (xOffsetItem * index) + (xOffsetItem / 2),
                    itemList.yPosition + itemList.height + 8,
                    ((activePageItem + 1) == page ? EnumChatFormatting.UNDERLINE : "") + String.valueOf(page)));

            index++;
        }

        if (index > (MAX_PAGES - 2)) {
            this.buttonList.add(new PageBtn(this.buttonList.size() + index,
                    itemList.xPosition + (xOffsetItem * index) + (xOffsetItem / 2),
                    itemList.yPosition + itemList.height + 8,
                    ((activePageItem + 1) == (minPageItem + 1 + index) ? EnumChatFormatting.UNDERLINE : "") +
                            String.valueOf((minPageItem + 1 + index) == PixelbayAPI.Client.maxItemPages ?
                                    (minPageItem + 1 + index) :
                            PixelbayAPI.Client.maxItemPages + "...")));
        }

        index = 0;
        for (int page = minPagePixelmon + 1; page < maxPagePixelmon + 1; page++) {
            this.buttonList.add(new PageBtn(this.buttonList.size() + index,
                    pixelmonList.xPosition + (xOffsetPixelmon * index) + (xOffsetPixelmon / 2),
                    pixelmonList.yPosition + pixelmonList.height + 8,
                    ((activePagePixelmon + 1) == page ? EnumChatFormatting.UNDERLINE : "") + String.valueOf(page)));

            index++;
        }

        if (index > (MAX_PAGES - 2)) {
            this.buttonList.add(new PageBtn(this.buttonList.size() + index,
                    pixelmonList.xPosition + (xOffsetPixelmon * index) + (xOffsetPixelmon / 2),
                    pixelmonList.yPosition + pixelmonList.height + 8,
                    ((activePagePixelmon + 1) == (minPagePixelmon + 1 + index) ? EnumChatFormatting.UNDERLINE : "") +
                            String.valueOf((minPagePixelmon + 1 + index) == PixelbayAPI.Client.maxPixelmonPages ?
                                    (minPagePixelmon + 1 + index) :
                                    PixelbayAPI.Client.maxPixelmonPages + "...")));
        }
    }

    private boolean isItemPageBtn(GuiButton button) {
        if (button.xPosition >= itemList.xPosition && button.xPosition <= itemList.xPosition + itemList.width) {
            return true;
        } else {
            return false;
        }
    }

    public int getMinimumPage(int page, boolean items) {
        page -= 1;
        page = page < 0 ? 0 : page;

        if (items) {
            if (PixelbayAPI.Client.maxItemPages <= 4) return 0;
        } else {
            if (PixelbayAPI.Client.maxPixelmonPages <= 4) return 0;
        }

        if (items) {
            if (page + MAX_PAGES > PixelbayAPI.Client.maxItemPages)
                return PixelbayAPI.Client.maxItemPages - MAX_PAGES;
        } else {
            if (page + MAX_PAGES > PixelbayAPI.Client.maxPixelmonPages)
                return PixelbayAPI.Client.maxPixelmonPages - MAX_PAGES;
        }
        return page;
    }

    public int getMaximumPage(int page, boolean items) {
        int minPage = getMinimumPage(page, items);
        int ans = minPage + (MAX_PAGES - 1);

        if (items) {
            ans = ans > PixelbayAPI.Client.maxItemPages ? PixelbayAPI.Client.maxItemPages : ans;
        } else {
            ans = ans > PixelbayAPI.Client.maxPixelmonPages ? PixelbayAPI.Client.maxPixelmonPages : ans;
        }

        return ans;
    }
}