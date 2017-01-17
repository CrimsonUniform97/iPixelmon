package com.ipixelmon.notification;

import com.ipixelmon.iPixelmon;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by colby on 11/13/2016.
 */
public class PacketNotification implements IMessage {

    public PacketNotification() {
    }

    private String text;

    public PacketNotification(String text) {
        this.text = text;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        text = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, text);
    }

    public static class Handler implements IMessageHandler<PacketNotification, IMessage> {

        @Override
        public IMessage onMessage(PacketNotification message, MessageContext ctx) {

            doMessage(message);

            return null;
        }

        @SideOnly(Side.CLIENT)
        private void doMessage(PacketNotification message) {
            NotificationMod.newSimpleNotification(message.text, 5);
        }
    }

    @SideOnly(Side.SERVER)
    public static final void sendToPlayer(EntityPlayerMP player, String text) {
        iPixelmon.network.sendTo(new PacketNotification(text), player);
    }
}
