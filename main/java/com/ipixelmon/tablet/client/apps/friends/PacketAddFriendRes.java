package com.ipixelmon.tablet.client.apps.friends;

import com.ipixelmon.tablet.notification.NotificationOverlay;
import com.ipixelmon.tablet.notification.SimpleTextNotification;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by colby on 11/5/2016.
 */
public class PacketAddFriendRes implements IMessage {

    public PacketAddFriendRes(){}

    private ResponseType responseType;
    private String player;

    public PacketAddFriendRes(ResponseType responseType, String player){
        this.responseType = responseType;
        this.player = player;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        responseType = ResponseType.values()[buf.readInt()];
        player = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(responseType.ordinal());
        ByteBufUtils.writeUTF8String(buf, player);
    }

    public static class Handler implements IMessageHandler<PacketAddFriendRes, IMessage> {

        @Override
        public IMessage onMessage(PacketAddFriendRes message, MessageContext ctx) {
            // TODO: Work on the responses
            System.out.println(message.responseType);
            switch(message.responseType) {
                case SENT:
                    new SimpleTextNotification("Friend request sent.");
                    break;
                // TODO: Make the REQUEST render the player face in the notification
                case REQUEST:
                    new SimpleTextNotification("Friend request from " + message.player + ".");
                    break;
                case PENDING:
                    new SimpleTextNotification("Friend request already pending.");
                    break;
                // TODO: Work on accepted and denied, need to add a section in gui in Friends app
            }
            return null;
        }

    }

    public enum ResponseType {
       SENT, PENDING, ACCEPTED, DENIED, REQUEST
    }
}
