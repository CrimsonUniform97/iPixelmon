package ipixelmon.pixelbay.gui.search;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import ipixelmon.ItemSerializer;
import ipixelmon.iPixelmon;
import ipixelmon.pixelbay.gui.ColorPicker;
import ipixelmon.uuidmanager.UUIDManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.GuiScrollingList;
import org.lwjgl.util.Rectangle;

import java.awt.*;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ListItem extends ISearchList
{

    private List<ItemListInfo> entries = new ArrayList<>();
    private int mysqlRow = 0, mysqlSearchLimit = 100;
    private static final ResourceLocation logo = new ResourceLocation(iPixelmon.id + ":pixelbay/textures/gui/PixelbayLogo.png");

    public ListItem(Minecraft client, int width, int height, int top, int bottom, int left, int entryHeight, int screenWidth, int screenHeight)
    {
        super(client, width, height, top, bottom, left, entryHeight, screenWidth, screenHeight);
    }

    @Override
    protected int getSize()
    {
        return entries.size();
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick)
    {
        if (doubleClick)
        {
            if (this.entries.get(index).price == -9999)
            {
                mysqlRow += mysqlSearchLimit;
                this.scrollDistance = 0.0F;
            } else if (this.entries.get(index).price == -9998)
            {
                mysqlRow -= mysqlSearchLimit;
                this.scrollDistance = 5000.0F;
            } else
            {
                // do buying

                return;
            }
            search(null);
        }
    }

    @Override
    protected boolean isSelected(int index)
    {
        return index == this.selectedIndex;
    }

    @Override
    protected void drawBackground()
    {
        GlStateManager.color(1, 1, 1, 1);
        this.drawRect(new Rectangle(left, top, listWidth, listHeight), ColorPicker.color(16, 0, 16, 250), ColorPicker.color(29, 0, 102, 250));

        this.client.getTextureManager().bindTexture(logo);
        int logoWidth = 398 / 2;
        int logoHeight = 108 / 2;
        GuiHelper.drawImageQuad(this.left + ((this.listWidth - logoWidth) / 2), this.top - logoHeight, logoWidth, logoHeight, 0.0D, 0.0D, 1.0D, 1.0D, 0.0F);
    }

    @Override
    public void drawSelectionBox(final int slotLeft, final int slotTop, final int slotRight, final int slotBuffer)
    {
        this.drawRect(new Rectangle(slotLeft, slotTop, listWidth - 6, slotBuffer), ColorPicker.color(70, 0, 80, 250), ColorPicker.color(90, 0, 100, 250));
    }

    @Override
    public void drawScrollBar(final int scrollBarLeft, final int scrollBarRight, final int thumbTop, final int thumbHeight)
    {
        this.drawRect(new Rectangle(scrollBarLeft, this.top, scrollBarRight - scrollBarLeft, this.bottom - this.top), ColorPicker.color(16, 0, 16, 250), ColorPicker.color(29, 0, 102, 250));
        this.drawRect(new Rectangle(scrollBarLeft, thumbTop, scrollBarRight - scrollBarLeft, thumbHeight),  ColorPicker.color(70, 0, 80, 250), ColorPicker.color(90, 0, 100, 250));
    }

    @Override
    protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess)
    {
        if (this.client.getRenderItem() != null && slotIdx < this.entries.size() && this.entries.get(slotIdx) != null)
        {
            ItemListInfo entryInfo = this.entries.get(slotIdx);

            // draw the next and previous page entries
            if (entryInfo.sellerName == null)
            {
                if (entryInfo.price == -9999)
                    this.client.fontRendererObj.drawString("Next Page >>> Double Click This Slot", left + (listWidth - this.client.fontRendererObj.getStringWidth("Next Page >>> Double Click This Slot")) / 2, slotTop + 8, 0xFFFFFF);
                if (entryInfo.price == -9998)
                    this.client.fontRendererObj.drawString("Previous Page >>> Double Click This Slot", left + (listWidth - this.client.fontRendererObj.getStringWidth("Previous Page >>> Double Click This Slot")) / 2, slotTop + 8, 0xFFFFFF);
            } else
            {
                RenderHelper.enableGUIStandardItemLighting();
                GlStateManager.enableBlend();
                this.client.getRenderItem().renderItemAndEffectIntoGUI(entryInfo.itemStack, left + 2, slotTop + 4);
                this.client.getRenderItem().renderItemOverlayIntoGUI(this.client.fontRendererObj, entryInfo.itemStack, left + 2, slotTop + 4, "" + entryInfo.itemStack.stackSize);
                RenderHelper.disableStandardItemLighting();
                this.client.fontRendererObj.drawString("Seller: " + entryInfo.sellerName, left + 22, slotTop + 4, 0xFFFFFF);
                this.client.fontRendererObj.drawString("Price: $" + entryInfo.price, left + 22, slotTop + 14, 0xFFFFFF);
            }
        }
    }

    @Override
    public void search(String str)
    {
        this.selectedIndex = -1;
        this.entries.clear();

        // add previous button
        if (this.mysqlRow != 0)
        {
            this.entries.add(new ItemListInfo(new ItemStack(Items.iron_pickaxe), UUID.randomUUID(), -9998));
        }

        try
        {
            ResultSet resultItem = iPixelmon.mysql.query("SELECT * FROM pixelbayItem"
                    + (str != null && !str.trim().isEmpty() ? " WHERE itemName LIKE '%" + str + "%' " : " ") + "LIMIT " + this.mysqlRow + "," + this.mysqlSearchLimit + ";");

            ItemStack item;
            while (resultItem.next())
            {
                item = ItemSerializer.itemFromString(resultItem.getString("item"));
                if (item != null)
                    this.entries.add(new ItemListInfo(item, UUID.fromString(resultItem.getString("seller")), resultItem.getInt("price")));
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        // -1 to make up for the "Previous Page" entry
        // add next page button
        if (this.entries.size() - (mysqlRow == 0 ? 0 : 1) == this.mysqlSearchLimit)
        {
            this.entries.add(new ItemListInfo(new ItemStack(Items.iron_pickaxe), UUID.randomUUID(), -9999));
        }
    }

    public void drawRect(Rectangle rect, Color bgColor, Color trimColor)
    {
        int x = rect.getX(), y = rect.getY(), w = rect.getWidth(), h = rect.getHeight();
        x += 4;
        y += 4;
        w -= 8;
        h -= 8;
        int l = bgColor.getRGB();
        this.drawGradientRect(x - 3, y - 4, x + w + 3, y - 3, l, l);
        this.drawGradientRect(x - 3, y + h + 3, x + w + 3, y + h + 4, l, l);
        this.drawGradientRect(x - 3, y - 3, x + w + 3, y + h + 3, l, l);
        this.drawGradientRect(x - 4, y - 3, x - 3, y + h + 3, l, l);
        this.drawGradientRect(x + w + 3, y - 3, x + w + 4, y + h + 3, l, l);
        int i1 = trimColor.getRGB();
        int j1 = (i1 & 16711422 /* white */) >> 1 | i1 & -16777216 /* black */;
        this.drawGradientRect(x - 3, y - 3 + 1, x - 3 + 1, y + h + 3 - 1, i1, j1);
        this.drawGradientRect(x + w + 2, y - 3 + 1, x + w + 3, y + h + 3 - 1, i1, j1);
        this.drawGradientRect(x - 3, y - 3, x + w + 3, y - 3 + 1, i1, i1);
        this.drawGradientRect(x - 3, y + h + 2, x + w + 3, y + h + 3, j1, j1);
    }


    public class ItemListInfo
    {
        private ItemStack itemStack;
        public int price;
        public UUID seller;
        public String sellerName;

        public ItemListInfo(ItemStack itemStack, UUID seller, int price)
        {
            this.itemStack = itemStack;
            this.price = price;
            this.seller = seller;
            sellerName = UUIDManager.getPlayerName(seller);
        }
    }

}
