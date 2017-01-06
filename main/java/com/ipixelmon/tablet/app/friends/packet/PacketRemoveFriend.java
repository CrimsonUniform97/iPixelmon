package com.ipixelmon.tablet.app.friends.packet;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.app.friends.Friend;
import com.ipixelmon.tablet.app.friends.FriendsAPI;
import com.ipixelmon.util.PlayerUtil;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

/**
 * Created by colby on 1/6/2017.
 */
public class PacketRemoveFriend implements IMessage {

    private Friend friend;

    public PacketRemoveFriend() {
    }

    public PacketRemoveFriend(Friend friend) {

        this.friend = friend;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        friend = Friend.fromBytes(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        friend.toBytes(buf);
    }

    public static class Handler implements IMessageHandler<PacketRemoveFriend, IMessage> {

        @Override
        public IMessage onMessage(PacketRemoveFriend message, MessageContext ctx) {
            UUID friendID = message.friend.getUUID();
            UUID playerID = ctx.getServerHandler().playerEntity.getUniqueID();
            System.out.println("CALLED1");
            if(!FriendsAPI.Server.areFriends(friendID, playerID)) return null;
            System.out.println("CALLED");
            FriendsAPI.Server.removeFriend(friendID, playerID);
            FriendsAPI.Server.removeFriend(playerID, friendID);

            EntityPlayerMP friend = PlayerUtil.getPlayer(message.friend.getUUID());

            if(friend != null)
                iPixelmon.network.sendTo(new PacketFriendInfo(
                        new Friend(ctx.getServerHandler().playerEntity.getName(), playerID, true), true), friend);

            iPixelmon.network.sendTo(new PacketFriendInfo(message.friend, true), ctx.getServerHandler().playerEntity);
            return null;
        }

    }

}
