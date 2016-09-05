package ipixelmon.pixelbay.gui.sell;

import ipixelmon.pixelbay.gui.BasicScrollList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ListItem extends BasicScrollList {

    protected List<ItemStack> items;
    protected SellGui parentScreen;

    public ListItem(final Minecraft client, final int width, final int height, final int top, final int bottom, final int left, final int entryHeight, List<ItemStack> items, SellGui parentScreen) {
        super(client, width, height, top, bottom, left, entryHeight, parentScreen.width, parentScreen.height);
        this.items = items;
        this.parentScreen = parentScreen;
    }

    @Override
    protected int getSize() {
        return items.size();
    }

    @Override
    protected void elementClicked(final int index, final boolean doubleClick) {
        if (doubleClick) {
            parentScreen.sellPopup.visible = true;
        }
    }

    @Override
    protected boolean isSelected(final int index) {
        return index == selectedIndex;
    }

    @Override
    protected void drawBackground() {
        this.drawDefaultBackground();
    }

    @Override
    protected void drawSlot(final int slotIdx, final int entryRight, final int slotTop, final int slotBuffer, final Tessellator tess) {
        if (slotIdx >= items.size()) {
            return;
        }

        ItemStack stack = items.get(slotIdx);

        if (stack == null) {
            return;
        }

        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableBlend();
        this.client.getRenderItem().renderItemAndEffectIntoGUI(stack, left + 2, slotTop + 4);
        this.client.getRenderItem().renderItemOverlayIntoGUI(this.client.fontRendererObj, stack, left + 2, slotTop + 4, "" + stack.stackSize);
        RenderHelper.disableStandardItemLighting();
    }
}
