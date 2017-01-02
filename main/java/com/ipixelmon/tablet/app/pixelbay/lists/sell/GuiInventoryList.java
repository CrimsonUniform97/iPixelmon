package com.ipixelmon.tablet.app.pixelbay.lists.sell;

import com.ipixelmon.tablet.app.pixelbay.gui.sell.SellGuiItem;
import com.ipixelmon.tablet.app.pixelbay.lists.IScrollListWithDesign;
import com.ipixelmon.util.ArrayUtil;
import com.ipixelmon.util.ItemUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
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
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        ItemUtil.Client.renderItem(items.get(index), 4, 2, this.width, this.height, mouseX, mouseY);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
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
