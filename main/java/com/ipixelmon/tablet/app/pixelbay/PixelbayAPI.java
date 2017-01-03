package com.ipixelmon.tablet.app.pixelbay;

import com.google.common.collect.Lists;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.mysql.InsertForm;
import com.ipixelmon.tablet.Tablet;
import com.ipixelmon.util.ArrayUtil;
import com.ipixelmon.util.ItemUtil;
import com.ipixelmon.util.NBTUtil;
import com.ipixelmon.util.PixelmonAPI;
import com.ipixelmon.uuidmanager.UUIDManager;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by colby on 12/31/2016.
 */
public class PixelbayAPI {

    public static final int maxResults = 30;
    public static final int maxListings = 30;

    @SideOnly(Side.CLIENT)
    public static class Client {

        public static List<ItemListing> itemListings = Lists.newArrayList();
        public static List<PixelmonListing> pixelmonListings = Lists.newArrayList();

    }

    @SideOnly(Side.SERVER)
    public static class Server {

        public static List<ItemListing> getItemsForSale(int page, String searchFor) {
            String query = "SELECT * FROM tabletItems WHERE item LIKE '%" + searchFor + "%' " +
                    "LIMIT " + (page * maxResults) + "," + ((page + 1) * maxResults) + ";";

            if(searchFor == null || searchFor.isEmpty()) {
                query = "SELECT * FROM tabletItems LIMIT " + (page * maxResults) + "," + ((page + 1) * maxResults) + ";";
            }

            ResultSet result = iPixelmon.mysql.query(query);

            List<ItemListing> items = Lists.newArrayList();

            try {
                ItemStack stack;
                UUID uuid;
                while (result.next()) {
                    stack = ItemUtil.itemFromString(result.getString("item"));
                    uuid = UUID.fromString(result.getString("player"));
                    items.add(new ItemListing(uuid, UUIDManager.getPlayerName(uuid), result.getLong("price"), stack));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return items;
        }

        public static List<PixelmonListing> getPixelmonForSale(int page, String searchFor) {
            String query = "SELECT * FROM tabletPixelmon WHERE item LIKE '%" + searchFor + "%' " +
                    "LIMIT " + (page * maxResults) + "," + ((page + 1) * maxResults) + ";";

            if(searchFor == null || searchFor.isEmpty()) {
                query = "SELECT * FROM tabletPixelmon LIMIT " + (page * maxResults) + "," + ((page + 1) * maxResults) + ";";
            }

            ResultSet result = iPixelmon.mysql.query(query);

            List<PixelmonListing> pixelmonListings = Lists.newArrayList();

            try {
                NBTTagCompound tagCompound;
                UUID uuid;
                while (result.next()) {
                    tagCompound = NBTUtil.fromString(result.getString("pixelmon"));
                    uuid = UUID.fromString(result.getString("player"));
                    pixelmonListings.add(new PixelmonListing(uuid, UUIDManager.getPlayerName(uuid),
                            result.getLong("price"), new PixelmonData(tagCompound)));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return pixelmonListings;
        }

        public static void postItem(UUID player, ItemStack stack, long price) {
            InsertForm insertForm = new InsertForm("Items");
            insertForm.add("player", player.toString());
            insertForm.add("price", price);
            insertForm.add("item", ItemUtil.itemToString(stack));
            iPixelmon.mysql.insert(Tablet.class, insertForm);
        }

        public static void postPixelmon(UUID player, EntityPixelmon pixelmon, long price) {
            InsertForm insertForm = new InsertForm("Pixelmon");
            insertForm.add("player", player.toString());
            insertForm.add("price", price);
            insertForm.add("pixelmon", PixelmonAPI.Server.pixelmonToString(pixelmon));
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
