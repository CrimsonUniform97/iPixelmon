package com.ipixelmon.party;

import com.ipixelmon.notification.NotificationMod;
import com.ipixelmon.notification.SimpleTextNotification;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

/**
 * Created by colbymchenry on 12/27/16.
 */
public class PacketJoinedParty implements IMessage {

    public PacketJoinedParty() {
    }

    private UUID partyID;
    private UUID playerID;
    private String playerName;

    public PacketJoinedParty(UUID playerID, String playerName, UUID partyID) {
        this.playerID = playerID;
        this.playerName = playerName;
        this.partyID = partyID;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        playerName = ByteBufUtils.readUTF8String(buf);
        playerID = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        partyID = UUID.fromString(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, playerName);
        ByteBufUtils.writeUTF8String(buf, playerID.toString());
        ByteBufUtils.writeUTF8String(buf, partyID.toString());
    }

    public static class Handler implements IMessageHandler<PacketJoinedParty, IMessage> {
        // TODO: When player leaves server remove from party
        @Override
        public IMessage onMessage(PacketJoinedParty message, MessageContext ctx) {
            if(PartyAPI.Client.getPartyID() == null || !PartyAPI.Client.getPartyID().equals(message.partyID)) {
                PartyAPI.Client.resetParty();
                PartyAPI.Client.setPartyID(message.partyID);
            }
            PartyAPI.Client.addPlayerToParty(message.playerID, message.playerName);
            NotificationMod.newSimpleNotification(message.playerName + " joined the party.", 3);
            return null;
        }

    }
}
