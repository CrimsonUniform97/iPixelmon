package com.ipixelmon.tablet.app.friends.packet;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.app.friends.Friend;
import com.ipixelmon.tablet.app.friends.FriendRequest;
import com.ipixelmon.tablet.app.friends.FriendsAPI;
import com.ipixelmon.util.PlayerUtil;
import com.ipixelmon.uuidmanager.UUIDManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

/**
 * Created by colby on 1/5/2017.
 */
public class PacketFriendRequestToServer implements IMessage {

    private String player;
    private FriendRequest friendRequest;
    private boolean accept;

    public PacketFriendRequestToServer() {
    }

    public PacketFriendRequestToServer(String player) {
        this.player = player;
    }

    public PacketFriendRequestToServer(FriendRequest friendRequest, boolean accept) {
        this.friendRequest = friendRequest;
        this.accept = accept;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        boolean isAcceptorDenyRequest = buf.readBoolean();

        if(!isAcceptorDenyRequest) {
            player = ByteBufUtils.readUTF8String(buf);
        } else {
            accept = buf.readBoolean();
            friendRequest = FriendRequest.fromBytes(buf);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        boolean isAcceptorDenyRequest = friendRequest != null;

        buf.writeBoolean(isAcceptorDenyRequest);

        if(!isAcceptorDenyRequest) {
            ByteBufUtils.writeUTF8String(buf, player);
        } else {
            buf.writeBoolean(accept);
            friendRequest.toBytes(buf);
        }
    }

    public static class Handler implements IMessageHandler<PacketFriendRequestToServer, IMessage> {

        @Override
        public IMessage onMessage(PacketFriendRequestToServer message, MessageContext ctx) {
            UUID senderUUID = ctx.getServerHandler().playerEntity.getUniqueID();

            if(message.friendRequest == null) {
                /**
                 * send a friend request
                 */

                UUID receiverUUID = UUIDManager.getUUID(message.player);

                if (receiverUUID == null) return null;

                FriendRequest friendRequest = new FriendRequest(
                        senderUUID, receiverUUID,
                        ctx.getServerHandler().playerEntity.getName(), UUIDManager.getPlayerName(receiverUUID));

                if (!FriendsAPI.Server.storeFriendRequest(friendRequest)) {
                    // TODO: Send message back to player for all the return nulls
                    return null;
                }

                if (PlayerUtil.isPlayerOnline(receiverUUID)) {
                    iPixelmon.network.sendTo(new PacketFriendRequestInfo(friendRequest), PlayerUtil.getPlayer(receiverUUID));
                }
            } else {
                /**
                 * respond to a friend request
                 */

                FriendRequest friendRequest = message.friendRequest;
                boolean acceptFriendRequest = message.accept;

                if(acceptFriendRequest) {
                    FriendsAPI.Server.makeFriends(friendRequest);
                    EntityPlayerMP sender = PlayerUtil.getPlayer(friendRequest.getSenderUUID());
                    EntityPlayerMP receiver = PlayerUtil.getPlayer(friendRequest.getReceiverUUID());

                    // send friend info to sender
                    if(sender != null) {
                        iPixelmon.network.sendTo(new PacketFriendRequestInfo(friendRequest, true), sender);
                        iPixelmon.network.sendTo(new PacketFriendInfo(new Friend(friendRequest.getReceiverName(),
                                friendRequest.getReceiverUUID(), receiver != null)), sender);
                    }

                    // send friend info to receiver
                    if(receiver != null) {
                        iPixelmon.network.sendTo(new PacketFriendRequestInfo(friendRequest, true), receiver);
                        iPixelmon.network.sendTo(new PacketFriendInfo(new Friend(friendRequest.getSenderName(),
                                friendRequest.getSenderUUID(), sender != null)), receiver);
                    }
                }

                FriendsAPI.Server.deleteFriendRequest(friendRequest);
            }
            return null;
        }

    }
}
