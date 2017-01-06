package com.ipixelmon.tablet.app.pixelbay.packet.sell;

import com.ipixelmon.tablet.app.pixelbay.PixelbayAPI;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by colby on 12/31/2016.
 */
public class PacketSellItem implements IMessage {

    private int slot, amount;
    private int price;

    public PacketSellItem() {
    }

    public PacketSellItem(int slot, int amount, int price) {
        this.slot = slot;
        this.amount = amount;
        this.price = price;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        slot = buf.readInt();
        amount = buf.readInt();
        price = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(slot);
        buf.writeInt(amount);
        buf.writeInt(price);
    }

    public static class Handler implements IMessageHandler<PacketSellItem, IMessage> {

        @Override
        public IMessage onMessage(PacketSellItem message, MessageContext ctx) {
            if(message.price <= 0) return null;

            EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            ItemStack stack = player.inventory.mainInventory[message.slot];

            if(PixelbayAPI.Server.getListingCount(player.getUniqueID()) > PixelbayAPI.maxListings) return null;

            if(stack == null) return null;

            if(stack.stackSize < message.amount) return null;

            ItemStack newStack = stack.copy();

            if(stack.stackSize - message.amount == 0) {
                player.inventory.mainInventory[message.slot] = null;
            } else {
                newStack.stackSize = stack.stackSize - message.amount;

                player.inventory.mainInventory[message.slot] = newStack;
            }

            stack.stackSize = message.amount;

            PixelbayAPI.Server.postItem(player.getUniqueID(), stack, message.price);
            return null;
        }

    }

}
