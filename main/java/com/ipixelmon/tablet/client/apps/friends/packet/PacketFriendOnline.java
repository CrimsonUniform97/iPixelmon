package com.ipixelmon.tablet.client.apps.friends.packet;

import com.ipixelmon.tablet.notification.SimpleTextNotification;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

/**
 * Created by colbymchenry on 11/9/16.
 */
public class PacketFriendOnline implements IMessage {

    public PacketFriendOnline(){}

    private UUID uuid;
    private String name;

    public PacketFriendOnline(UUID uuid, String name){
        this.uuid = uuid;
        this.name = name;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        String[] data = ByteBufUtils.readUTF8String(buf).split(",");
        name = data[0];
        uuid = UUID.fromString(data[1]);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, name + "," + uuid.toString());
    }

    public static class Handler implements IMessageHandler<PacketFriendOnline, IMessage> {

        @Override
        public IMessage onMessage(PacketFriendOnline message, MessageContext ctx) {
            if(message.name != null && message.uuid != null) new SimpleTextNotification(message.name + " is online.");
            return null;
        }

    }

}
