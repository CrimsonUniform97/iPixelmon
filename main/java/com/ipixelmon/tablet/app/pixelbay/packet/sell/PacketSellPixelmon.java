package com.ipixelmon.tablet.app.pixelbay.packet.sell;

import com.ipixelmon.tablet.app.pixelbay.PixelbayAPI;
import com.ipixelmon.util.PixelmonAPI;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.storage.PixelmonStorage;
import com.pixelmonmod.pixelmon.storage.PlayerNotLoadedException;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by colby on 1/3/2017.
 */
public class PacketSellPixelmon implements IMessage {

    private int[] pixelmonID;
    private long price;

    public PacketSellPixelmon() {
    }

    public PacketSellPixelmon(int[] pixelmonID, long price) {
        this.pixelmonID = pixelmonID;
        this.price = price;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pixelmonID = new int[2];
        pixelmonID[0] = buf.readInt();
        pixelmonID[1] = buf.readInt();
        price = buf.readLong();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(pixelmonID[0]);
        buf.writeInt(pixelmonID[1]);
        buf.writeLong(price);
    }

    public static class Handler implements IMessageHandler<PacketSellPixelmon, IMessage> {

        @Override
        public IMessage onMessage(PacketSellPixelmon message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;

            if(message.price <= 0) return null;

            if(PixelbayAPI.Server.getListingCount(player.getUniqueID()) > PixelbayAPI.maxListings) return null;

            if(PixelmonAPI.Server.getPixelmon(player, true).size() == 1) return null;

            try {
                EntityPixelmon entityPixelmon = PixelmonStorage.PokeballManager.getPlayerStorage(player)
                        .getPokemon(message.pixelmonID, player.worldObj);

                PixelbayAPI.Server.postPixelmon(player.getUniqueID(), new PixelmonData(entityPixelmon), message.price);
                PixelmonAPI.Server.removePixelmon(entityPixelmon, player);
            } catch (PlayerNotLoadedException e) {
                e.printStackTrace();
            }
            return null;
        }

    }
}