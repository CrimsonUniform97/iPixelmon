package com.ipixelmon.tablet.client.apps.friends;

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
            return null;
        }

    }

    public enum ResponseType {
       SENT, PENDING, ACCEPTED, DENIED, REQUEST;
    }
}
