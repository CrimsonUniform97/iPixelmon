package com.ipixelmon.tablet.app.pixelbay;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.mysql.DeleteForm;
import com.ipixelmon.tablet.Tablet;
import com.ipixelmon.util.ItemUtil;
import com.ipixelmon.util.NBTUtil;
import com.ipixelmon.util.PixelmonAPI;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
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
    private EntityPixelmon pixelmon;

    public PixelmonListing(UUID player, String playerName, int price, EntityPixelmon pixelmon) {
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

    public EntityPixelmon getPixelmon() {
        return pixelmon;
    }

    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, player.toString() + "," + playerName);
        buf.writeInt(price);
        ByteBufUtils.writeUTF8String(buf, PixelmonAPI.pixelmonToString(pixelmon));
    }

    public static PixelmonListing fromBytes(ByteBuf buf) {
        String[] data = ByteBufUtils.readUTF8String(buf).split(",");
        UUID player = UUID.fromString(data[0]);
        String playerName = data[1];

        int price = buf.readInt();

        EntityPixelmon pixelmon = PixelmonAPI.pixelmonFromString(ByteBufUtils.readUTF8String(buf), iPixelmon.proxy.getDefaultWorld());

        PixelmonListing pixelmonListing = new PixelmonListing(player, playerName, price, pixelmon);
        return pixelmonListing;
    }

    public boolean confirmListing() {
        ResultSet result = iPixelmon.mysql.query("SELECT * FROM tabletPixelmon WHERE " +
                "player='" + player.toString() + "' AND " +
                "price='" + price + "' AND " +
                "pixelmonData='" + PixelmonAPI.pixelmonToString(pixelmon) + "';");

        try {
            return result.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public void deleteListing() {
        iPixelmon.mysql.delete(Tablet.class, new DeleteForm("Pixelmon")
                .add("player", player.toString())
                .add("price", price)
                .add("pixelmonData", PixelmonAPI.pixelmonToString(pixelmon)));
    }

}
