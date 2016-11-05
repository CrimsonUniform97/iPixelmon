package com.ipixelmon.tablet.client.apps.friends;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.Set;

/**
 * Created by colbymchenry on 11/4/16.
 */
public class PacketFriendsListRes implements IMessage {

    public PacketFriendsListRes() {
    }

    private Set<Friend> friends;

    public PacketFriendsListRes(Set<Friend> friends) {
        this.friends = friends;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        for (String s : ByteBufUtils.readUTF8String(buf).split(";"))
            if (s.contains(","))
                friends.add(Friend.fromString(s));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        String s = "";

        for (Friend friend : friends) s += friend.toString() + ";";

        ByteBufUtils.writeUTF8String(buf, s);
    }

    public static class Handler implements IMessageHandler<PacketFriendsListRes, IMessage> {

        @Override
        public IMessage onMessage(PacketFriendsListRes message, MessageContext ctx) {

            Friends.friends = message.friends;

            return null;
        }

    }
}
