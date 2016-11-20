package com.ipixelmon.tablet.client.apps.mail.packet;

import com.ipixelmon.tablet.client.App;
import com.ipixelmon.tablet.client.apps.mail.Mail;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by colby on 11/20/2016.
 */
public class PacketMailConfirmation implements IMessage {

    public PacketMailConfirmation() {
    }

    private boolean failed;

    public PacketMailConfirmation(boolean failed) {
        this.failed = failed;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        failed = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(failed);
    }

    public static class Handler implements IMessageHandler<PacketMailConfirmation, IMessage> {

        @Override
        public IMessage onMessage(PacketMailConfirmation message, MessageContext ctx) {

            if(message.failed) {
                Mail.message.setMessage("An error occurred...", 5);
            } else {
                Mail.clearFields();
            }

            return null;
        }

    }

}
