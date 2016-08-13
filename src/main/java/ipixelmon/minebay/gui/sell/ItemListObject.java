package ipixelmon.minebay.gui.sell;

import ipixelmon.GuiList;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import org.lwjgl.opengl.GL11;

public final class ItemListObject extends GuiList.ListObject {

    private final RenderItem renderItem;
    private final ItemStack itemStack;

    public ItemListObject(final int width, final int height, final RenderItem renderItem, final ItemStack itemStack) {
        super(width, height);
        this.renderItem = renderItem;
        this.itemStack = itemStack;
    }

    @Override
    public final void draw(final int x, final int y) {
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableBlend();
        if (renderItem != null && itemStack != null) {
            this.renderItem.renderItemAndEffectIntoGUI(itemStack, this.xPos, this.yPos);
            this.renderItem.renderItemOverlayIntoGUI(this.mc.fontRendererObj, itemStack, this.xPos, this.yPos, "" + itemStack.stackSize);
        }
        RenderHelper.disableStandardItemLighting();
    }

}
