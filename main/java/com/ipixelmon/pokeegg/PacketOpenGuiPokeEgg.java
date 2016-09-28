package com.ipixelmon.pokeegg;

import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumPokeballs;
import io.netty.buffer.ByteBuf;
import com.ipixelmon.pokeegg.client.GuiPokeEgg;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketOpenGuiPokeEgg implements IMessage
{

    public PacketOpenGuiPokeEgg()
    {
    }

    private PixelmonData pokeData;

    public PacketOpenGuiPokeEgg(PixelmonData pokeData)
    {
        this.pokeData = pokeData;
    }

    @Override
    public void fromBytes(final ByteBuf buf)
    {
        pokeData = new PixelmonData();
        pokeData.decodeInto(buf);
    }

    @Override
    public void toBytes(final ByteBuf buf)
    {
        pokeData.encodeInto(buf);
    }

    public static class Handler implements IMessageHandler<PacketOpenGuiPokeEgg, IMessage>
    {

        @Override
        public IMessage onMessage(final PacketOpenGuiPokeEgg message, final MessageContext ctx)
        {
            openGui(message);
            return null;
        }
    }

    @SideOnly(Side.CLIENT)
    private static void openGui(final PacketOpenGuiPokeEgg message)
    {
        Minecraft.getMinecraft().addScheduledTask(new Runnable()
        {
            @Override
            public void run()
            {
                EntityPixelmon pokemon = (EntityPixelmon) PixelmonEntityList.createEntityByName(message.pokeData.name, Minecraft.getMinecraft().thePlayer.worldObj);
                pokemon.setHealth(message.pokeData.health);
                pokemon.setIsShiny(message.pokeData.isShiny);
                pokemon.setForm(message.pokeData.form);
                pokemon.getLvl().setLevel(message.pokeData.lvl);
                pokemon.caughtBall = EnumPokeballs.PokeBall;
                pokemon.friendship.initFromCapture();
                Minecraft.getMinecraft().displayGuiScreen(new GuiPokeEgg(pokemon));
            }
        });
    }
}
