package ipixelmon.pixelbay.gui.search;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import ipixelmon.ItemSerializer;
import ipixelmon.iPixelmon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class SearchListItem extends BasicSearchList {

    public SearchListItem(final SearchGui parentScreen) {
        super(parentScreen);
    }

    @Override
    public void doSearch(final String str) throws SQLException {
        ResultSet resultItem = iPixelmon.mysql.query("SELECT * FROM pixelbayItem"
                + (str != null && !str.isEmpty() ? " WHERE itemName LIKE '%" + str + "%' " : " ") + "LIMIT " + this.getRow() + "," + this.getQueryLimit() + ";");

        ItemStack item;
        while (resultItem.next()) {
            item = ItemSerializer.itemFromString(resultItem.getString("item"));
            if (item != null)
                this.addObject(new ItemSearchObject(item, UUID.fromString(resultItem.getString("seller")), resultItem.getInt("price")));
        }

    }

    @Override
    public int getQueryLimit() {
        return 250;
    }

    @Override
    public int getMaxTotalEntries() {
        ResultSet result = iPixelmon.mysql.query("SELECT COUNT(*) AS totalRows FROM pixelbayItem;");
        try {
            if (result.next())
                return result.getInt("totalRows");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }
}
