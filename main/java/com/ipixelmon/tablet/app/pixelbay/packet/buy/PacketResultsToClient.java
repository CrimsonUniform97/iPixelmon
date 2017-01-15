package com.ipixelmon.tablet.app.pixelbay.packet.buy;

import com.google.common.collect.Lists;
import com.ipixelmon.tablet.app.pixelbay.ItemListing;
import com.ipixelmon.tablet.app.pixelbay.PixelbayAPI;
import com.ipixelmon.tablet.app.pixelbay.PixelmonListing;
import com.ipixelmon.util.PixelmonAPI;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

import java.util.List;

/**
 * Created by colby on 12/31/2016.
 */
public class PacketResultsToClient implements IMessage {

    public PacketResultsToClient() {
    }

    private boolean searchForItems;
    private List<ItemListing> items;
    private List<PixelmonListing> pixelmons;
    private int maxPages;

    public PacketResultsToClient(int page, String criteria, boolean searchForItems) {
        this.searchForItems = searchForItems;
        if(searchForItems) {
            this.items = PixelbayAPI.Server.getItemsForSale(page, criteria);
            this.maxPages = PixelbayAPI.Server.getMaxItemPages(criteria);
        } else {
            this.pixelmons = PixelbayAPI.Server.getPixelmonForSale(page, criteria);
            this.maxPages = PixelbayAPI.Server.getMaxPixelmonPages(criteria);
        }
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        maxPages = buf.readInt();
        searchForItems = buf.readBoolean();
        int size = buf.readInt();

        if(searchForItems) {
            this.items = Lists.newArrayList();

            for (int i = 0; i < size; i++) {
                this.items.add(ItemListing.fromBytes(buf));
            }
        } else {
            this.pixelmons = Lists.newArrayList();

            for(int i = 0; i < size; i++) {
                this.pixelmons.add(PixelmonListing.fromBytes(buf));
            }
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(maxPages);
        buf.writeBoolean(searchForItems);
        if(searchForItems) {
            buf.writeInt(items.size());

            for (ItemListing itemListing : items) {
                itemListing.toBytes(buf);
            }
        } else {
            buf.writeInt(pixelmons.size());

            for (PixelmonListing pixelmonListing : pixelmons) {
                pixelmonListing.toBytes(buf);
            }
        }
    }

    public static class Handler implements IMessageHandler<PacketResultsToClient, IMessage> {

        @Override
        public IMessage onMessage(PacketResultsToClient message, MessageContext ctx) {
            if(message.searchForItems) {
                PixelbayAPI.Client.itemListings.clear();
                PixelbayAPI.Client.itemListings.addAll(message.items);
                PixelbayAPI.Client.maxItemPages = message.maxPages;
            } else {
                PixelbayAPI.Client.pixelmonListings.clear();
                PixelbayAPI.Client.pixelmonListings.addAll(message.pixelmons);
                PixelbayAPI.Client.maxPixelmonPages = message.maxPages;
            }
            return null;
        }

    }
}
