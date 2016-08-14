package ipixelmon.pixelbay.gui.search;

import com.pixelmonmod.pixelmon.comm.PixelmonData;
import ipixelmon.GuiList;
import ipixelmon.ItemSerializer;
import net.minecraft.item.ItemStack;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public final class SearchListPopulator implements Runnable {

    private final ResultSet resultPokemon, resultItem;
    public final List<GuiList.ListObject> listObjects;
    public boolean done = false;

    public SearchListPopulator(final ResultSet resultPokemon, final ResultSet resultItem) {
        this.resultPokemon = resultPokemon;
        this.resultItem = resultItem;
        this.listObjects = new ArrayList<>();
    }

    @Override
    public final void run() {
        try {
            populateItem();
            populatePokemon();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // TODO: Only show a set amount of results. If we show too much it could cause huge hangs for the client.

    public final void populatePokemon() throws SQLException {
        PixelmonData pData;
        long startTime = System.currentTimeMillis();
        while (resultPokemon.next()) {
            pData = new PixelmonData();
            if (pData != null) {
                pData.name = resultPokemon.getString("name");
                pData.xp = resultPokemon.getInt("xp");
                pData.lvl = resultPokemon.getInt("lvl");
                pData.isShiny = resultPokemon.getBoolean("isShiny");
                listObjects.add(new PokemonListObject(30, 28, pData, UUID.fromString(resultPokemon.getString("seller")), resultPokemon.getLong("price")));
            }
        }
        long endTime = System.currentTimeMillis();

        System.out.println("populatePokemon() took " + (endTime - startTime) + " milliseconds.");

        this.done = true;
    }

    public final void populateItem() throws SQLException {
        ItemStack item;
        long startTime = System.currentTimeMillis();
        while (resultItem.next()) {
            item = ItemSerializer.itemFromString(resultItem.getString("item"));
            if (item != null)
                listObjects.add(new ItemListObject(30, 20, item, UUID.fromString(resultItem.getString("seller")), resultItem.getLong("price")));
        }
        long endTime = System.currentTimeMillis();

        System.out.println("populateItems() took " + (endTime - startTime) + " milliseconds.");
    }

}
