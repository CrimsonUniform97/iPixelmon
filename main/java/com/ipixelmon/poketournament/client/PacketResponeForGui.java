package com.ipixelmon.poketournament.client;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketResponeForGui implements IMessage {

    private String msg;

    public PacketResponeForGui() {
    }

    public PacketResponeForGui(String msg) {
        this.msg = msg;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        msg = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, msg);
    }

    public static class Handler implements IMessageHandler<PacketResponeForGui, IMessage> {

        @Override
        public IMessage onMessage(PacketResponeForGui message, MessageContext ctx) {
            doMessage(message);
            return null;
        }

        @SideOnly(Side.CLIENT)
        public void doMessage(PacketResponeForGui message) {
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    if (Minecraft.getMinecraft().currentScreen != null) {
                        if (Minecraft.getMinecraft().currentScreen instanceof TournamentGui) {
                            ((TournamentGui) Minecraft.getMinecraft().currentScreen).getTimedMessage().setMessage(message.msg, 5);
                        }
                    }
                }
            });
        }
    }
}
