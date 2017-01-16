package com.ipixelmon.team;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class PacketClientTeam implements IMessage {

    private EnumTeam enumTeam;

    public PacketClientTeam() {
    }

    public PacketClientTeam(EnumTeam enumTeam) {
        this.enumTeam = enumTeam;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        enumTeam = EnumTeam.getTeamFromID(buf.readInt());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(enumTeam.ordinal());
    }

    public static class Handler implements IMessageHandler<PacketClientTeam, IMessage> {

        @Override
        public IMessage onMessage(PacketClientTeam message, MessageContext ctx) {
            TeamMod.clientSideTeam = message.enumTeam;
            return null;
        }

    }

}
