package com.ipixelmon.poketournament;

import io.netty.buffer.ByteBuf;

public class Match implements Comparable<Match> {

    public Match prevMatch;
    public int round = 0;
    public Team team1, team2;
    public Team winner = null;
    public boolean active = false;

    public void toBytes(ByteBuf buf) {
        buf.writeInt(round);
        buf.writeBoolean(team1 == null);
        buf.writeBoolean(team2 == null);
        buf.writeBoolean(winner == null);
        buf.writeBoolean(prevMatch == null);
        buf.writeBoolean(active);
        if (team1 != null)
            team1.toBytes(buf);
        if (team2 != null)
            team2.toBytes(buf);
        if (winner != null)
            winner.toBytes(buf);
        if (prevMatch != null)
            prevMatch.toBytes(buf);
    }

    public static Match fromBytes(ByteBuf buf) {
        Match match = new Match();
        match.round = buf.readInt();
        boolean team1Null = buf.readBoolean();
        boolean team2Null = buf.readBoolean();
        boolean winnerNull = buf.readBoolean();
        boolean prevMatchNull = buf.readBoolean();
        match.active = buf.readBoolean();

        if(!team1Null)
            match.team1 = Team.fromBytes(buf);
        if(!team2Null)
            match.team2 = Team.fromBytes(buf);
        if(!winnerNull)
            match.winner = Team.fromBytes(buf);
        if(!prevMatchNull)
            match.prevMatch = Match.fromBytes(buf);

        return match;
    }

    @Override
    public int compareTo(Match o) {
        return team1 == null && team2 == null ? -999 : o.team1 == team1 && o.team2 == team2 ? 0 : -999;
    }
}
