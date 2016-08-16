package ipixelmon.pixelbay.gui.sell;

import ipixelmon.guiList.IListObject;
import ipixelmon.iPixelmon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.item.ItemStack;
import org.lwjgl.input.Keyboard;

public final class ItemSellObject extends IListObject {

    private final ItemStack item;
    private SellBtn sellBtn;
    private final int sections = 3;
    private GuiTextField priceField;
    private ItemRenderer itemRenderer;

    public ItemSellObject(ItemStack item) {
        this.item = item;
        this.itemRenderer = Minecraft.getMinecraft().getItemRenderer();
    }

    @Override
    public void drawObject(int mouseX, int mouseY, Minecraft mc) {
        if (this.isSelected()) this.sellBtn.drawButton(mc, mouseX, mouseY);


        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableBlend();
        if (mc.getRenderItem() != null && this.item != null) {
            mc.getRenderItem().renderItemAndEffectIntoGUI(this.item, 4, 2);
            mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, this.item, 4, 2, "" + this.item.stackSize);

            RenderHelper.disableStandardItemLighting();

            int x = this.getList().getBounds().getWidth() / this.sections;
            mc.fontRendererObj.drawString("Item: " + this.item.getDisplayName(), getX(30, x * 1 - 100) , 2, 0xFFFFFF);
        }
    }

    @Override
    public void initGui() {
        int x = this.getList().getBounds().getWidth() / this.sections;
        this.sellBtn = new SellBtn(0, getX(300, x * 3 - 100), 2, "Sell");
        String priceFieldText = this.priceField != null ? this.priceField.getText() : "$";
        this.priceField = new GuiTextField(0, Minecraft.getMinecraft().fontRendererObj, getX(200, x * 2 - 100), 2, 75, 20);
        this.priceField.setText(priceFieldText);
    }

    // TODO: Auto update screen list when removing item.
    @Override
    public void mouseClicked(int mouseX, int mouseY, int btn) {
        this.priceField.mouseClicked(mouseX, mouseY, 0);

        if(this.sellBtn.isMouseOver() && this.sellBtn.enabled) {
            iPixelmon.network.sendToServer(new PacketSellItem(this.item, Long.parseLong(this.priceField.getText().replaceAll("\\$", ""))));
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
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
    public void update() {
        this.priceField.updateCursorCounter();

        if(this.priceField.getCursorPosition() == 0) {
            this.priceField.setCursorPosition(1);
        }

        if(this.priceField.getSelectionEnd() == 0) {
            this.priceField.setSelectionPos(1);
        }

        this.sellBtn.enabled = !this.priceField.getText().replaceAll("\\$", "").isEmpty();
    }

    @Override
    public int getHeight() {
        return 20;
    }

    private int getX(int min, int x) {
        return x < min ? min : x;
    }

}
