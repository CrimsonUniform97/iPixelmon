package com.ipixelmon.poketournament;

import io.netty.buffer.ByteBuf;

import java.util.Set;
import java.util.TreeSet;

public class SingleEliminationTournament {

    /* All participating teams */
    protected Set<Team> teams = new TreeSet<>();

    /* All teams that didn't get to participate in the first round */
    protected Set<Team> unluckyTeams = new TreeSet<>();

    /* All matches, current and history */
    protected Set<Match> matches = new TreeSet<>();

    protected int round = 0;

    protected int getTeamCountFirstRound() {
        int N = teams.size();
        int P = Integer.highestOneBit(teams.size());
        return (N - (P - N));
    }

    protected int getMatchCountFirstRound() {
        int N = teams.size();
        int P = Integer.highestOneBit(teams.size());
        return (N - P / 2);
    }

    protected int getTeamCountSecondRound() {
        int N = teams.size();
        int P = Integer.highestOneBit(teams.size());
        return N - P / 2 + P - N;
    }

    /* Add a team to the tournament */
    public void addTeam(Team team) {
        teams.add(team);
    }

    /* Remove a team from the tournament */
    public void removeTeam(Team team) {
        teams.remove(team);
    }

    /* Grabs all the teams */
    public Set<Team> getTeams() {
        return teams;
    }

    /* Grabs all the teams for the second round that didn't par take in the first round */
    public Set<Team> getUnluckyTeams() {
        return unluckyTeams;
    }

    /* Gets all the active matches */
    public Set<Match> getActiveMatches() {
        Set<Match> currentMatches = new TreeSet<>();

        for (Match match : matches) if (match.winner == null) currentMatches.add(match);

        return currentMatches;
    }

    /* Returns all matches */
    public Set<Match> getMatches() {
        return matches;
    }

    /* Gets all the previous matches */
    public Set<Match> getPreviousMatches() {
        Set<Match> prevMatches = new TreeSet<>();

        for (Match match : matches) if (match.winner != null && match.prevMatch1 == null) prevMatches.add(match);

        return prevMatches;
    }

    /* Gets all the matches for the given round */
    public Set<Match> getMatchesForRound(int round) {
        Set<Match> matchesForRound = new TreeSet<>();

        for (Match match : matches) if (match.round == round) matchesForRound.add(match);

        return matchesForRound;
    }

    public int getRound() {
        return round;
    }

    /* Setups up matches after all current matches are complete */
    public void setupRound() throws Exception {
        if(getTeams().isEmpty() || getTeams().size() < 2) throw new Exception("Not enough participants.");

        /* Check for any active matches */
        if(getActiveMatches().size() != 0) throw new Exception("There are still matches active.");

        Team[] teamArray = (Team[]) teams.toArray();

        /* If there are no matches, setup the first round of matches and the second matches */
        if(matches.isEmpty()) {
            Match m;
            for (int i = 0; i < getTeamCountFirstRound(); i++) {
                if (i + 1 < getTeamCountFirstRound()) {
                    m = new Match();
                    m.team1 = teamArray[i];
                    m.team2 = teamArray[i + 1];
                    matches.add(m);
                    m.round = round;
                }
            }

            /* Setup unlucky teams for second round */
            for (int i = getTeamCountFirstRound(); i < getTeamCountSecondRound(); i++) {
                unluckyTeams.add(teamArray[i]);
            }
        } else {
            /* If there are unlucky matches we need to set them up */
            if(!unluckyTeams.isEmpty()) {
                Match[] prevMatchArray = (Match[]) getPreviousMatches().toArray();
                Team[] unluckyTeamArray = (Team[]) unluckyTeams.toArray();
                Match m;
                for(int i = 0; i < prevMatchArray.length; i++) {
                    m = new Match();
                    m.team1 = prevMatchArray[i].winner;
                    m.team2 = unluckyTeamArray[i];
                    m.prevMatch1 = prevMatchArray[i];
                    m.round = round;
                }
            } else {
                /* We are past the second round, now we need to pair winner against winner */
                Match[] prevMatchArray = (Match[]) getPreviousMatches().toArray();

                Match m;
                for(int i = 0; i < prevMatchArray.length; i++) {
                    if(i + 1 < prevMatchArray.length) {
                        m = new Match();
                        m.team1 = prevMatchArray[i].winner;
                        m.team2 = prevMatchArray[i + 1].winner;
                        m.prevMatch1 = prevMatchArray[i];
                        m.prevMatch2 = prevMatchArray[i + 1];
                        m.round = round;
                    }
                }
            }
        }

        round++;
    }

    /* Convert all data to bytes for packet sending */
    public void toBytes(ByteBuf buf) {
        buf.writeInt(round);
        buf.writeInt(getTeams().size());
        for(Team team : getTeams()) team.toBytes(buf);
        buf.writeInt(getMatches().size());
        for(Match match : getMatches()) match.toBytes(buf);
        buf.writeInt(getUnluckyTeams().size());
        for(Team team : getUnluckyTeams()) team.toBytes(buf);
    }

    /* Convert bytes into a tournament for packet receiving */
    public static SingleEliminationTournament fromBytes(ByteBuf buf) {
        SingleEliminationTournament tournament = new SingleEliminationTournament();
        tournament.round = buf.readInt();
        int teams = buf.readInt();
        for(int i = 0; i < teams; i++) tournament.addTeam(Team.fromBytes(buf));
        int matches = buf.readInt();
        for(int i = 0; i < matches; i++) tournament.matches.add(Match.fromBytes(buf));
        int unluckyTeams = buf.readInt();
        for(int i = 0; i < unluckyTeams; i++) tournament.unluckyTeams.add(Team.fromBytes(buf));

        return tournament;
    }
}
