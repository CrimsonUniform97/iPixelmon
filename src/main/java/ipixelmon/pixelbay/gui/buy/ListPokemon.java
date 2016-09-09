package ipixelmon.pixelbay.gui.buy;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import ipixelmon.PixelmonUtility;
import ipixelmon.iPixelmon;
import ipixelmon.pixelbay.gui.ColorPicker;
import ipixelmon.uuidmanager.UUIDManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.util.Rectangle;

import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ListPokemon extends ISearchList
{

    public List<PokeListInfo> entries = new ArrayList<>();
    private int mysqlRow = 0, mysqlSearchLimit = 100;
    private GuiSearch parentScreen;

    public ListPokemon(Minecraft client, int width, int height, int top, int bottom, int left, GuiSearch parentScreen)
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
                Minecraft.getMinecraft().displayGuiScreen(new GuiPopupBuy(parentScreen, "", "Buy Pokémon?", 3));
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
            PokeListInfo entryInfo = this.entries.get(slotIdx);

            // draw the next and previous page entries
            if (entryInfo.sellerName == null)
            {
                if (entryInfo.price == -9999)
                    this.client.fontRendererObj.drawString("Next Page >>> Double Click This Slot", left + (listWidth - this.client.fontRendererObj.getStringWidth("Next Page >>> Double Click This Slot")) / 2, slotTop + 8, 0xFFFFFF);
                if (entryInfo.price == -9998)
                    this.client.fontRendererObj.drawString("Previous Page >>> Double Click This Slot", left + (listWidth - this.client.fontRendererObj.getStringWidth("Previous Page >>> Double Click This Slot")) / 2, slotTop + 8, 0xFFFFFF);
            } else
            {
                GlStateManager.disableBlend();
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                GuiHelper.bindPokemonSprite(entryInfo.pixelmonData, this.client);
                GuiHelper.drawImageQuad(this.left, slotTop - 3, 26.0D, 26.0F, 0.0D, 0.0D, 1.0D, 1.0D, 0.0F);

                this.client.fontRendererObj.drawString("Level: " + entryInfo.pixelmonData.lvl, left + 26, slotTop + 4, 0xFFFFFF);
                this.client.fontRendererObj.drawString("XP: " + entryInfo.pixelmonData.xp, left + 26, slotTop + 15, 0xFFFFFF);
                this.client.fontRendererObj.drawString("Seller: " + entryInfo.sellerName, left + 88, slotTop + 4, 0xFFFFFF);
                this.client.fontRendererObj.drawString("Price:   " + entryInfo.price, left + 88, slotTop + 15, 0xFFFFFF);
                PixelmonUtility.drawPokeDollar(client, left + 91 + client.fontRendererObj.getStringWidth("Price:"), slotTop + 15, 6, 9, 0xFFFFFF);
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
            this.entries.add(new PokeListInfo(new PixelmonData(), UUID.randomUUID(), -9998));
        }

        try
        {
            ResultSet resultPokemon = iPixelmon.mysql.query("SELECT * FROM pixelbayPokemon"
                    + (str != null && !str.trim().isEmpty() ? " WHERE name LIKE '%" + str + "%' " : " ") + "LIMIT " + this.mysqlRow + "," + this.mysqlSearchLimit + ";");

            PixelmonData pData;
            while (resultPokemon != null && resultPokemon.next())
            {
                pData = new PixelmonData();
                if (pData != null)
                {
                    pData.name = resultPokemon.getString("name");
                    pData.xp = resultPokemon.getInt("xp");
                    pData.lvl = resultPokemon.getInt("lvl");
                    pData.isShiny = resultPokemon.getBoolean("isShiny");
                    this.entries.add(new PokeListInfo(pData, UUID.fromString(resultPokemon.getString("seller")), resultPokemon.getInt("price")));
                }
            }
        } catch (SQLException e)
        {
            e.printStackTrace();
        }

        // -1 to make up for the "Previous Page" entry
        // add next page button
        if (this.entries.size() - (mysqlRow == 0 ? 0 : 1) == this.mysqlSearchLimit)
        {
            this.entries.add(new PokeListInfo(new PixelmonData(), UUID.randomUUID(), -9999));
        }
    }

    public PokeListInfo getSelected()
    {
        return entries.get(this.selectedIndex);
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


    public class PokeListInfo
    {
        public PixelmonData pixelmonData;
        public int price;
        public UUID seller;
        public String sellerName;

        public PokeListInfo(PixelmonData pixelmonData, UUID seller, int price)
        {
            this.pixelmonData = pixelmonData;
            this.price = price;
            this.seller = seller;
            sellerName = UUIDManager.getPlayerName(seller);
        }
    }

}
