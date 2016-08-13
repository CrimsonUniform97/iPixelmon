package ipixelmon.minebay.gui.sell;

import com.pixelmonmod.pixelmon.client.ServerStorageDisplay;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.pokedex.EnumPokedexRegisterStatus;
import com.pixelmonmod.pixelmon.pokedex.Pokedex;
import com.pixelmonmod.pixelmon.storage.PCServer;
import com.pixelmonmod.pixelmon.storage.PixelmonStorage;
import com.pixelmonmod.pixelmon.storage.PlayerStorage;
import io.netty.buffer.ByteBuf;
import ipixelmon.iPixelmon;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.Optional;

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
        this.pokeData = new PixelmonData();
        pokeData.decodeInto(buf);

        price = Integer.parseInt(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public final void toBytes(final ByteBuf buf) {
        pokeData.encodeInto(buf);
        ByteBufUtils.writeUTF8String(buf, "" + price);
    }

    public static final class Handler implements IMessageHandler<PacketSellPokemon, IMessage> {

        @Override
        public final IMessage onMessage(final PacketSellPokemon message, final MessageContext ctx) {
            final EntityPlayerMP player = ctx.getServerHandler().playerEntity;

            if (player == null) return null;

            try {
                if(PixelmonStorage.PokeballManager.getPlayerStorage(player).count() == 1) throw new Exception("You cannot sell your only pokemon.");

                final EntityPixelmon pixelmon = PixelmonStorage.PokeballManager.getPlayerStorage(player).getPokemon(message.pokeData.pokemonID, player.worldObj);

                if (pixelmon == null) throw new Exception("That pokemon is null.");

                if (!pixelmon.getOwner().getUniqueID().equals(player.getUniqueID()))
                    throw new Exception("You are not the owner");

//                iPixelmon.mys

                MinecraftServer.getServer().addScheduledTask(() -> {
                    PCServer.deletePokemon(player, -1, message.pokeData.order);
                });
            } catch (Exception e) {
                e.printStackTrace();
                if (player != null)
                    player.addChatComponentMessage(new ChatComponentText(e.getMessage()));
            }
            return null;
        }

    }
}
