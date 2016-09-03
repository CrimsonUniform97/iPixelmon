package ipixelmon.pixelbay.gui.search;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.GuiScrollingList;

import java.util.ArrayList;
import java.util.List;

public class PokemonList extends GuiScrollingList {

    List<ItemStack> items = new ArrayList<>();
    int selected = -1;

    Minecraft client = null;

    public PokemonList(Minecraft client, int width, int height, int top, int bottom, int left, int entryHeight, int screenWidth, int screenHeight) {
        super(client, width, height, top, bottom, left, entryHeight, screenWidth, screenHeight);

        this.client = client;

        items.add(new ItemStack(Items.arrow));
        items.add(new ItemStack(Items.apple));
        items.add(new ItemStack(Items.gold_ingot));
        items.add(new ItemStack(Items.iron_axe));
        items.add(new ItemStack(Items.golden_sword));
        items.add(new ItemStack(Items.gold_ingot));
        items.add(new ItemStack(Items.golden_boots));
        items.add(new ItemStack(Items.golden_shovel));
        items.add(new ItemStack(Items.mushroom_stew));
        items.add(new ItemStack(Items.golden_carrot));
        items.add(new ItemStack(Items.stick));
        items.add(new ItemStack(Items.carrot_on_a_stick));
        items.add(new ItemStack(Items.paper));
        items.add(new ItemStack(Items.bow));
    }

    @Override
    protected int getSize() {
        return items.size();
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick) {

    }

    @Override
    protected boolean isSelected(int index) {
        return index == selected;
    }

    @Override
    protected void drawBackground() {

    }

    @Override
    protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableBlend();
        if (this.client.getRenderItem() != null && this.items.get(slotIdx) != null) {
            ItemStack stack = this.items.get(slotIdx);
            this.client.getRenderItem().renderItemAndEffectIntoGUI(stack, left, slotTop);
            this.client.getRenderItem().renderItemOverlayIntoGUI(this.client.fontRendererObj, stack, left, slotTop, "" + stack.stackSize);

            RenderHelper.disableStandardItemLighting();

//            mc.fontRendererObj.drawString("Item: " + this.itemStack.getDisplayName(), getX(30, x * 1 - 100) , 2, 0xFFFFFF);
//            mc.fontRendererObj.drawString("Seller: " + playerName, getX(200, x * 2 - 100), 2, 0xFFFFFF);
//            mc.fontRendererObj.drawString("Price: $" + price, getX(200, x * 2 - 100), 11, 0xFFFFFF);
        }
    }
}
