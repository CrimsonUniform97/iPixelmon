package com.ipixelmon.gym.packet;

import com.ipixelmon.gym.Gym;
import com.ipixelmon.gym.GymAPI;
import com.ipixelmon.iPixelmon;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

public class PacketBattle implements IMessage {

    private Gym gym;

    public PacketBattle() {
    }

    public PacketBattle(Gym gym) {
        this.gym = gym;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        gym = GymAPI.Server.getGym(UUID.fromString(ByteBufUtils.readUTF8String(buf)));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, gym.getID().toString());
    }

    public static class Handler implements IMessageHandler<PacketBattle, IMessage> {

        @Override
        public IMessage onMessage(PacketBattle message, MessageContext ctx) {
            onMessage(message, ctx.getServerHandler().playerEntity);
            ctx.getServerHandler().playerEntity.closeScreen();
            return null;
        }

        @SideOnly(Side.SERVER)
        public void onMessage(PacketBattle message, EntityPlayerMP player) {


            iPixelmon.proxy.getDefaultWorld().getMinecraftServer().addScheduledTask(new Runnable() {
                @Override
                public void run() {

                    if(message.gym.getQue().contains(player.getUniqueID())) {
                        player.addChatComponentMessage(new TextComponentString("You are already in que."));
                        return;
                    }

                    message.gym.battle(player);
                }
            });
        }

    }

}
