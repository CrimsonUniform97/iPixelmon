package com.ipixelmon.tablet.app.pixelbay.lists;

import com.ipixelmon.GuiScrollList;
import com.ipixelmon.util.ArrayUtil;
import com.ipixelmon.util.ItemUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import scala.actors.threadpool.Arrays;

import java.util.List;

/**
 * Created by colby on 12/31/2016.
 */
public class GuiPlayerInventoryList extends GuiScrollList {

    private List<ItemStack> items;

    public GuiPlayerInventoryList(int xPosition, int yPosition, int width, int height) {
        super(xPosition, yPosition, width, height);
        this.items = Arrays.asList(Minecraft.getMinecraft().thePlayer.inventory.mainInventory);
        ArrayUtil.cleanNull(this.items);
    }

    @Override
    public int getObjectHeight(int index) {
        return 20;
    }

    @Override
    public void drawObject(int index, int mouseX, int mouseY, boolean isHovering) {
        ItemUtil.Client.renderItem(items.get(index), 2, 2, this.width, this.height, mouseX, mouseY);
    }

    @Override
    public int getSize() {
        return items.size();
    }

    @Override
    public void elementClicked(int index, int mouseX, int mouseY, boolean doubleClick) {

    }
}
