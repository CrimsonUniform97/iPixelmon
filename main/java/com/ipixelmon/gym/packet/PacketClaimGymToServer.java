package com.ipixelmon.gym.packet;

import com.ipixelmon.gym.Gym;
import com.ipixelmon.gym.GymAPI;
import com.ipixelmon.tablet.app.pixelbay.PixelbayAPI;
import com.ipixelmon.util.PixelmonAPI;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.storage.PixelmonStorage;
import com.pixelmonmod.pixelmon.storage.PlayerNotLoadedException;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class PacketClaimGymToServer implements IMessage{

    private UUID gymID;
    private int[] pixelmonID;

    public PacketClaimGymToServer() {
    }

    public PacketClaimGymToServer(UUID gymID, int[] pixelmonID) {
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

    public static class Handler implements IMessageHandler<PacketClaimGymToServer, IMessage> {

        @Override
        public IMessage onMessage(PacketClaimGymToServer message, MessageContext ctx) {
            Gym gym = GymAPI.Server.getGym(message.gymID);
            if(gym == null) return null;

            EntityPlayerMP player = ctx.getServerHandler().playerEntity;

            if(PixelmonAPI.Server.getPixelmon(player, true).size() == 1) return null;

            try {
                EntityPixelmon entityPixelmon = PixelmonStorage.PokeballManager.getPlayerStorage(player)
                        .getPokemon(message.pixelmonID, player.worldObj);

                gym.addTrainer(player.getUniqueID(), entityPixelmon);

                MinecraftServer.getServer().addScheduledTask(new Runnable() {
                    @Override
                    public void run() {
                        gym.reloadLivingEntities();
                    }
                });

                PixelmonAPI.Server.removePixelmon(entityPixelmon, player);
            } catch (PlayerNotLoadedException e) {
                e.printStackTrace();
            }

            return null;
        }

    }

}
