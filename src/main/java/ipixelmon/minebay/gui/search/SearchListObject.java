package ipixelmon.minebay.gui.search;

import ipixelmon.GuiList;
import ipixelmon.uuidmanager.UUIDManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;

import java.util.UUID;

public final class SearchListObject extends GuiList.ListObject {

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
        RenderHelper.enableGUIStandardItemLighting();
        this.itemRender.renderItemAndEffectIntoGUI(itemStack, this.xPos, this.yPos);
        RenderHelper.disableStandardItemLighting();

        this.mc.fontRendererObj.drawString("Soled by: " + this.seller, this.xPos + 20, this.yPos, 0xFFFFFF);
        this.mc.fontRendererObj.drawString("Price: $" + price, this.xPos + 20, this.yPos + 11, 0x00ff00);
    }

}
