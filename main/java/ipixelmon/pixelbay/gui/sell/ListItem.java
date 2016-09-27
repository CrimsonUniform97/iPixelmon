package ipixelmon.pixelbay.gui.sell;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;

import java.util.List;

public class ListItem extends ISellList {

    protected List<ItemStack> items;

    public ListItem(final Minecraft client, final int width, final int height, final int top, final int bottom, final int left, final int entryHeight, final GuiSell parentScreen, final List<ItemStack> items)
    {
        super(client, width, height, top, bottom, left, entryHeight, parentScreen);
        this.items = items;
    }

    @Override
    protected int getSize() {
        return items.size();
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
