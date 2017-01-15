package com.ipixelmon.tablet.app.pixelbay;

import com.google.common.collect.Lists;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.mysql.InsertForm;
import com.ipixelmon.tablet.Tablet;
import com.ipixelmon.util.DateUtil;
import com.ipixelmon.util.ItemUtil;
import com.ipixelmon.util.NBTUtil;
import com.ipixelmon.util.PixelmonAPI;
import com.ipixelmon.uuidmanager.UUIDManager;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

/**
 * Created by colby on 12/31/2016.
 */
public class PixelbayAPI {

    public static final int maxResults = 3;
    public static final int maxListings = 100;

    @SideOnly(Side.CLIENT)
    public static class Client {

        public static List<ItemListing> itemListings = Lists.newArrayList();
        public static List<PixelmonListing> pixelmonListings = Lists.newArrayList();
        public static int maxItemPages, maxPixelmonPages;

    }

    @SideOnly(Side.SERVER)
    public static class Server {

        public static List<ItemListing> getItemsForSale(int page, String searchFor) {
            String query = "SELECT * FROM tabletItems WHERE item LIKE '%" + searchFor + "%' " +
                    "ORDER BY price LIMIT " + (page * maxResults) + "," + maxResults + ";";

            if(searchFor == null || searchFor.isEmpty()) {
                query = "SELECT * FROM tabletItems ORDER BY price ASC LIMIT " + (page * maxResults) + "," + maxResults + ";";
            }

            ResultSet result = iPixelmon.mysql.query(query);

            List<ItemListing> items = Lists.newArrayList();

            try {
                ItemStack stack;
                UUID uuid;
                while (result.next()) {
                    stack = ItemUtil.itemFromString(result.getString("item"));
                    uuid = UUID.fromString(result.getString("player"));
                    items.add(new ItemListing(uuid, UUIDManager.getPlayerName(uuid), result.getInt("price"), stack));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return items;
        }

        public static List<PixelmonListing> getPixelmonForSale(int page, String searchFor) {

            String query = "SELECT * FROM tabletPixelmon WHERE pixelmonName LIKE '%" + searchFor + "%' " +
                    "ORDER BY price LIMIT " + (page * maxResults) + "," + maxResults + ";";

            if(searchFor == null || searchFor.isEmpty()) {
                query = "SELECT * FROM tabletPixelmon ORDER BY price ASC LIMIT " + (page * maxResults) + "," + maxResults + ";";
            }

            ResultSet result = iPixelmon.mysql.query(query);

            List<PixelmonListing> pixelmonListings = Lists.newArrayList();

            try {
                NBTTagCompound tagCompound;
                UUID uuid;
                while (result.next()) {
                    tagCompound = NBTUtil.fromString(result.getString("pixelmonData"));
                    uuid = UUID.fromString(result.getString("player"));
                    EntityPixelmon pixelmon = PixelmonAPI.getPokemonEntity(tagCompound, MinecraftServer.getServer().getEntityWorld());
                    pixelmonListings.add(new PixelmonListing(uuid, UUIDManager.getPlayerName(uuid),
                            result.getInt("price"), pixelmon));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return pixelmonListings;
        }

        public static int getMaxItemPages(String searchFor) {
            String query = "SELECT COUNT(*) AS items FROM tabletItems";

            if(searchFor != null || !searchFor.isEmpty()) {
                query += " WHERE item LIKE '%" + searchFor + "%';";
            }

            ResultSet result = iPixelmon.mysql.query(query);
            try {
                if(result.next()) {
                    int results = result.getInt("items");
                    int pages = results / maxResults;
                    if (results % maxResults != 0) pages++;
                    return pages;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return 0;
        }

        public static int getMaxPixelmonPages(String searchFor) {
            String query = "SELECT COUNT(*) AS pixelmons FROM tabletPixelmon";

            if(searchFor != null || !searchFor.isEmpty()) {
                query += " WHERE pixelmonName LIKE '%" + searchFor + "%';";
            }

            ResultSet result = iPixelmon.mysql.query(query);
            try {
                if(result.next()) {
                    int results = result.getInt("pixelmons");
                    int pages = results / maxResults;
                    if (results % maxResults != 0) pages++;
                    return pages;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return 0;
        }

        public static void postItem(UUID player, ItemStack stack, int price) {
            InsertForm insertForm = new InsertForm("Items");
            insertForm.add("player", player.toString());
            insertForm.add("price", price);
            insertForm.add("item", ItemUtil.itemToString(stack));
            System.out.println(DateUtil.dateToString(DateUtil.getCurrentTime()));
            iPixelmon.mysql.insert(Tablet.class, insertForm);
        }

        public static void postPixelmon(UUID player, EntityPixelmon pixelmon, int price) {
            InsertForm insertForm = new InsertForm("Pixelmon");
            insertForm.add("player", player.toString());
            insertForm.add("price", price);
            insertForm.add("pixelmonName", pixelmon.getName());
            insertForm.add("pixelmonData", PixelmonAPI.getNBT(pixelmon).toString());
            iPixelmon.mysql.insert(Tablet.class, insertForm);
        }

        public static int getListingCount(UUID player) {
            ResultSet resultItem = iPixelmon.mysql.query("SELECT COUNT(player) AS listings FROM tabletItems WHERE player='" + player.toString() + "';");
            ResultSet resultPixelmon = iPixelmon.mysql.query("SELECT COUNT(player) AS listings FROM tabletPixelmon WHERE player='" + player.toString() + "';");
            int count = 0;

            try {
                if (resultItem.next()) count += resultItem.getInt("listings");
                if (resultPixelmon.next()) count += resultPixelmon.getInt("listings");
            } catch (Exception e) {
            }

            return count;
        }

    }

}
