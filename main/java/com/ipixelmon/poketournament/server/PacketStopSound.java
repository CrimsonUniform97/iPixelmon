package com.ipixelmon.poketournament.server;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketStopSound implements IMessage {

    // TODO: Move the Stop and Start sound to the main iPixelmon package

    private String soundFile;
    private SoundCategory soundCategory;

    public PacketStopSound() {
    }

    public PacketStopSound(String soundFile, SoundCategory soundCategory) {
        this.soundFile = soundFile;
        this.soundCategory = soundCategory;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        soundFile = ByteBufUtils.readUTF8String(buf);
        soundCategory = SoundCategory.valueOf(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, soundFile);
        ByteBufUtils.writeUTF8String(buf, soundCategory.name());
    }

    public static class Handler implements IMessageHandler<PacketStopSound, IMessage> {

        @Override
        public IMessage onMessage(PacketStopSound message, MessageContext ctx) {
            doMessage(message);
            return null;
        }

        @SideOnly(Side.CLIENT)
        public void doMessage(PacketStopSound message) {
            // TODO: Not working... Sadly
        }

    }

}
