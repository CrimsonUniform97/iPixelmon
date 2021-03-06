package com.ipixelmon.party;

import com.ipixelmon.util.PlayerUtil;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.uuidmanager.UUIDManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.text.TextComponentBase;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

/**
 * Created by colbymchenry on 12/18/16.
 */
public class PacketSendPartyInvite implements IMessage{

    public PacketSendPartyInvite(){}

    private String player;

    public PacketSendPartyInvite(String player){
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


    public static class Handler implements IMessageHandler<PacketSendPartyInvite, IMessage> {

        @Override
        public IMessage onMessage(PacketSendPartyInvite message, MessageContext ctx) {
            UUID playerUUID = UUIDManager.getUUID(message.player);
            EntityPlayerMP sender = ctx.getServerHandler().playerEntity;

            // player does not exist
            if(playerUUID == null) {
                sender.addChatComponentMessage(new TextComponentString("Player not found."));
                return null;
            }

            EntityPlayerMP player = PlayerUtil.getPlayer(playerUUID);

            // player is not online
            if(player == null) {
                sender.addChatComponentMessage(new TextComponentString("Player not online.") {
                });
                return null;
            }

            UUID partyUUID = PartyAPI.Server.getPlayersParty(sender.getUniqueID());

            if(partyUUID == null) {
                partyUUID = UUID.randomUUID();
                PartyAPI.Server.addPlayerToParty(partyUUID, sender.getUniqueID());
            }

            iPixelmon.network.sendTo(new PacketReceivePartyInvite(partyUUID, sender.getName()), player);

            return null;
        }

    }

}
