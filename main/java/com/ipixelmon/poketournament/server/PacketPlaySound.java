package com.ipixelmon.poketournament.server;

import com.ipixelmon.iPixelmon;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketPlaySound implements IMessage {

    private String soundFile;

    public PacketPlaySound() {
    }

    public PacketPlaySound(String soundFile) {
        this.soundFile = soundFile;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        soundFile = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, soundFile);
    }

    public static class Handler implements IMessageHandler<PacketPlaySound, IMessage> {

        @Override
        public IMessage onMessage(PacketPlaySound message, MessageContext ctx) {
            doMessage(message);
            return null;
        }

        @SideOnly(Side.CLIENT)
        public void doMessage(PacketPlaySound message) {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            player.playSound(new SoundEvent(new ResourceLocation(iPixelmon.id + ":" + message.soundFile)),
                    0.5F, 1.0F);
    }

}

}
