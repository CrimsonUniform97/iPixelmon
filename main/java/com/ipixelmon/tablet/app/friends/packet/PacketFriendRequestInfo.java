package com.ipixelmon.tablet.app.friends.packet;

import com.ipixelmon.notification.SimpleTextNotification;
import com.ipixelmon.tablet.app.friends.FriendRequest;
import com.ipixelmon.tablet.app.friends.FriendRequestNotification;
import com.ipixelmon.tablet.app.friends.FriendsAPI;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by colby on 1/5/2017.
 */
public class PacketFriendRequestInfo implements IMessage {

    private FriendRequest friendRequest;
    private int response = 0;

    public PacketFriendRequestInfo() {
    }

    public PacketFriendRequestInfo(FriendRequest friendRequest) {
        this.friendRequest = friendRequest;
    }

    public PacketFriendRequestInfo(FriendRequest friendRequest, boolean accept) {
        this(friendRequest);
        this.response = (accept ? 1 : 2);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        response = buf.readInt();
        friendRequest = FriendRequest.fromBytes(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(response);
        friendRequest.toBytes(buf);
    }

    public static class Handler implements IMessageHandler<PacketFriendRequestInfo, IMessage> {

        @Override
        public IMessage onMessage(PacketFriendRequestInfo message, MessageContext ctx) {
            if(message.response == 0) {
                FriendsAPI.Client.friendRequests.add(message.friendRequest);
                new FriendRequestNotification(message.friendRequest.getSenderName() + " sent you a friend request.", 5);
            } else if(message.response == 1) {
                new FriendRequestNotification(message.friendRequest.getReceiverName() + " accepted your friend request.", 5);
                FriendsAPI.Client.friendRequests.remove(message.friendRequest);
            } else {
                FriendsAPI.Client.friendRequests.remove(message.friendRequest);
            }
            return null;
        }

    }

}
