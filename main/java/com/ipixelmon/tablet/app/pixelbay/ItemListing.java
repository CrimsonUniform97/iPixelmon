package com.ipixelmon.tablet.app.pixelbay;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.mysql.DeleteForm;
import com.ipixelmon.tablet.Tablet;
import com.ipixelmon.util.ItemUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by colby on 12/31/2016.
 */
public class ItemListing {

    private UUID player;
    private String playerName;
    private long price;
    private ItemStack item;

    public ItemListing(UUID player, String playerName, long price, ItemStack item) {
        this.player = player;
        this.playerName = playerName;
        this.price = price;
        this.item = item;
    }

    public UUID getPlayer() {
        return player;
    }

    public String getPlayerName() {
        return playerName;
    }

    public long getPrice() {
        return price;
    }

    public ItemStack getItem() {
        return item;
    }

    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, player.toString() + "," +  playerName);
        buf.writeLong(price);
        ByteBufUtils.writeItemStack(buf, item);
    }

    public static ItemListing fromBytes(ByteBuf buf) {
        String[] data = ByteBufUtils.readUTF8String(buf).split(",");
        UUID player = UUID.fromString(data[0]);
        String playerName = data[1];
        long price = buf.readLong();
        ItemStack item = ByteBufUtils.readItemStack(buf);
        return new ItemListing(player, playerName, price, item);
    }

    public boolean confirmListing() {
        ResultSet result = iPixelmon.mysql.query("SELECT * FROM tabletItems WHERE " +
                "player='" + player.toString() + "' AND " +
                "price='" + price + "' AND " +
                "item='" + ItemUtil.itemToString(item) + "';");
        try {
            if(result.next()) return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    public void deleteListing() {
        item.stackSize = item.stackSize <= 0 ? 1 : item.stackSize;
        iPixelmon.mysql.delete(Tablet.class, new DeleteForm("Items")
                .add("player", player.toString())
                .add("price", price)
                .add("item", ItemUtil.itemToString(item)));
    }
}
