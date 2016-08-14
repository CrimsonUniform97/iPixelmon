package ipixelmon.pixelbay.gui.search;

import com.pixelmonmod.pixelmon.comm.PixelmonData;
import ipixelmon.ItemSerializer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public final class SearchListPopulator implements Runnable {

    private final ResultSet resultPokemon, resultItem;
    public final SearchList searchList;

    public SearchListPopulator(final ResultSet resultPokemon, final ResultSet resultItem, final SearchList searchList) {
        this.resultPokemon = resultPokemon;
        this.resultItem = resultItem;
        this.searchList = searchList;
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
                this.searchList.addObject(new PokemonSearchObject(pData, UUID.fromString(resultPokemon.getString("seller")), resultPokemon.getLong("price")));
            }
        }
        long endTime = System.currentTimeMillis();

        System.out.println("populatePokemon() took " + (endTime - startTime) + " milliseconds.");
        this.searchList.initGui();
    }

    public final void populateItem() throws SQLException {
        ItemStack item;
        long startTime = System.currentTimeMillis();
        while (resultItem.next()) {
            item = ItemSerializer.itemFromString(resultItem.getString("item"));
            if (item != null)
                this.searchList.addObject(new ItemSearchObject(item, UUID.fromString(resultItem.getString("seller")), resultItem.getLong("price")));
        }
        long endTime = System.currentTimeMillis();

        System.out.println("populateItems() took " + (endTime - startTime) + " milliseconds.");
    }

}
