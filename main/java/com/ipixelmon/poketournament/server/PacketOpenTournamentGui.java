package com.ipixelmon.poketournament.server;

import com.ipixelmon.poketournament.SingleEliminationTournament;
import com.ipixelmon.poketournament.client.TournamentGui;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

public class PacketOpenTournamentGui implements IMessage {

    private UUID arena;
    private SingleEliminationTournament tournament;
    private boolean drawBrackets;

    public PacketOpenTournamentGui() {
    }

    public PacketOpenTournamentGui(UUID arena, SingleEliminationTournament tournament, boolean drawBrackets) {
        this.arena = arena;
        this.tournament = tournament;
        this.drawBrackets = drawBrackets;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.arena = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        this.tournament = SingleEliminationTournament.fromBytes(buf);
        this.drawBrackets = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, arena.toString());
        this.tournament.toBytes(buf);
        buf.writeBoolean(drawBrackets);
    }

    public static class Handler implements IMessageHandler<PacketOpenTournamentGui, IMessage> {

        @Override
        public IMessage onMessage(PacketOpenTournamentGui message, MessageContext ctx) {
            onMessage(message);
            return null;
        }

        @SideOnly(Side.CLIENT)
        private void onMessage(PacketOpenTournamentGui message) {
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                public void run() {
                    Minecraft.getMinecraft().displayGuiScreen(new TournamentGui(message.arena, message.tournament, message.drawBrackets));
                }
            });
        }
    }

}
