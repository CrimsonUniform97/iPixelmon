package com.ipixelmon.party;

import com.ipixelmon.notification.SimpleTextNotification;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

/**
 * Created by colbymchenry on 12/28/16.
 */
public class PacketLeavePartyToClient implements IMessage {

    private String player;
    private UUID playerID;

    public PacketLeavePartyToClient() {
    }

    public PacketLeavePartyToClient(String player, UUID playerID) {
        this.player= player;
        this.playerID = playerID;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        player = ByteBufUtils.readUTF8String(buf);
        playerID = UUID.fromString(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, player);
        ByteBufUtils.writeUTF8String(buf, playerID.toString());

        PartyAPI.Server.removePlayerFromParty(PartyAPI.Server.getPlayersParty(playerID), playerID);
    }

    public static class Handler implements IMessageHandler<PacketLeavePartyToClient, IMessage> {

        @Override
        public IMessage onMessage(PacketLeavePartyToClient message, MessageContext ctx) {
            PartyAPI.Client.removePlayerFromParty(message.playerID);
            new SimpleTextNotification(message.player + " left the party.");
            return null;
        }

    }

}
