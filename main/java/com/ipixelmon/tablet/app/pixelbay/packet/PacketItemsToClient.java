package com.ipixelmon.tablet.app.pixelbay.packet;

import com.google.common.collect.Lists;
import com.ipixelmon.tablet.app.pixelbay.ItemListing;
import com.ipixelmon.tablet.app.pixelbay.PixelbayAPI;
import com.ipixelmon.tablet.app.pixelbay.PixelbayGui;
import io.netty.buffer.ByteBuf;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.List;

/**
 * Created by colby on 12/31/2016.
 */
public class PacketItemsToClient implements IMessage {

    public PacketItemsToClient() {
    }

    private List<ItemListing> items;

    public PacketItemsToClient(int page, String search) {
        this.items = PixelbayAPI.Server.getItemsForSale(page, search);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int size = buf.readInt();
        this.items = Lists.newArrayList();

        for(int i = 0; i < size; i++) {
            this.items.add(ItemListing.fromBytes(buf));
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(items.size());

        for(ItemListing itemListing : items) {
            itemListing.toBytes(buf);
        }
    }

    public static class Handler implements IMessageHandler<PacketItemsToClient, IMessage> {

        @Override
        public IMessage onMessage(PacketItemsToClient message, MessageContext ctx) {
            // TODO: Display in PixelbayGui
            PixelbayGui.itemListings.clear();
            PixelbayGui.itemListings.addAll(message.items);
            return null;
        }

    }
}
