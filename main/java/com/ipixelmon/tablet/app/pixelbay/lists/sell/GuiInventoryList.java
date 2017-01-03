package com.ipixelmon.tablet.app.pixelbay.lists.sell;

import com.google.common.collect.Maps;
import com.ipixelmon.tablet.app.pixelbay.gui.sell.SellGuiItem;
import com.ipixelmon.tablet.app.pixelbay.lists.IScrollListWithDesign;
import com.ipixelmon.util.ItemUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;
import java.util.Map;

/**
 * Created by colby on 12/31/2016.
 */
public class GuiInventoryList extends IScrollListWithDesign {

    private Map<Integer, ItemStack> items = Maps.newHashMap();

    public GuiInventoryList(int xPosition, int yPosition, int width, int height) {
        super(xPosition, yPosition, width, height);
        for(int slot = 0; slot < mc.thePlayer.inventory.mainInventory.length; slot++) {
            if(mc.thePlayer.inventory.mainInventory[slot] != null) {
                items.put(slot, mc.thePlayer.inventory.mainInventory[slot]);
            }
        }
        drawScrollbarOnTop = false;
    }

    @Override
    public int getObjectHeight(int index) {
        return 22;
    }

    @Override
    public void drawObject(int index, int mouseX, int mouseY, boolean isHovering) {
        ItemUtil.Client.renderItem((ItemStack) items.values().toArray()[index], 4, 2, this.width, this.height, mouseX, mouseY);
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
    }

    @Override
    public int getSize() {
        return items.size();
    }

    @Override
    public void elementClicked(int index, int mouseX, int mouseY, boolean doubleClick) {
        if(doubleClick) {
            Minecraft.getMinecraft().displayGuiScreen(new SellGuiItem(new Object[]{items.values().toArray()[index],
                    items.keySet().toArray()[index]}));
        }
    }

}
