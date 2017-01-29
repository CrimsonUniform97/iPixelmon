package com.ipixelmon.poketournament;

public class Match implements Comparable<Match>{

    protected Match prevMatch;
    protected Team team1, team2;
    protected Team winner = null;

    @Override
    public int compareTo(Match o) {
        return o.team1 == team1 && o.team2 == team2 ? 0 : -999;
    }
}
