package com.ipixelmon.party;

import com.ipixelmon.PlayerUtil;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.notification.PacketNotification;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

/**
 * Created by colbymchenry on 12/18/16.
 */
public class PacketAcceptPartyInvite implements IMessage {

    public PacketAcceptPartyInvite() {
    }

    private UUID partyID;

    public PacketAcceptPartyInvite(UUID partyID) {
        this.partyID = partyID;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        partyID = UUID.fromString(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, partyID.toString());
    }

    public static class Handler implements IMessageHandler<PacketAcceptPartyInvite, IMessage> {
// TODO: Make players be able to view the players in their party.
        @Override
        public IMessage onMessage(PacketAcceptPartyInvite message, MessageContext ctx) {

            for (UUID player : PartyMod.getPlayersInParty(message.partyID)) {
                if (PlayerUtil.isPlayerOnline(player) && !player.equals(ctx.getServerHandler().playerEntity.getUniqueID())) {
                    iPixelmon.network.sendTo(new PacketNotification(ctx.getServerHandler().playerEntity.getName() + " joined the party."), PlayerUtil.getPlayer(player));
                }
            }

            PartyMod.addPlayerToParty(message.partyID, ctx.getServerHandler().playerEntity.getUniqueID());

            return null;
        }

    }
}
