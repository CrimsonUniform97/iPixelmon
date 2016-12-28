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
public class PacketFriendStatus implements IMessage {

    public PacketFriendStatus() {
    }

    private UUID id;
    private String name;
    private boolean online;

    public PacketFriendStatus(UUID id, String name, boolean online) {
        this.id = id;
        this.name = name;
        this.online = online;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        id = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        name = ByteBufUtils.readUTF8String(buf);
        online = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, id.toString());
        ByteBufUtils.writeUTF8String(buf, name);
        buf.writeBoolean(online);
    }

    public static class Handler implements IMessageHandler<PacketFriendStatus, IMessage> {

        @Override
        public IMessage onMessage(PacketFriendStatus message, MessageContext ctx) {
            Friends.addFriend(message.id, message.name, message.online);
            return null;
        }

    }

}
