package com.ipixelmon.gym.packet;

import com.ipixelmon.gym.Gym;
import com.ipixelmon.gym.GymAPI;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.team.EnumTeam;
import com.ipixelmon.team.TeamMod;
import com.ipixelmon.util.PixelmonAPI;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.storage.PixelmonStorage;
import com.pixelmonmod.pixelmon.storage.PlayerNotLoadedException;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class PacketJoinGym implements IMessage{

    private UUID gymID;
    private int[] pixelmonID;

    public PacketJoinGym() {
    }

    public PacketJoinGym(UUID gymID, int[] pixelmonID) {
        this.gymID = gymID;
        this.pixelmonID = pixelmonID;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        gymID = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        pixelmonID = new int[2];
        pixelmonID[0] = buf.readInt();
        pixelmonID[1] = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, gymID.toString());
        buf.writeInt(pixelmonID[0]);
        buf.writeInt(pixelmonID[1]);
    }

    public static class Handler implements IMessageHandler<PacketJoinGym, IMessage> {

        @Override
        public IMessage onMessage(PacketJoinGym message, MessageContext ctx) {
            Gym gym = GymAPI.Server.getGym(message.gymID);
            if(gym == null) return null;

            EntityPlayerMP player = ctx.getServerHandler().playerEntity;

            if(gym.getTeam() != EnumTeam.None && TeamMod.getPlayerTeam(player.getUniqueID()) != gym.getTeam()) {
                player.addChatComponentMessage(new TextComponentString("You are not a " + gym.getTeam().name() + " member."));
                return null;
            }

            if(PixelmonAPI.Server.getPixelmon(player, true).size() == 1) return null;

            try {
                EntityPixelmon entityPixelmon = PixelmonStorage.pokeBallManager.getPlayerStorage(player).get()
                        .getPokemon(message.pixelmonID, player.worldObj);

                if(gym.getAvailableSeats() <= 0) return null;

                gym.addTrainer(player.getUniqueID(), entityPixelmon);

                PixelmonAPI.Server.removePixelmon(entityPixelmon, player);

                iPixelmon.proxy.getDefaultWorld().getMinecraftServer().addScheduledTask(new Runnable() {
                    @Override
                    public void run() {
                        gym.reloadLivingEntities();
                    }
                });

                player.closeScreen();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

    }

}
