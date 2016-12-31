package com.ipixelmon.tablet.app.pixelbay.packet;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.mysql.InsertForm;
import com.ipixelmon.tablet.Tablet;
import com.ipixelmon.tablet.app.pixelbay.PixelbayAPI;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by colby on 12/31/2016.
 */
public class PacketSellItem implements IMessage {

    private int slot;
    private long price;

    public PacketSellItem() {
    }

    public PacketSellItem(int slot, long price) {
        this.slot = slot;
        this.price = price;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        slot = buf.readInt();
        price = buf.readLong();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(slot);
        buf.writeLong(price);
    }

    public static class Handler implements IMessageHandler<PacketSellItem, IMessage> {

        @Override
        public IMessage onMessage(PacketSellItem message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            ItemStack stack = player.inventory.mainInventory[message.slot];

            if(stack == null) return null;

            PixelbayAPI.Server.postItem(player.getUniqueID(), stack, message.price);

            player.inventory.mainInventory[message.slot] = null;
            return null;
        }

    }

}
