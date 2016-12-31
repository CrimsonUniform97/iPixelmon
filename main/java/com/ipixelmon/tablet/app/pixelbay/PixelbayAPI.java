package com.ipixelmon.tablet.app.pixelbay;

import com.google.common.collect.Lists;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.mysql.InsertForm;
import com.ipixelmon.tablet.Tablet;
import com.ipixelmon.util.NBTUtil;
import com.ipixelmon.uuidmanager.UUIDManager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

/**
 * Created by colby on 12/31/2016.
 */
public class PixelbayAPI {

    public static final int maxResults = 30;

    public static class Server {

        public static List<ItemListing> getItemsForSale(int page, String searchFor) {
            String query = "SELECT * FROM tabletItems WHERE item LIKE '%" + searchFor + "%' " +
                    "LIMIT " + (page * maxResults) + "," + ((page + 1) * maxResults) + ";";
            ResultSet result = iPixelmon.mysql.query(query);

            List<ItemListing> items = Lists.newArrayList();

            try {
                ItemStack stack;
                UUID uuid;
                while (result.next()) {
                    stack = ItemStack.loadItemStackFromNBT(NBTUtil.fromString(result.getString("item")));
                    uuid = UUID.fromString(result.getString("player"));
                    items.add(new ItemListing(uuid, UUIDManager.getPlayerName(uuid), result.getLong("price"), stack));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return items;
        }

        public static void postItem(UUID player, ItemStack stack, long price) {
            InsertForm insertForm = new InsertForm("Items");
            insertForm.add("player", player.toString());
            insertForm.add("price", price);
            NBTTagCompound tagCompound = stack.writeToNBT(new NBTTagCompound());
            insertForm.add("item", tagCompound.toString());
            iPixelmon.mysql.insert(Tablet.class, insertForm);
        }

    }

}
