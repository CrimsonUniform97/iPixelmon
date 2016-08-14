package ipixelmon.pixelbay.gui.search;

import ipixelmon.GuiList;
import ipixelmon.pixelbay.gui.sell.SellBtn;
import ipixelmon.uuidmanager.UUIDManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;

import java.util.UUID;

public final class ItemListObject extends GuiList.ListObject {

    private final ItemStack itemStack;
    private final SellBtn buyBtn;
    private final long price;
    private final String playerName;
    private final RenderItem renderItem;

    public ItemListObject(final int width, final int height, final ItemStack itemStack, final UUID seller, final long price) {
        super(width, height);
        this.itemStack = itemStack;
        this.renderItem = Minecraft.getMinecraft().getRenderItem();
        this.buyBtn = new SellBtn(0, this.xPos + 300, this.yPos, "Buy");
        this.price = price;
        this.playerName = UUIDManager.getPlayerName(seller);
    }

    // TODO: Draw enchantments while hovering over item.
    @Override
    public final void draw(final int x, final int y) {
        this.buyBtn.xPosition = this.xPos + 300;
        this.buyBtn.yPosition = this.yPos + ((this.height - this.buyBtn.height) / 2);
        if(this.isSelected) this.buyBtn.drawButton(this.mc, x, y);

        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableBlend();
        if (this.renderItem != null && this.itemStack != null) {
            this.renderItem.renderItemAndEffectIntoGUI(this.itemStack, this.xPos, this.yPos);
            this.renderItem.renderItemOverlayIntoGUI(this.mc.fontRendererObj, this.itemStack, this.xPos, this.yPos, "" + this.itemStack.stackSize);
        }
        RenderHelper.disableStandardItemLighting();

        this.mc.fontRendererObj.drawString("Item: " + this.itemStack.getDisplayName(), this.xPos + 30, this.yPos + 1, 0xFFFFFF);
        this.mc.fontRendererObj.drawString("Amount: " + this.itemStack.stackSize, this.xPos + 30, this.yPos + 10, 0xFFFFFF);
        this.mc.fontRendererObj.drawString("Seller: " + playerName, this.xPos + 150, this.yPos + 1, 0xFFFFFF);
        this.mc.fontRendererObj.drawString("Price: $" + price, this.xPos + 150, this.yPos + 10, 0xFFFFFF);
    }

    @Override
    public void mouseClicked(final int mouseX, final int mouseY) {
        if(this.buyBtn.isMouseOver() && this.buyBtn.enabled) {
            // TODO: Buy the item, send the packet
        }
    }

}
