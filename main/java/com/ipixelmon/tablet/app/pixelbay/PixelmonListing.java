package com.ipixelmon.tablet.app.pixelbay;

import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.UUID;

/**
 * Created by colby on 1/3/2017.
 */
public class PixelmonListing {

    private UUID player;
    private String playerName;
    private long price;
    private PixelmonData pixelmon;

    public PixelmonListing(UUID player, String playerName, long price, PixelmonData pixelmon) {
        this.player = player;
        this.playerName = playerName;
        this.price = price;
        this.pixelmon = pixelmon;
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

    public PixelmonData getPixelmon() {
        return pixelmon;
    }

    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, player.toString() + "," + playerName);
        buf.writeLong(price);
        pixelmon.encodeInto(buf);
    }

    public static PixelmonListing fromBytes(ByteBuf buf) {
        String[] data = ByteBufUtils.readUTF8String(buf).split(",");
        UUID player = UUID.fromString(data[0]);
        String playerName = data[1];
        long price = buf.readLong();
        PixelmonData pixelmon = new PixelmonData();
        pixelmon.decodeInto(buf);
        return new PixelmonListing(player, playerName, price, pixelmon);
    }


}
