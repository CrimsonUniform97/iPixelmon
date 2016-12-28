package com.ipixelmon.tablet.apps.friends;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

/**
 * Created by colbymchenry on 12/27/16.
 */
public class PacketFriendRequestToClient implements IMessage {

    public PacketFriendRequestToClient() {
    }

    private String player;
    private UUID id;

    public PacketFriendRequestToClient(String player, UUID id) {
        this.player = player;
        this.id = id;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        player = ByteBufUtils.readUTF8String(buf);
        id = UUID.fromString(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, player);
        ByteBufUtils.writeUTF8String(buf, id.toString());
    }

    public static class Handler implements IMessageHandler<PacketFriendRequestToClient, IMessage> {

        @Override
        public IMessage onMessage(PacketFriendRequestToClient message, MessageContext ctx) {
            Friends.addFriendRequest(message.id, message.player);
            return null;
        }

    }

}
