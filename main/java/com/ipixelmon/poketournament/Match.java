package com.ipixelmon.poketournament;

import com.google.common.collect.Lists;
import com.ipixelmon.gym.BattleController;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.TrainerParticipant;
import com.pixelmonmod.pixelmon.enums.EnumBattleType;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.List;
import java.util.UUID;

public class Match implements Comparable<Match> {

    public UUID id = UUID.randomUUID();
    public Match prevMatch1, prevMatch2;
    public int round = 0;
    public Team team1, team2;
    public Team winner = null;
    public boolean active = false;
    public BattleControllerBase battleController;

    public void start() {
        try {
            PlayerParticipant[] team1Par = team1.getParticipants();
            PlayerParticipant[] team2Par = team2.getParticipants();

            List<BattleParticipant> battleParticipants1 = Lists.newArrayList();
            List<BattleParticipant> battleParticipants2 = Lists.newArrayList();

            for (PlayerParticipant p : team1Par) battleParticipants1.add(p);
            for (PlayerParticipant p : team2Par) battleParticipants2.add(p);

            battleController = new BattleControllerBase(battleParticipants1.toArray(new BattleParticipant[battleParticipants1.size()]), battleParticipants2.toArray(new BattleParticipant[battleParticipants2.size()]));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void toBytes(ByteBuf buf) {
        buf.writeInt(round);
        buf.writeBoolean(team1 == null);
        buf.writeBoolean(team2 == null);
        buf.writeBoolean(winner == null);
        buf.writeBoolean(prevMatch1 == null);
        buf.writeBoolean(prevMatch2 == null);
        buf.writeBoolean(active);
        if (team1 != null)
            team1.toBytes(buf);
        if (team2 != null)
            team2.toBytes(buf);
        if (winner != null)
            winner.toBytes(buf);
        if (prevMatch1 != null)
            prevMatch1.toBytes(buf);
        if (prevMatch2 != null)
            prevMatch2.toBytes(buf);

        ByteBufUtils.writeUTF8String(buf, id.toString());
    }

    public static Match fromBytes(ByteBuf buf) {
        Match match = new Match();
        match.round = buf.readInt();
        boolean team1Null = buf.readBoolean();
        boolean team2Null = buf.readBoolean();
        boolean winnerNull = buf.readBoolean();
        boolean prevMatch1Null = buf.readBoolean();
        boolean prevMatch2Null = buf.readBoolean();
        match.active = buf.readBoolean();

        if (!team1Null)
            match.team1 = Team.fromBytes(buf);
        if (!team2Null)
            match.team2 = Team.fromBytes(buf);
        if (!winnerNull)
            match.winner = Team.fromBytes(buf);
        if (!prevMatch1Null)
            match.prevMatch1 = Match.fromBytes(buf);
        if (!prevMatch2Null)
            match.prevMatch2 = Match.fromBytes(buf);

        match.id = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        return match;
    }

    @Override
    public int compareTo(Match o) {
        return o.id.equals(id) ? 0 : -999;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Match)) return false;

        Match m = (Match) obj;
        return m.id.equals(id);
    }
}
