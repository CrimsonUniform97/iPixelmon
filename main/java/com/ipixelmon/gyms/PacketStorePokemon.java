package com.ipixelmon.gyms;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.Region;
import com.ipixelmon.pixelbay.gui.sell.PacketSellResponse;
import com.ipixelmon.teams.Teams;
import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.achievement.PixelmonAchievements;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.Remove;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumPokeballs;
import com.pixelmonmod.pixelmon.storage.PCServer;
import com.pixelmonmod.pixelmon.storage.PixelmonStorage;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.Map;
import java.util.UUID;

/**
 * Created by colby on 10/10/2016.
 */
public class PacketStorePokemon implements IMessage {

    private PixelmonData pixelmonData;

    public PacketStorePokemon(PixelmonData pixelmonData) {
        this.pixelmonData = pixelmonData;
    }

    public PacketStorePokemon() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        pixelmonData = new PixelmonData();
        pixelmonData.decodeInto(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        pixelmonData.encodeInto(buf);
    }

    public static class Handler implements IMessageHandler<PacketStorePokemon, IMessage> {

        @Override
        public IMessage onMessage(final PacketStorePokemon message, MessageContext ctx) {

            ctx.getServerHandler().playerEntity.closeScreen();

            final EntityPlayerMP player = ctx.getServerHandler().playerEntity;

            // TODO: Check gyms points before adding pokemon, and it's slots

            try {
                Region region = new Region(player.worldObj, new BlockPos(player.posX, player.posY, player.posZ));

                Gym gym = new Gym(region.getUUID());

                gym.setTeam(Teams.getPlayerTeam(player.getUniqueID()));
                Map<UUID, EntityPixelmon> pixelmonMap = gym.getPokemon();

                final EntityPixelmon pixelmon = PixelmonStorage.PokeballManager.getPlayerStorage(player).getPokemon(message.pixelmonData.pokemonID, player.worldObj);
                pixelmonMap.put(player.getUniqueID(), pixelmon);
                gym.setPokemon(pixelmonMap);
                gym.updateWool();

                MinecraftServer.getServer().addScheduledTask(new Runnable() {
                    @Override
                    public void run() {
                        PCServer.deletePokemon(player, -1, message.pixelmonData.order);
                        Pixelmon.network.sendTo(new Remove(message.pixelmonData.pokemonID), player);
                        iPixelmon.network.sendTo(new PacketSellResponse(message.pixelmonData), player);
                    }
                });
            }catch(Exception e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
