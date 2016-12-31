package com.ipixelmon.tablet.app.pixelbay;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.ByteBufUtils;

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
}
