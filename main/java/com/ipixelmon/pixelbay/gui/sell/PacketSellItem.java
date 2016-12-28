package com.ipixelmon.pixelbay.gui.sell;

import io.netty.buffer.ByteBuf;
import com.ipixelmon.util.ItemUtil;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.pixelbay.Pixelbay;
import com.ipixelmon.mysql.InsertForm;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.Iterator;

public final class PacketSellItem implements IMessage {

    public PacketSellItem() {
    }

    private ItemStack itemStack;
    private int price;

    public PacketSellItem(final ItemStack item, final int price) {
        this.itemStack = item;
        this.price = price;
    }

    @Override
    public final void fromBytes(final ByteBuf buf) {
        itemStack = ByteBufUtils.readItemStack(buf);
        price = buf.readInt();
    }

    @Override
    public final void toBytes(final ByteBuf buf) {
        ByteBufUtils.writeItemStack(buf, itemStack);
        buf.writeInt(price);
    }

    public static final class Handler implements IMessageHandler<PacketSellItem, IMessage> {

        @Override
        public final IMessage onMessage(final PacketSellItem message, final MessageContext ctx) {

            final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            if(player == null) return null;

            try {

                if(message.itemStack == null) throw new Exception("Item is null!");

                if(message.price <= 0) throw new Exception("Price must be greater than zero.");

                Iterator<ItemUtil.ItemStackInfo> iterator = iPixelmon.util.item.getPlayerInvIterator(player);
                ItemUtil.ItemStackInfo itemStackInfo;
                boolean foundItemStack = false;
                while(iterator.hasNext()) {
                    itemStackInfo = iterator.next();
                    if(itemStackInfo.itemStackEquals(message.itemStack)) {
                        itemStackInfo.removeFromPlayersInventory(player);
                        foundItemStack = true;
                        break;
                    }
                }

                if(!foundItemStack) {
                    player.addChatComponentMessage(new ChatComponentText("Could not find item within your inventory. Notify an admin."));
                    return null;
                }

                final InsertForm itemForm = new InsertForm("Item");
                itemForm.add("seller", player.getUniqueID().toString());
                itemForm.add("item", iPixelmon.util.item.itemToString(message.itemStack));
                itemForm.add("itemName", message.itemStack.getUnlocalizedName().toLowerCase().replaceAll("item.", "").replaceAll("tile.", ""));
                itemForm.add("price", "" + message.price);

                iPixelmon.mysql.insert(Pixelbay.class, itemForm);
                iPixelmon.network.sendTo(new PacketSellResponse(message.itemStack), player);
            } catch(Exception e) {
                e.printStackTrace();
                player.addChatComponentMessage(new ChatComponentText(e.getMessage()));
            }

            return null;
        }
    }
}
