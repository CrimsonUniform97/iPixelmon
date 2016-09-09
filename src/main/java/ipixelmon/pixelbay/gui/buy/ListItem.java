package ipixelmon.pixelbay.gui.buy;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import ipixelmon.ItemSerializer;
import ipixelmon.PixelmonUtility;
import ipixelmon.iPixelmon;
import ipixelmon.pixelbay.gui.ColorPicker;
import ipixelmon.uuidmanager.UUIDManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Rectangle;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.lwjgl.opengl.GL11.*;

public class ListItem extends ISearchList
{

    public List<ItemListInfo> entries = new ArrayList<>();
    private int mysqlRow = 0, mysqlSearchLimit = 100;
    private GuiSearch parentScreen;

    public ListItem(Minecraft client, int width, int height, int top, int bottom, int left, GuiSearch parentScreen)
    {
        super(client, width, height, top, bottom, left, 30, parentScreen.width, parentScreen.height);
        this.parentScreen = parentScreen;
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
                Minecraft.getMinecraft().displayGuiScreen(new GuiPopupBuy(parentScreen, "", "Buy Item?", 3));
                return;
            }
            search(null);
        }
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
                this.client.fontRendererObj.drawString("Price:   " + entryInfo.price, left + 22, slotTop + 14, 0xFFFFFF);
                PixelmonUtility.drawPokeDollar(client, left + 25 + client.fontRendererObj.getStringWidth("Price:"), slotTop + 13, 6, 9, 0xFFFFFF);
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

    public ItemListInfo getSelected()
    {
        return entries.get(this.selectedIndex);
    }


    public class ItemListInfo
    {
        public ItemStack itemStack;
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
