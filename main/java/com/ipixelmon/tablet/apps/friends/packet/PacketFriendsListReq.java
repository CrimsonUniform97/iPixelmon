package com.ipixelmon.tablet.apps.friends.packet;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.apps.friends.Friend;
import com.ipixelmon.tablet.apps.friends.FriendsAPI;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.Set;

/**
 * Created by colbymchenry on 11/4/16.
 */
public class PacketFriendsListReq implements IMessage {

    public PacketFriendsListReq() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    public static class Handler implements IMessageHandler<PacketFriendsListReq, IMessage> {

        @Override
        public IMessage onMessage(PacketFriendsListReq message, MessageContext ctx) {
            Set<Friend> friends = FriendsAPI.getFriends(ctx.getServerHandler().playerEntity.getUniqueID());
            iPixelmon.network.sendTo(new PacketFriendsListRes(friends), ctx.getServerHandler().playerEntity);
            return null;
        }
    }
}
