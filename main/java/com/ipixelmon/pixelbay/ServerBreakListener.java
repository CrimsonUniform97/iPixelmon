package com.ipixelmon.pixelbay;

import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import com.pixelmonmod.pixelmon.storage.PixelmonStorage;
import com.pixelmonmod.pixelmon.storage.PlayerNotLoadedException;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ServerBreakListener {

    @SubscribeEvent
    public void onBreak(BlockEvent.BreakEvent event) {
        try {
            EntityPlayerMP player = (EntityPlayerMP) event.getPlayer();
            EntityPixelmon pokemon = (EntityPixelmon) PixelmonEntityList.createEntityByName(EnumPokemon.Bayleef.name, player.worldObj);
            pokemon.getLvl().setLevel(80);
            PixelmonStorage.PokeballManager.getPlayerStorage(player).addToParty(pokemon);
            player.addChatComponentMessage(new ChatComponentText("You now have a " + pokemon.getName()));
        }catch(PlayerNotLoadedException e) {
            e.printStackTrace();
        }
    }

}
