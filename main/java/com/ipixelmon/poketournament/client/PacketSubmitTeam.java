package com.ipixelmon.poketournament.client;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.LandControlAPI;
import com.ipixelmon.poketournament.Arena;
import com.ipixelmon.poketournament.Team;
import com.ipixelmon.poketournament.TournamentAPI;
import com.ipixelmon.poketournament.server.PacketPlaySound;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.UUID;

public class PacketSubmitTeam implements IMessage {

    private UUID arena;
    private String teamName;

    public PacketSubmitTeam() {}

    public PacketSubmitTeam(UUID arena, String teamName) {
        this.arena = arena;
        this.teamName = teamName;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.arena = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        this.teamName = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, arena.toString());
        ByteBufUtils.writeUTF8String(buf, teamName);
    }

    public static class Handler implements IMessageHandler<PacketSubmitTeam, IMessage> {

        @Override
        public IMessage onMessage(PacketSubmitTeam message, MessageContext ctx) {
            Arena arena = TournamentAPI.Server.getArena(LandControlAPI.Server.getRegion(message.arena));

            if(arena == null) return null;

            for(Team team : arena.getTournament().getTeams()) {
                if(team.name.equalsIgnoreCase(message.teamName)) {
                    iPixelmon.network.sendTo(new PacketResponeForGui("There is already a team with that name."), ctx.getServerHandler().playerEntity);
                    return null;
                }
            }

            if(arena.isStarted()) {
                iPixelmon.network.sendTo(new PacketResponeForGui("Tournament has already started."), ctx.getServerHandler().playerEntity);
                return null;
            }

            Team team = new Team(message.teamName);
            team.players.add(ctx.getServerHandler().playerEntity);
            arena.getTournament().addTeam(team);
            try {
                arena.getTournament().setupRounds();
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            iPixelmon.network.sendTo(new PacketPlaySound("tournamentSong"), ctx.getServerHandler().playerEntity);

            return null;
        }
    }

}
