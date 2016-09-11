package ipixelmon.pixelbay.gui.buy;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.achievement.PixelmonAchievements;
import com.pixelmonmod.pixelmon.api.enums.ReceiveType;
import com.pixelmonmod.pixelmon.api.events.PixelmonRecievedEvent;
import com.pixelmonmod.pixelmon.comm.CommandChatHandler;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumPokeballs;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import com.pixelmonmod.pixelmon.storage.PixelmonStorage;
import com.pixelmonmod.pixelmon.util.RegexPatterns;
import io.netty.buffer.ByteBuf;
import ipixelmon.PixelmonUtility;
import ipixelmon.iPixelmon;
import ipixelmon.mysql.DeleteForm;
import ipixelmon.pixelbay.Pixelbay;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
        pixelmonData = new PixelmonData();
        pixelmonData.decodeInto(buf);
        seller = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        price = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        new PixelmonData(getEntityPokemon()).encodeInto(buf);
        ByteBufUtils.writeUTF8String(buf, seller.toString());
        buf.writeInt(price);
    }

    public static class Handler implements IMessageHandler<PacketBuyPokemon, IMessage>
    {

        @Override
        public IMessage onMessage(PacketBuyPokemon message, MessageContext ctx)
        {
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

                if (message.price > PixelmonUtility.getServerBalance(player.getUniqueID()))
                {
                    throw new Exception("Insufficient PokéDollars.");
                }

                EntityPixelmon pokemon = (EntityPixelmon) PixelmonEntityList.createEntityByName(message.pixelmonData.name, player.worldObj);
                pokemon.setHealth(message.pixelmonData.health);
                pokemon.setIsShiny(message.pixelmonData.isShiny);
                pokemon.setForm(message.pixelmonData.form);
                pokemon.getLvl().setLevel(message.pixelmonData.lvl);
                pokemon.caughtBall = EnumPokeballs.PokeBall;
                pokemon.friendship.initFromCapture();
                PixelmonStorage.PokeballManager.getPlayerStorage(player).addToParty(pokemon);
                PixelmonAchievements.pokedexChieves(player);
                Pixelmon.EVENT_BUS.post(new PixelmonRecievedEvent(player, ReceiveType.Command, pokemon));

                PixelmonUtility.takeMoney(player.getUniqueID(), message.price);
                PixelmonUtility.giveMoney(message.seller, message.price);

                iPixelmon.mysql.delete(Pixelbay.class, new DeleteForm("Pokemon")
                        .add("seller", message.seller.toString())
                        .add("name", message.pixelmonData.name)
                        .add("isShiny", message.pixelmonData.isShiny)
                        .add("lvl", message.pixelmonData.lvl)
                        .add("xp", message.pixelmonData.xp)
                        .add("price", message.price));

                throw new Exception("Pokémon purchased.");
            } catch (Exception e)
            {
                player.addChatComponentMessage(new ChatComponentText(e.getMessage()));
            }
            return null;
        }
    }

    @SideOnly(Side.CLIENT)
    private EntityPixelmon getEntityPokemon()
    {
        EntityPixelmon pokemon = (EntityPixelmon) PixelmonEntityList.createEntityByName(pixelmonData.name, Minecraft.getMinecraft().theWorld);
        pokemon.setIsShiny(pixelmonData.isShiny);
        pokemon.setForm(pixelmonData.form);
        pokemon.getLvl().setLevel(pixelmonData.lvl);
        pokemon.caughtBall = EnumPokeballs.PokeBall;
        pokemon.friendship.initFromCapture();
        return pokemon;
    }
}
