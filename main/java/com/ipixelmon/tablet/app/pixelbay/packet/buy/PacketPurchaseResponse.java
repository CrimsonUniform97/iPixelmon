package com.ipixelmon.tablet.app.pixelbay.packet.buy;

import com.ipixelmon.tablet.app.pixelbay.ItemListing;
import com.ipixelmon.tablet.app.pixelbay.PixelmonListing;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by colby on 1/4/2017.
 */
public class PacketPurchaseResponse implements IMessage {

    private Object object;
    private boolean isItem;

    public PacketPurchaseResponse() {
    }

    public PacketPurchaseResponse(Object object) {
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

    public static class Handler implements IMessageHandler<PacketPurchaseResponse, IMessage> {

        @Override
        public IMessage onMessage(PacketPurchaseResponse message, MessageContext ctx) {

            if(message.isItem) {
                ItemListing itemListing = (ItemListing) message.object;
                
            } else {
                PixelmonListing pixelmonListing = (PixelmonListing) message.object;

            }

            return null;
        }

    }
}
