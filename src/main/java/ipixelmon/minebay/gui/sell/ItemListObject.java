package ipixelmon.minebay.gui.sell;

import ipixelmon.GuiList;
import ipixelmon.iPixelmon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;

public final class ItemListObject extends GuiList.ListObject {

    private final RenderItem renderItem;
    private final ItemStack itemStack;
    private final SellBtn sellBtn;
    private final GuiTextField priceField;

    public ItemListObject(final int width, final int height, final ItemStack itemStack) {
        super(width, height);
        this.renderItem = Minecraft.getMinecraft().getRenderItem();
        this.itemStack = itemStack;
        this.sellBtn = new SellBtn(0, this.xPos + 300, this.yPos, "Sell");
        this.priceField = new GuiTextField(0, this.mc.fontRendererObj, this.xPos, this.yPos, 100, 13);
        this.priceField.setText("$");
    }

    @Override
    public final void draw(final int x, final int y) {

        this.sellBtn.xPosition = this.xPos + 300;
        this.sellBtn.yPosition = this.yPos + ((this.height - this.sellBtn.height) / 2);
        this.priceField.xPosition =  this.sellBtn.xPosition + this.sellBtn.width + 10;
        this.priceField.yPosition = this.yPos + ((this.height - this.priceField.height) / 2);
        if(this.isSelected) {
            this.sellBtn.drawButton(this.mc, x, y);
            this.priceField.drawTextBox();
        }

        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableBlend();
        if (renderItem != null && itemStack != null) {
            this.renderItem.renderItemAndEffectIntoGUI(itemStack, this.xPos, this.yPos);
            this.renderItem.renderItemOverlayIntoGUI(this.mc.fontRendererObj, itemStack, this.xPos, this.yPos, "" + itemStack.stackSize);
        }
        RenderHelper.disableStandardItemLighting();
    }

    @Override
    public void mouseClicked(final int mouseX, final int mouseY) {
        this.priceField.mouseClicked(mouseX, mouseY, 0);

        if(this.sellBtn.isMouseOver() && this.sellBtn.enabled) {
            iPixelmon.network.sendToServer(new PacketSellItem(itemStack, Long.parseLong(this.priceField.getText().replaceAll("\\$", ""))));
        }
    }

    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
        if(keyCode == Keyboard.KEY_BACK) {
            if(this.priceField.getText().charAt(this.priceField.getCursorPosition() - 1) == '$') {
                return;
            }

            this.priceField.textboxKeyTyped(typedChar, keyCode);
            return;
        }

        if(String.valueOf(typedChar).matches("[0-9]+") || keyCode == Keyboard.KEY_LEFT || keyCode == Keyboard.KEY_RIGHT) {
            if(!this.priceField.getSelectedText().contains("$")) {
                this.priceField.textboxKeyTyped(typedChar, keyCode);
            }
        }
    }

    @Override
    public void updateScreen() {
        this.priceField.updateCursorCounter();

        if(this.priceField.getCursorPosition() == 0) {
            this.priceField.setCursorPosition(1);
        }

        if(this.priceField.getSelectionEnd() == 0) {
            this.priceField.setSelectionPos(1);
        }

        this.sellBtn.enabled = !this.priceField.getText().replaceAll("\\$", "").isEmpty();
    }
}
