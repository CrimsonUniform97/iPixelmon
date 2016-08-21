package ipixelmon.pixelbay.gui.search;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import ipixelmon.guiList.IListObject;
import ipixelmon.iPixelmon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SearchListPokemon extends BasicSearchList {

    public SearchListPokemon(final SearchGui parentScreen) {
        super(parentScreen);
    }

    @Override
    public void doSearch(final String str) throws SQLException {
        ResultSet resultPokemon = iPixelmon.mysql.query("SELECT * FROM pixelbayPokemon"
                + (str != null && !str.isEmpty() ? " WHERE name LIKE '%" + str + "%' " : " ") + "LIMIT " + this.getRow() + "," + this.getQueryLimit() + ";");

        PixelmonData pData;
        while (resultPokemon.next()) {
            pData = new PixelmonData();
            if (pData != null) {
                pData.name = resultPokemon.getString("name");
                pData.xp = resultPokemon.getInt("xp");
                pData.lvl = resultPokemon.getInt("lvl");
                pData.isShiny = resultPokemon.getBoolean("isShiny");
                this.addObject(new PokemonSearchObject(pData, UUID.fromString(resultPokemon.getString("seller")), resultPokemon.getLong("price")));
            }
        }
    }

    @Override
    public int getQueryLimit() {
        return 250;
    }

    @Override
    public int getMaxTotalEntries() {
        ResultSet result = iPixelmon.mysql.query("SELECT COUNT(*) AS totalRows FROM pixelbayPokemon;");
        try {
            if (result.next())
                return result.getInt("totalRows");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
