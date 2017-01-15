package com.ipixelmon.tablet.app.pixelbay.packet.buy;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.app.pixelbay.ItemListing;
import com.ipixelmon.tablet.app.pixelbay.PixelmonListing;
import com.ipixelmon.util.PixelmonAPI;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.battles.controller.participants.PixelmonWrapper;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.Add;
import com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.Remove;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumPokeballs;
import com.pixelmonmod.pixelmon.storage.PCServer;
import com.pixelmonmod.pixelmon.storage.PixelmonStorage;
import com.pixelmonmod.pixelmon.storage.PlayerNotLoadedException;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by colby on 1/4/2017.
 */
public class PacketPurchaseRequest implements IMessage{

    private Object object;
    private boolean isItem;

    public PacketPurchaseRequest() {
    }

    public PacketPurchaseRequest(Object object) {
        this.object = object;
        this.isItem = object instanceof ItemListing;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        isItem = buf.readBoolean();
        if(isItem) {
            object = ItemListing.fromBytes(buf);
        } else {
            object = PixelmonListing.fromBytes(buf);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(isItem);
        if(isItem) {
            ((ItemListing) object).toBytes(buf);
        } else {
            ((PixelmonListing) object).toBytes(buf);
        }
    }

    public static class Handler implements IMessageHandler<PacketPurchaseRequest, IMessage> {

        @Override
        public IMessage onMessage(PacketPurchaseRequest message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            long balance = PixelmonAPI.Server.getBalance(player.getUniqueID());

            if(message.isItem) {
                ItemListing itemListing = (ItemListing) message.object;

                if(!itemListing.confirmListing()) return null;

                if(balance < itemListing.getPrice()) return null;

                if(!player.inventory.addItemStackToInventory(itemListing.getItem())) {
                    return null;
                }

                PixelmonAPI.Server.takeMoney(player.getUniqueID(),itemListing.getPrice());
                PixelmonAPI.Server.giveMoney(itemListing.getPlayer(),itemListing.getPrice());

                itemListing.deleteListing();
            } else {
                PixelmonListing pixelmonListing = (PixelmonListing) message.object;

                if(!pixelmonListing.confirmListing()) {
                    pixelmonListing.deleteListing();
                    return null;
                }

                if(balance < pixelmonListing.getPrice()) return null;

                EntityPixelmon entityPixelmon = pixelmonListing.getPixelmon();

                pixelmonListing.deleteListing();

                entityPixelmon.caughtBall = EnumPokeballs.PokeBall;
                entityPixelmon.friendship.initFromCapture();

                try {
                    PixelmonStorage.PokeballManager.getPlayerStorage(player).addToParty(entityPixelmon);
                } catch (PlayerNotLoadedException e) {
                    e.printStackTrace();
                    return null;
                }

                PixelmonAPI.Server.takeMoney(player.getUniqueID(), pixelmonListing.getPrice());
                PixelmonAPI.Server.giveMoney(pixelmonListing.getPlayer(), pixelmonListing.getPrice());
            }
            return null;
        }

    }

}
