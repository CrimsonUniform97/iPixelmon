package com.ipixelmon.tablet.app.friends.packet;

import com.ipixelmon.tablet.app.friends.Friend;
import com.ipixelmon.tablet.app.friends.FriendRequestNotification;
import com.ipixelmon.tablet.app.friends.FriendsAPI;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by colby on 1/5/2017.
 */
public class PacketFriendInfo implements IMessage {

    private Friend friend;
    private int remove = 0;

    public PacketFriendInfo() {
    }

    public PacketFriendInfo(Friend friend) {
        this.friend = friend;
    }

    public PacketFriendInfo(Friend friend, boolean remove) {
        this(friend);
        this.remove = 1;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        remove = buf.readInt();
        friend = Friend.fromBytes(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(remove);
        friend.toBytes(buf);
    }

    public static class Handler implements IMessageHandler<PacketFriendInfo, IMessage> {

        @Override
        public IMessage onMessage(PacketFriendInfo message, MessageContext ctx) {

            if (message.remove == 0) {
                FriendsAPI.Client.friends.add(message.friend);
                Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                    @Override
                    public void run() {
                        if(message.friend.isOnline()) {
                            new FriendRequestNotification(message.friend.getName() + " is online!", 3);
                        }
                    }
                });
            } else {
                FriendsAPI.Client.friends.remove(message.friend);
            }
            return null;
        }
    }
}
