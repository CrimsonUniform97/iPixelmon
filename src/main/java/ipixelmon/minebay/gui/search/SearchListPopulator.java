package ipixelmon.minebay.gui.search;

import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import ipixelmon.GuiList;
import ipixelmon.ItemSerializer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public final class SearchListPopulator implements Runnable {

    private final ResultSet resultPokemon, resultItem;
    private final List<GuiList.ListObject> listObjects;
    public boolean done = false;

    public SearchListPopulator(final ResultSet resultPokemon, final ResultSet resultItem, final List<GuiList.ListObject> listObjects) {
        this.resultPokemon = resultPokemon;
        this.resultItem = resultItem;
        this.listObjects = listObjects;
    }

    @Override
    public final void run() {
        try {
            populateItem();
            populatePokemon();
        }catch(SQLException e) {
            e.printStackTrace();
        }
    }

    public final void populatePokemon() throws SQLException {
        PixelmonData pData;
        while(resultPokemon.next()) {
            pData = new PixelmonData((EntityPixelmon) PixelmonEntityList.createEntityByName(resultPokemon.getString("name"), Minecraft.getMinecraft().theWorld));
            if(pData != null) {
                pData.xp = resultPokemon.getInt("xp");
                pData.lvl = resultPokemon.getInt("lvl");
                pData.isShiny = resultPokemon.getBoolean("isShiny");
                listObjects.add(new PokemonListObject(30, 28, pData, UUID.fromString(resultPokemon.getString("seller")), resultPokemon.getLong("price")));
            }
        }

        this.done = true;
    }

    public final void populateItem() throws SQLException {
        ItemStack item;
        while(resultItem.next()) {
            item = ItemSerializer.itemFromString(resultItem.getString("item"));
            if(item != null) listObjects.add(new ItemListObject(30, 20, item, UUID.fromString(resultItem.getString("seller")), resultItem.getLong("price")));
        }
    }

}
