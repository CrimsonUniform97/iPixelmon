package com.ipixelmon.tablet.apps.friends.packet;

import com.ipixelmon.tablet.apps.friends.Friend;
import com.ipixelmon.tablet.apps.friends.FriendRequest;
import com.ipixelmon.tablet.apps.friends.Friends;
import com.ipixelmon.tablet.apps.friends.FriendsAPI;
import com.ipixelmon.tablet.notification.SimpleTextNotification;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.Date;
import java.util.UUID;

/**
 * Created by colby on 11/5/2016.
 */
public class PacketAddFriendRes implements IMessage {

    public PacketAddFriendRes() {
    }

    private ResponseType responseType;
    private String player;

    public PacketAddFriendRes(ResponseType responseType, String player) {
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
            switch (message.responseType) {
                case SENT:
                    new SimpleTextNotification("Friend request sent.");
                    break;
                case REQUEST:
                    String[] data = message.player.split(",");
                    new SimpleTextNotification("You received a friend request from " + data[1] + ".");
                    Friends.requests.add(new FriendRequest(UUID.fromString(data[0]), new Date()));
                    break;
                case PENDING:
                    new SimpleTextNotification("Friend request already pending.");
                    break;
                case FRIENDS:
                    Friends.message.setMessage("Already friends.", 5);
                    break;
                case UPDATE:
                    FriendsAPI.populateFriendRequests();
                    FriendsAPI.getFriends(true);
                    break;
                case SELF:
                    Friends.message.setMessage("You cannot add yourself.", 5);
                    break;
                case NOTFOUND:
                    Friends.message.setMessage("Player not found.", 5);
                    break;
                case ACCEPT:
                    data = message.player.split(",");
                    new SimpleTextNotification(data[1] + " accepted your friend request.");
                    Friends.friends.add(new Friend(UUID.fromString(data[0]), data[1], true));
                    break;
                case DENY:
                    data = message.player.split(",");
                    new SimpleTextNotification(data[1] + " denied your friend request.");
                    break;
                case REMOVE:
                    data = message.player.split(",");
                    Friends.friends.remove(new Friend(UUID.fromString(data[0]), data[1], true));
                    break;
            }
            return null;
        }

    }

    public enum ResponseType {
        SENT, PENDING, REQUEST, FRIENDS, UPDATE, SELF, NOTFOUND, ACCEPT, DENY, REMOVE
    }
}
