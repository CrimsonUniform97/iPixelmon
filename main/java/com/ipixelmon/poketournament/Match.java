package com.ipixelmon.poketournament;

import io.netty.buffer.ByteBuf;

public class Match implements Comparable<Match> {

    protected Match prevMatch1, prevMatch2;
    protected int round = 0;
    protected Team team1, team2;
    protected Team winner = null;

    public void toBytes(ByteBuf buf) {
        buf.writeInt(round);
        buf.writeBoolean(team1 == null);
        buf.writeBoolean(team2 == null);
        buf.writeBoolean(winner == null);
        buf.writeBoolean(prevMatch1 == null);
        buf.writeBoolean(prevMatch2 == null);
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
    }

    public static Match fromBytes(ByteBuf buf) {
        Match match = new Match();
        match.round = buf.readInt();
        boolean team1Null = buf.readBoolean();
        boolean team2Null = buf.readBoolean();
        boolean winnerNull = buf.readBoolean();
        boolean prevMatch1Null = buf.readBoolean();
        boolean prevMatch2Null = buf.readBoolean();

        if(!team1Null)
            match.team1 = Team.fromBytes(buf);
        if(!team2Null)
            match.team2 = Team.fromBytes(buf);
        if(!winnerNull)
            match.winner = Team.fromBytes(buf);
        if(!prevMatch1Null)
            match.prevMatch1 = Match.fromBytes(buf);
        if(!prevMatch2Null)
            match.prevMatch2 = Match.fromBytes(buf);

        return match;
    }

    @Override
    public int compareTo(Match o) {
        return o.team1 == team1 && o.team2 == team2 ? 0 : -999;
    }
}
