package ipixelmon.pixelbay.gui.search;

import ipixelmon.guiList.IListObject;
import ipixelmon.iPixelmon;
import ipixelmon.pixelbay.PacketBuyItem;
import ipixelmon.pixelbay.gui.sell.SellBtn;
import ipixelmon.uuidmanager.UUIDManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import java.util.List;
import java.util.UUID;

public final class ItemSearchObject extends IListObject {

    public final ItemStack itemStack;
    private SellBtn buyBtn;
    private UUID seller;
    private final int price;
    private final String playerName;
    private final int sections = 3;

    public ItemSearchObject(final ItemStack itemStack, final UUID seller, final int price) {
        this.itemStack = itemStack;
        this.price = price;
        this.playerName = UUIDManager.getPlayerName(seller);
        this.seller = seller;
    }

    @Override
    public void drawObject(int mouseX, int mouseY, Minecraft mc) {
        if (this.isSelected()) this.buyBtn.drawButton(mc, mouseX, mouseY);


        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableBlend();
        if (mc.getRenderItem() != null && this.itemStack != null) {
            mc.getRenderItem().renderItemAndEffectIntoGUI(this.itemStack, 4, 2);
            mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, this.itemStack, 4, 2, "" + this.itemStack.stackSize);

            // TODO: Fix the tool tip
            if (mouseX < 16 && mouseY < this.getHeight()) this.renderToolTip(this.itemStack, mouseX, mouseY, mc);

            RenderHelper.disableStandardItemLighting();

            int x = this.getList().getBounds().getWidth() / sections;
            mc.fontRendererObj.drawString("Item: " + this.itemStack.getDisplayName(), getX(30, x * 1 - 100) , 2, 0xFFFFFF);
            mc.fontRendererObj.drawString("Seller: " + playerName, getX(200, x * 2 - 100), 2, 0xFFFFFF);
            mc.fontRendererObj.drawString("Price: $" + price, getX(200, x * 2 - 100), 11, 0xFFFFFF);
        }
    }

    private int getX(int min, int x) {
        return x < min ? min : x;
    }

    @Override
    public void initGui() {
        int x = this.getList().getBounds().getWidth() / sections;
        this.buyBtn = new SellBtn(0, getX(300, x * 3 - 100), 3, "Buy");
    }

    // TODO: Implement buying.
    @Override
    public void mouseClicked(int mouseX, int mouseY, int btn) {
        if (this.buyBtn.isMouseOver()) {
            iPixelmon.network.sendToServer(new PacketBuyItem(itemStack, seller, price));
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }

    @Override
    public void update() {

    }

    @Override
    public int getHeight() {
        return 20;
    }


    protected void renderToolTip(ItemStack stack, int x, int y, Minecraft mc) {
        List<String> list = stack.getTooltip(mc.thePlayer, mc.gameSettings.advancedItemTooltips);

        for (int i = 0; i < list.size(); ++i) {
            if (i == 0) {
                list.set(i, stack.getRarity().rarityColor + (String) list.get(i));
            } else {
                list.set(i, EnumChatFormatting.GRAY + (String) list.get(i));
            }
        }

        FontRenderer font = stack.getItem().getFontRenderer(stack);
        this.drawHoveringText(list, x, y, (font == null ? mc.fontRendererObj : font), mc);
    }

    protected void drawHoveringText(List<String> textLines, int x, int y, FontRenderer font, Minecraft mc) {
        if (!textLines.isEmpty()) {
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            int i = 0;

            for (String s : textLines) {
                int j = font.getStringWidth(s);

                if (j > i) {
                    i = j;
                }
            }

            int l1 = x + 12;
            int i2 = y - 12;
            int k = 8;

            if (textLines.size() > 1) {
                k += 2 + (textLines.size() - 1) * 10;
            }

            if (l1 + i > this.getList().getBounds().getWidth()) {
                l1 -= 28 + i;
            }

            if (i2 + k + 6 > this.getList().getBounds().getHeight()) {
                i2 = this.getList().getBounds().getHeight() - k - 6;
            }

            this.zLevel = 300.0F;
            mc.getRenderItem().zLevel = 300.0F;
            int l = -267386864;
            this.drawGradientRect(l1 - 3, i2 - 4, l1 + i + 3, i2 - 3, l, l);
            this.drawGradientRect(l1 - 3, i2 + k + 3, l1 + i + 3, i2 + k + 4, l, l);
            this.drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 + k + 3, l, l);
            this.drawGradientRect(l1 - 4, i2 - 3, l1 - 3, i2 + k + 3, l, l);
            this.drawGradientRect(l1 + i + 3, i2 - 3, l1 + i + 4, i2 + k + 3, l, l);
            int i1 = 1347420415;
            int j1 = (i1 & 16711422) >> 1 | i1 & -16777216;
            this.drawGradientRect(l1 - 3, i2 - 3 + 1, l1 - 3 + 1, i2 + k + 3 - 1, i1, j1);
            this.drawGradientRect(l1 + i + 2, i2 - 3 + 1, l1 + i + 3, i2 + k + 3 - 1, i1, j1);
            this.drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 - 3 + 1, i1, i1);
            this.drawGradientRect(l1 - 3, i2 + k + 2, l1 + i + 3, i2 + k + 3, j1, j1);

            for (int k1 = 0; k1 < textLines.size(); ++k1) {
                String s1 = (String) textLines.get(k1);
                font.drawStringWithShadow(s1, (float) l1, (float) i2, -1);

                if (k1 == 0) {
                    i2 += 2;
                }

                i2 += 10;
            }

            this.zLevel = 0.0F;
            mc.getRenderItem().zLevel = 0.0F;
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
        }
    }
}
