package com.ipixelmon.tablet.apps.mail.packet;

import com.ipixelmon.tablet.apps.mail.ComposeMail;
import com.ipixelmon.tablet.apps.mail.ViewMail;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by colbymchenry on 12/17/16.
 */
public class PacketSendResponse implements IMessage {

    public PacketSendResponse() {}

    private boolean successful;
    private String message;

    public PacketSendResponse(boolean successful, String message) {
        this.successful = successful;
        this.message = message;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        successful = buf.readBoolean();
        message = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(successful);
        ByteBufUtils.writeUTF8String(buf, message);
    }

    // TODO: Test response
    public static class Handler implements IMessageHandler<PacketSendResponse, IMessage> {

        @Override
        public IMessage onMessage(PacketSendResponse message, MessageContext ctx) {

            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    if(Minecraft.getMinecraft().currentScreen instanceof ComposeMail) {
                        ComposeMail composeMail = (ComposeMail) Minecraft.getMinecraft().currentScreen;
                        composeMail.handleResponse(message.successful, message.message);
                    } else if (Minecraft.getMinecraft().currentScreen instanceof ViewMail) {
                        ViewMail viewMail = (ViewMail) Minecraft.getMinecraft().currentScreen;
                        viewMail.handleResponse(message.successful, message.message);
                    }
                }
            });

            return null;
        }


    }

}
