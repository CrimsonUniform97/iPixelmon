package ipixelmon.pixelbay.gui.sell;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.Remove;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumPokeballs;
import com.pixelmonmod.pixelmon.storage.PCServer;
import com.pixelmonmod.pixelmon.storage.PixelmonStorage;
import io.netty.buffer.ByteBuf;
import ipixelmon.iPixelmon;
import ipixelmon.pixelbay.Pixelbay;
import ipixelmon.mysql.InsertForm;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public final class PacketSellPokemon implements IMessage {

    public PacketSellPokemon() {
    }

    private PixelmonData pokeData;
    private int price;

    public PacketSellPokemon(final PixelmonData pixelmonData, final int price) {
        this.pokeData = pixelmonData;
        this.price = price;
    }

    @Override
    public final void fromBytes(final ByteBuf buf) {
        pokeData = new PixelmonData();
        pokeData.decodeInto(buf);
        price = buf.readInt();
    }

    @Override
    public final void toBytes(final ByteBuf buf) {
        new PixelmonData(getEntityPokemon()).encodeInto(buf);
        buf.writeInt(price);
    }

    public static final class Handler implements IMessageHandler<PacketSellPokemon, IMessage> {

        @Override
        public final IMessage onMessage(final PacketSellPokemon message, final MessageContext ctx) {
            final EntityPlayerMP player = ctx.getServerHandler().playerEntity;

            if (player == null) return null;

            try {
                if(PixelmonStorage.PokeballManager.getPlayerStorage(player).count() == 1) throw new Exception("You cannot sell your only items.");

                final EntityPixelmon pixelmon = PixelmonStorage.PokeballManager.getPlayerStorage(player).getPokemon(message.pokeData.pokemonID, player.worldObj);

                if (pixelmon == null) throw new Exception("That pokemon is null.");

                if (!pixelmon.getOwner().getUniqueID().equals(player.getUniqueID()))
                    throw new Exception("You are not the owner");

                final InsertForm pokeForm = new InsertForm("Pokemon");
                pokeForm.add("seller", player.getUniqueID().toString());
                pokeForm.add("name", message.pokeData.name);
                pokeForm.add("isShiny", "" + message.pokeData.isShiny);
                pokeForm.add("lvl", "" + message.pokeData.lvl);
                pokeForm.add("xp", "" + message.pokeData.xp);
                pokeForm.add("price", "" + message.price);


                iPixelmon.mysql.insert(Pixelbay.class, pokeForm);

                MinecraftServer.getServer().addScheduledTask(new Runnable() {
                    @Override
                    public void run() {
                        PCServer.deletePokemon(player, -1, message.pokeData.order);
                        Pixelmon.network.sendTo(new Remove(message.pokeData.pokemonID), player);
                        iPixelmon.network.sendTo(new PacketSellResponse(message.pokeData), player);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                if (player != null)
                    player.addChatComponentMessage(new ChatComponentText(e.getMessage()));
            }
            return null;
        }

    }

    @SideOnly(Side.CLIENT)
    private EntityPixelmon getEntityPokemon()
    {
        EntityPixelmon pokemon = (EntityPixelmon) PixelmonEntityList.createEntityByName(pokeData.name, Minecraft.getMinecraft().theWorld);
        pokemon.setIsShiny(pokeData.isShiny);
        pokemon.setForm(pokeData.form);
        pokemon.getLvl().setLevel(pokeData.lvl);
        pokemon.caughtBall = EnumPokeballs.PokeBall;
        pokemon.friendship.initFromCapture();
        pokemon.setPokemonId(pokeData.pokemonID);
        return pokemon;
    }
}
