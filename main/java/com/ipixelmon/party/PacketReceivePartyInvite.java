package com.ipixelmon.party;

import com.ipixelmon.party.client.NotificationPartyInvite;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

/**
 * Created by colbymchenry on 12/18/16.
 */
public class PacketReceivePartyInvite implements IMessage {

    public PacketReceivePartyInvite() {}

    private UUID partyUUID;
    private String sender;

    public PacketReceivePartyInvite(UUID partyUUID, String sender) {
        this.partyUUID = partyUUID;
        this.sender = sender;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        partyUUID = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        sender = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, partyUUID.toString());
        ByteBufUtils.writeUTF8String(buf, sender);
    }

    public static class Handler implements IMessageHandler<PacketReceivePartyInvite, IMessage> {

        @Override
        public IMessage onMessage(PacketReceivePartyInvite message, MessageContext ctx) {
            new NotificationPartyInvite(message.sender, message.partyUUID);
            return null;
        }

    }

}
