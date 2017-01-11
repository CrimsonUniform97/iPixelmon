package com.ipixelmon.gym.packet;

import com.ipixelmon.gym.Gym;
import com.ipixelmon.gym.GymAPI;
import com.ipixelmon.iPixelmon;
import io.netty.buffer.ByteBuf;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketOpenGymGuiToServer implements IMessage {
    public PacketOpenGymGuiToServer() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    public static class Handler implements IMessageHandler<PacketOpenGymGuiToServer, IMessage> {

        @Override
        public IMessage onMessage(PacketOpenGymGuiToServer message, MessageContext ctx) {
            Gym gym = GymAPI.Server.getGym(ctx.getServerHandler().playerEntity.getPosition());

            if(gym == null) {
                ctx.getServerHandler().playerEntity.addChatComponentMessage(new ChatComponentText("No gym found."));
            } else {
                iPixelmon.network.sendTo(new PacketOpenGymGuiToClient(gym), ctx.getServerHandler().playerEntity);
            }

            return null;
        }

    }
}
