package ipixelmon.pixelbay.gui.search;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import ipixelmon.ItemSerializer;
import ipixelmon.guiList.IGuiList;
import ipixelmon.guiList.IListObject;
import ipixelmon.iPixelmon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.util.Rectangle;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public final class SearchList extends IGuiList{

    private int maxPages;
    private int pokeIndex, itemIndex;
    public final int searchLimit = 150;

    public SearchList(GuiScreen screen) {
        super(screen);
    }

    @Override
    public void drawList(int mouseX, int mouseY, Minecraft mc) {
        mc.getTextureManager().bindTexture(new ResourceLocation(iPixelmon.id + ":textures/gui/PixelbayLogo.png"));
        int logoWidth = 398 / 2;
        int logoHeight = 108 / 2;
        GuiHelper.drawImageQuad(this.getBounds().getX() + ((this.getBounds().getWidth() - logoWidth) / 2), this.getBounds().getY() - logoHeight, logoWidth, logoHeight, 0.0D, 0.0D, 1.0D, 1.0D, 0.0F);

        String pageString = "Page (" + (this.getPage() + 1) + "/" + (this.maxPages + 1) + ")";
        int pageStringWidth = mc.fontRendererObj.getStringWidth(pageString);
        mc.fontRendererObj.drawString(pageString, this.getBounds().getX() + (this.getBounds().getWidth() - pageStringWidth), this.getBounds().getY() - 10, 0xFFFFFF);

        super.drawList(mouseX, mouseY, mc);
    }

    @Override
    public void initGui() {
        super.initGui();
        maxPages = this.getMaxPages();
    }

    @Override
    public Rectangle getBounds() {
        int listWidth = this.getParentScreen().width - 50, listHeight = this.getParentScreen().height - 50;
        return new Rectangle((this.getParentScreen().width - listWidth) / 2, ((this.getParentScreen().height - listHeight) / 2) + 40, listWidth, listHeight - 40);
    }


    public void search(String toSearch) {
        ResultSet resultPokemon =  iPixelmon.mysql.query("SELECT * FROM pixelbayPokemon " + (toSearch != null && !toSearch.isEmpty() ? "WHERE name LIKE '%" + toSearch + "%' " : "") + "LIMIT " + pokeIndex + "," + this.getPokeSearchLimit() + ";");

        try {
            this.setObjects(new IListObject[searchLimit]);

            PixelmonData pData;
            while(resultPokemon.next()) {
                pData = new PixelmonData();
                if (pData != null) {
                    pData.name = resultPokemon.getString("name");
                    pData.xp = resultPokemon.getInt("xp");
                    pData.lvl = resultPokemon.getInt("lvl");
                    pData.isShiny = resultPokemon.getBoolean("isShiny");
                    this.addObject(new PokemonSearchObject(pData, UUID.fromString(resultPokemon.getString("seller")), resultPokemon.getLong("price")));
                }
            }

            ResultSet resultItem = iPixelmon.mysql.query("SELECT * FROM pixelbayItem " + (toSearch != null && !toSearch.isEmpty() ? "WHERE itemName LIKE '%" + toSearch + "%' " : "") + "LIMIT " + itemIndex + "," + this.getItemSearchLimit() + ";");

            ItemStack item;
            while (resultItem.next()) {
                item = ItemSerializer.itemFromString(resultItem.getString("item"));
                if (item != null) this.addObject(new ItemSearchObject(item, UUID.fromString(resultItem.getString("seller")), resultItem.getLong("price")));
            }

            this.initGui();

            if(this.getItemEntries() == this.getItemSearchLimit() && this.getPokeEntries() == this.getPokeSearchLimit()) {
                for (IListObject object : this.getObjectsOnPage(this.getMaxPages())) {
                    this.removeObject(object);
                }

                this.initGui();
            }

            this.setPage(this.getMaxPages());

        }catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public int getPokeIndex() {
        return pokeIndex;
    }

    public int getItemIndex() {
        return itemIndex;
    }

    public void setPokeIndex(final int pokeIndex) {
        this.pokeIndex = pokeIndex;
    }

    public void setItemIndex(final int itemIndex) {
        this.itemIndex = itemIndex;
    }

    public int getPokeEntries() {
        Iterator<IListObject> iterator = this.getIterator();

        int count = 0;
        while(iterator.hasNext()) if(iterator.next() instanceof PokemonSearchObject) count++;

        return count;
    }

    public int getItemEntries() {
        Iterator<IListObject> iterator = this.getIterator();

        int count = 0;
        while(iterator.hasNext()) if(iterator.next() instanceof ItemSearchObject) count++;

        return count;
    }

    public int getPokeSearchLimit() { return  ((searchLimit / 2) + ((searchLimit / 2) - this.getItemEntries())); }

    public int getItemSearchLimit() { return  ((searchLimit / 2) + ((searchLimit / 2) - this.getPokeEntries())); }

    public int getSearchLimit() {
        return searchLimit;
    }

}
