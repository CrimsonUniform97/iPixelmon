package com.ipixelmon.tablet.app.pixelbay;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.mysql.DeleteForm;
import com.ipixelmon.tablet.Tablet;
import com.ipixelmon.util.ItemUtil;
import com.ipixelmon.util.PixelmonAPI;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by colby on 1/3/2017.
 */
public class PixelmonListing {

    private UUID player;
    private String playerName;
    private int price;
    private PixelmonData pixelmon;

    public PixelmonListing(UUID player, String playerName, int price, PixelmonData pixelmon) {
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

    public int getPrice() {
        return price;
    }

    public PixelmonData getPixelmon() {
        return pixelmon;
    }

    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, player.toString() + "," + playerName);
        buf.writeInt(price);
        pixelmon.encodeInto(buf);
    }

    public static PixelmonListing fromBytes(ByteBuf buf) {
        String[] data = ByteBufUtils.readUTF8String(buf).split(",");
        UUID player = UUID.fromString(data[0]);
        String playerName = data[1];
        int price = buf.readInt();
        PixelmonData pixelmon = new PixelmonData();
        pixelmon.decodeInto(buf);
        return new PixelmonListing(player, playerName, price, pixelmon);
    }

    public boolean confirmListing() {
        ResultSet result = iPixelmon.mysql.query("SELECT * FROM tabletPixelmon WHERE " +
                "player='" + player.toString() + "' AND " +
                "price='" + price + "' AND " +
                "pixelmon='" + PixelmonAPI.pixelmonDataToString(pixelmon) + "';");
        try {
            if(result.next()) return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    public void deleteListing() {
        iPixelmon.mysql.delete(Tablet.class, new DeleteForm("Pixelmon")
                .add("player", player.toString())
                .add("price", price)
                .add("pixelmon", PixelmonAPI.pixelmonDataToString(pixelmon)));
    }

}
