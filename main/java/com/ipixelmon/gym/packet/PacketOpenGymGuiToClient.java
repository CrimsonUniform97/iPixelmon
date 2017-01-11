package com.ipixelmon.gym.packet;

import com.ipixelmon.gym.Gym;
import com.ipixelmon.gym.client.GuiGymInfo;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketOpenGymGuiToClient implements IMessage {

    private Gym gym;

    public PacketOpenGymGuiToClient() {
    }

    public PacketOpenGymGuiToClient(Gym gym) {
        this.gym = gym;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        gym = Gym.fromBytes(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        gym.toBytes(buf);
    }

    public static class Handler implements IMessageHandler<PacketOpenGymGuiToClient, IMessage> {

        @Override
        public IMessage onMessage(PacketOpenGymGuiToClient message, MessageContext ctx) {
            onMessage(message);
            return null;
        }

        @SideOnly(Side.CLIENT)
        private void onMessage(PacketOpenGymGuiToClient message) {
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    Minecraft.getMinecraft().displayGuiScreen(new GuiGymInfo(message.gym));
                }
            });
        }

    }

}
