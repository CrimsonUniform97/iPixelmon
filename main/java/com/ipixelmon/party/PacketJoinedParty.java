package com.ipixelmon.party;

import com.ipixelmon.tablet.apps.party.PartyApp;
import com.ipixelmon.tablet.notification.SimpleTextNotification;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Created by colbymchenry on 12/27/16.
 */
public class PacketJoinedParty implements IMessage {

    public PacketJoinedParty() {
    }

    private String player;

    public PacketJoinedParty(String player) {
        this.player = player;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        player = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, player);
    }

    public static class Handler implements IMessageHandler<PacketJoinedParty, IMessage> {
        // TODO: When player leaves server remove from party
        @Override
        public IMessage onMessage(PacketJoinedParty message, MessageContext ctx) {
            PartyApp.playersInParty.add(message.player);
            new SimpleTextNotification(message.player + " joined the party.");
            return null;
        }

    }
}
