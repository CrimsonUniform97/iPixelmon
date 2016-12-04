package com.ipixelmon.tablet.client.apps.mail.packets;

import com.ipixelmon.tablet.client.apps.mail.Conversation;
import com.ipixelmon.tablet.client.apps.mail.Mail;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by colby on 12/3/2016.
 */
public class PacketReceiveMessage implements IMessage {

    public PacketReceiveMessage() {
    }

    private UUID participant1, participant2;
    private String message;
    private boolean isNew = false;
    private int senderNumber = 0;

    public PacketReceiveMessage(UUID participant1, UUID participant2, int senderNumber, String message, boolean isNew) {
        this.participant1 = participant1;
        this.participant2 = participant2;
        this.senderNumber = senderNumber;
        this.message = message;
        this.isNew = isNew;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        participant1 = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        participant2 = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        message = ByteBufUtils.readUTF8String(buf);
        isNew = buf.readBoolean();
        senderNumber = buf.readInt();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, participant1.toString());
        ByteBufUtils.writeUTF8String(buf, participant2.toString());
        ByteBufUtils.writeUTF8String(buf, message);
        buf.writeBoolean(isNew);
        buf.writeInt(senderNumber);
    }

    public static class Handler implements IMessageHandler<PacketReceiveMessage, IMessage> {

        @Override
        public IMessage onMessage(PacketReceiveMessage message, MessageContext ctx) {

            if(message.isNew) {
                try {
                    Mail.conversations.add(new Conversation(message.participant1, message.participant2));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                for(Conversation conversation : Mail.conversations) {
                    if(conversation.getParticipant1().equals(message.participant1)
                            && conversation.getParticipant2().equals(message.participant2)) {
                            conversation.addMessage(message.senderNumber == 1 ? message.participant1 : message.participant2, message.message);
                    }
                }
            }

            return null;
        }

    }
}
