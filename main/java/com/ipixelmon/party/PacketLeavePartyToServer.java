package com.ipixelmon.party;

import com.ipixelmon.iPixelmon;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

/**
 * Created by colbymchenry on 12/28/16.
 */
public class PacketLeavePartyToServer implements IMessage {

    public PacketLeavePartyToServer() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    public static class Handler implements IMessageHandler<PacketLeavePartyToServer, IMessage> {

        @Override
        public IMessage onMessage(PacketLeavePartyToServer message, MessageContext ctx) {

            UUID partyID = PartyAPI.Server.getPlayersParty(ctx.getServerHandler().playerEntity.getUniqueID());

            if(partyID == null) return null;

            EntityPlayerMP player;
            for(UUID playerID : PartyAPI.Server.getPlayersInParty(partyID)) {
                player = iPixelmon.util.player.getPlayer(playerID);

                if(player != null) {
                    iPixelmon.network.sendTo(new PacketLeavePartyToClient(ctx.getServerHandler().playerEntity.getName(),
                            ctx.getServerHandler().playerEntity.getUniqueID()), player);
                }
            }
            return null;
        }

    }
}
