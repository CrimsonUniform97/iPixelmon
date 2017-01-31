package com.ipixelmon.poketournament.server;

import com.ipixelmon.poketournament.SingleEliminationTournament;
import com.ipixelmon.poketournament.client.TournamentGui;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PacketOpenTournamentGui implements IMessage {

    private SingleEliminationTournament tournament;

    public PacketOpenTournamentGui() {
    }

    public PacketOpenTournamentGui(SingleEliminationTournament tournament) {
        this.tournament = tournament;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.tournament = SingleEliminationTournament.fromBytes(buf);
        System.out.println(this.tournament.getTeams().size());
    }

    @Override
    public void toBytes(ByteBuf buf) {
        this.tournament.toBytes(buf);
        System.out.println(this.tournament.getTeams().size());
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
                    Minecraft.getMinecraft().displayGuiScreen(new TournamentGui(message.tournament));
                }
            });
        }
    }

}
