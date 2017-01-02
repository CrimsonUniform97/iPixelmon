package com.ipixelmon.tablet.app.pixelbay.lists;

import com.ipixelmon.tablet.app.pixelbay.gui.SellGuiItem;
import com.ipixelmon.tablet.app.pixelbay.gui.SellGuiPixelmon;
import com.ipixelmon.util.ArrayUtil;
import com.ipixelmon.util.ItemUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import scala.actors.threadpool.Arrays;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by colby on 12/31/2016.
 */
public class GuiInventoryList extends IScrollListWithDesign {

    private List<ItemStack> items;

    public GuiInventoryList(int xPosition, int yPosition, int width, int height) {
        super(xPosition, yPosition, width, height);
        this.items = new ArrayList<>(Arrays.asList(Minecraft.getMinecraft().thePlayer.inventory.mainInventory));
        ArrayUtil.cleanNull(this.items);
        drawScrollbarOnTop = false;
    }

    @Override
    public int getObjectHeight(int index) {
        return 22;
    }

    @Override
    public void drawObject(int index, int mouseX, int mouseY, boolean isHovering) {
        ItemUtil.Client.renderItem(items.get(index), 4, 2, this.width, this.height, mouseX, mouseY);
    }

    @Override
    public int getSize() {
        return items.size();
    }

    @Override
    public void elementClicked(int index, int mouseX, int mouseY, boolean doubleClick) {
        if(doubleClick) {
            Minecraft.getMinecraft().displayGuiScreen(new SellGuiItem(new Object[]{items.get(index)}));
        }
    }

}
