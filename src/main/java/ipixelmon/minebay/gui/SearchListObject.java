package ipixelmon.minebay.gui;

import ipixelmon.GuiList;
import ipixelmon.uuidmanager.UUIDManager;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;

import java.util.UUID;

public final class SearchListObject extends GuiList.GuiObject {

    private final ItemStack itemStack;
    private final String seller;
    private final int price;

    private final RenderItem itemRender;

    public SearchListObject(final int width, final int height, final ItemStack itemStack, final UUID seller, final int price, final RenderItem itemRender) {
        super(width, height);

        this.itemStack = itemStack;
        this.seller = UUIDManager.getPlayerName(seller);
        this.price = price;
        this.itemRender = itemRender;
    }

    @Override
    public final void draw(final int x, final int y) {
        this.mc.fontRendererObj.drawString("Soled by: " + this.seller, x + 20, y, 0xFFFFFF);
        this.mc.fontRendererObj.drawString("Price: $" + price, x + 20, y + 11, 0x00ff00);

        this.itemRender.renderItemAndEffectIntoGUI(itemStack, x, y);
    }

}
