package ipixelmon.minebay.gui.sell;

import com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.Add;
import io.netty.buffer.ByteBuf;
import ipixelmon.ItemSerializer;
import ipixelmon.iPixelmon;
import ipixelmon.minebay.Minebay;
import ipixelmon.mysql.InsertForm;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public final class PacketSellItem implements IMessage {

    public PacketSellItem() {
    }

    private ItemStack itemStack;
    private long price;

    public PacketSellItem(final ItemStack item, final long price) {
        this.itemStack = item;
        this.price = price;

        System.out.println(ItemSerializer.itemToString(item));
    }

    @Override
    public final void fromBytes(final ByteBuf buf) {
        itemStack = ByteBufUtils.readItemStack(buf);
        price = buf.readLong();
    }

    @Override
    public final void toBytes(final ByteBuf buf) {
        ByteBufUtils.writeItemStack(buf, itemStack);
        buf.writeLong(price);
    }

    public static final class Handler implements IMessageHandler<PacketSellItem, IMessage> {

        @Override
        public final IMessage onMessage(final PacketSellItem message, final MessageContext ctx) {

            final EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            if(player == null) return null;

            player.dropItem(message.itemStack, true, true);

            try {

                if(message.itemStack == null) throw new Exception("Item is null!");

                if(message.price <= 0) throw new Exception("Price must be greater than zero.");

                final InsertForm itemForm = new InsertForm("Item");
                itemForm.add("seller", player.getUniqueID().toString());
                itemForm.add("item", ItemSerializer.itemToString(message.itemStack));
                itemForm.add("price", "" + message.price);

                iPixelmon.mysql.insert(Minebay.class, itemForm);

            } catch(Exception e) {
                e.printStackTrace();
                player.addChatComponentMessage(new ChatComponentText(e.getMessage()));
            }

            return null;
        }
    }
}
