package ipixelmon.pixelbay.gui.buy;

import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import com.pixelmonmod.pixelmon.storage.PixelmonStorage;
import io.netty.buffer.ByteBuf;
import ipixelmon.PixelmonUtility;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class PacketBuyPokemon implements IMessage
{

    public PacketBuyPokemon()
    {
    }

    private PixelmonData pixelmonData;
    private UUID seller;
    private int price;

    public PacketBuyPokemon(PixelmonData pixelmonData, UUID seller, int price)
    {
        this.pixelmonData = pixelmonData;
        this.seller = seller;
        this.price = price;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        this.pixelmonData = new PixelmonData();
        pixelmonData.decodeInto(buf);
        seller = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        price = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        pixelmonData.encodeInto(buf);
        ByteBufUtils.writeUTF8String(buf, seller.toString());
        buf.writeInt(price);
    }

    public static class Handler implements IMessageHandler<PacketBuyPokemon, IMessage>
    {

        @Override
        public IMessage onMessage(PacketBuyPokemon message, MessageContext ctx)
        {
            // TODO: Give money to buyer. Can use  final PlayerStorage targetStorage = PixelmonStorage.PokeballManager.getPlayerStorageFromUUIDOffline
            // TODO: Actually finish this

            EntityPlayerMP player = ctx.getServerHandler().playerEntity;

            if (player == null) return null;

            try
            {
                if (message.pixelmonData == null)
                {
                    throw new Exception("ItemStack is null.");
                }

                if (message.seller == null)
                {
                    throw new Exception("Seller is null.");
                }

                if (message.price > PixelmonUtility.getServerBalance(player))
                {
                    throw new Exception("Insufficient PokéDollars.");
                }

                EntityPixelmon pokemon = (EntityPixelmon) PixelmonEntityList.createEntityByName(message.pixelmonData.name, player.worldObj);
                pokemon.getLvl().setLevel(message.pixelmonData.lvl);
                pokemon.setIsShiny(message.pixelmonData.isShiny);
                pokemon.setHealth(message.pixelmonData.health);
                PixelmonStorage.PokeballManager.getPlayerStorage(player).addToParty(pokemon);

            } catch (Exception e)
            {
                player.addChatComponentMessage(new ChatComponentText(e.getMessage()));
            }
            return null;
        }
    }
}
