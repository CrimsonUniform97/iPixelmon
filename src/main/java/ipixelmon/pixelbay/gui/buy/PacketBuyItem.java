package ipixelmon.pixelbay.gui.buy;

import io.netty.buffer.ByteBuf;
import ipixelmon.ItemSerializer;
import ipixelmon.ItemUtil;
import ipixelmon.PixelmonUtility;
import ipixelmon.iPixelmon;
import ipixelmon.mysql.DeleteForm;
import ipixelmon.mysql.SelectionForm;
import ipixelmon.pixelbay.Pixelbay;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.sql.ResultSet;
import java.util.Iterator;
import java.util.UUID;

public class PacketBuyItem implements IMessage {

    public PacketBuyItem() {
    }

    private String itemStack;
    private UUID seller;
    private int price;

    public PacketBuyItem(ItemStack itemStack, UUID seller, int price) {
        this.itemStack = ItemSerializer.itemToString(itemStack);
        this.seller = seller;
        this.price = price;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        itemStack = ByteBufUtils.readUTF8String(buf);
        seller = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        price = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, itemStack);
        ByteBufUtils.writeUTF8String(buf, seller.toString());
        buf.writeInt(price);
    }

    public static class Handler implements IMessageHandler<PacketBuyItem, IMessage> {

        @Override
        public IMessage onMessage(PacketBuyItem message, MessageContext ctx) {
            EntityPlayerMP player = ctx.getServerHandler().playerEntity;

            if (player == null) return null;

            try {
                if (message.itemStack == null) throw new Exception("ItemStack is null.");

                if (message.seller == null) throw new Exception("Seller is null.");

                if(message.price > PixelmonUtility.getBalance(player)) throw new Exception("Insufficient funds.");

                ResultSet result = iPixelmon.mysql.selectAllFrom(Pixelbay.class, new SelectionForm("Item")
                        .add("seller", message.seller.toString())
                        .add("item", message.itemStack)
                        .add("price", "" + message.price)
                        .setLimit(1));

                if (!result.next()) throw new Exception("That listing was not found.");

                Iterator<ItemUtil.ItemStackInfo> iterator = ItemUtil.getPlayerInvIterator(player);

                boolean hasEmptySlot = false;
                ItemUtil.ItemStackInfo itemStackInfo;
                while (iterator.hasNext()) {
                    itemStackInfo = iterator.next();
                    if (itemStackInfo.getItemStack() == null && itemStackInfo.getInventoryType() == ItemUtil.InventoryType.MAIN_INVENTORY) {
                        hasEmptySlot = true;
                        break;
                    }
                }

                if (!hasEmptySlot) throw new Exception("No empty slots found.");

                player.inventory.addItemStackToInventory(ItemSerializer.itemFromString(message.itemStack));
                PixelmonUtility.takeMoney(player, (int) message.price);
                player.addChatComponentMessage(new ChatComponentText(message.price + " deducted from your account."));
                iPixelmon.mysql.delete(Pixelbay.class, new DeleteForm("Item")
                        .add("seller", message.seller.toString())
                        .add("item", message.itemStack)
                        .add("price", "" + message.price));
            } catch (Exception e) {
                e.printStackTrace();
                player.addChatComponentMessage(new ChatComponentText(e.getLocalizedMessage()));
                player.addChatComponentMessage(new ChatComponentText("*** If you think this is an error notify an admin. ***"));
            }

            return null;
        }
    }
}
