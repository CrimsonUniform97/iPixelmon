package com.ipixelmon.party;

import com.ipixelmon.iPixelmon;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
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

        @Override
        public IMessage onMessage(PacketAcceptPartyInvite message, MessageContext ctx) {

            EntityPlayerMP entityPlayer;
            for (UUID player : PartyAPI.Server.getPlayersInParty(message.partyID)) {
                entityPlayer = iPixelmon.util.player.getPlayer(player);
                if (entityPlayer != null && !player.equals(ctx.getServerHandler().playerEntity.getUniqueID())) {
                    iPixelmon.network.sendTo(new PacketJoinedParty(ctx.getServerHandler().playerEntity.getUniqueID(),
                            ctx.getServerHandler().playerEntity.getName(), message.partyID), entityPlayer);
                    iPixelmon.network.sendTo(new PacketJoinedParty(entityPlayer.getUniqueID(), entityPlayer.getName(),
                            message.partyID), ctx.getServerHandler().playerEntity);
                }
            }

            PartyAPI.Server.addPlayerToParty(message.partyID, ctx.getServerHandler().playerEntity.getUniqueID());

            return null;
        }

    }
}
