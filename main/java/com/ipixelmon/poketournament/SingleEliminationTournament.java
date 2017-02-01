package com.ipixelmon.poketournament;

import io.netty.buffer.ByteBuf;

import java.util.Set;
import java.util.TreeSet;

public class SingleEliminationTournament {

    /* All participating teams */
    protected Set<Team> teams = new TreeSet<>();

    /* All matches, current and history */
    protected Set<Match> matches = new TreeSet<>();

    protected int round = 0;

    /* Calculates the number of teams participating in the first round */
    protected int getTeamCountFirstRound() {
        int N = teams.size();
        int P = N;
        P = (int) Math.pow(2, Math.ceil(Math.log((double) P) / Math.log(2)));
        return N - (P - N);
    }

    public int getMatchCountFirstRound() {
        int N = teams.size();
        int P = N;
        P = (int) Math.pow(2, Math.ceil(Math.log((double) P) / Math.log(2)));
        return N - P / 2;
    }

    /* Calculates the number of teams participating in the second round */
    protected int getTeamCountSecondRound() {
        int N = teams.size();
        int P = N;
        P = (int) Math.pow(2, Math.ceil(Math.log((double) P) / Math.log(2)));
        return N - P / 2 + P - N;
    }

    /* Calculates the number of teams participating in the given round */
    public int getTeamCountForRound(int round) {
        if (round <= 1) return getTeamCountFirstRound();
        if (round == 2) return getTeamCountSecondRound();
        return getTeamCountForRound(round - 1) / 2;
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

    /* Gets all the active matches */
    public Set<Match> getActiveMatches() {
        Set<Match> currentMatches = new TreeSet<>();

        for (Match match : matches) if (match.active) currentMatches.add(match);

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

    public void setRound(int round) {
        this.round = round;
    }

    public int getTotalNumberOfRounds() {
        for (int i = 0; i < 1000; i++) {
            if (getTeamCountForRound(i) <= 1) return i;
        }

        return 0;
    }

    /* Setups up matches after all current matches are complete */
    public void setupRounds() throws Exception {
        if (getTeams().isEmpty() || getTeams().size() < 2) throw new Exception("Not enough participants.");

        matches.clear();

        Team[] teamArray = teams.toArray(new Team[teams.size()]);

        Match m;
//        for (int i = 0; i < getTeamCountFirstRound(); i++) {
//            if (i + 1 < getTeamCountFirstRound()) {
//                m = new Match();
//                m.team1 = teamArray[i];
//                m.team2 = teamArray[i + 1];
//                m.round = 1;
//                matches.add(m);
//            }
//        }
//
//            /* Setup unlucky teams for second round */
//        if (getTeamCountFirstRound() != getTeams().size()) {
//            for (int i = 0; i < getTeamCountSecondRound() / 2; i++) {
//                // TODO: May not need unluckyTeams, we just go ahead and assign them to round 2.
//                m = new Match();
//                if (getTeamCountFirstRound() + i < teamArray.length)
//                    m.team1 = teamArray[getTeamCountFirstRound() + i];
//
//                if (getTeamCountFirstRound() + i + 1 < teamArray.length)
//                    m.team2 = teamArray[getTeamCountFirstRound() + i + 1];
//                m.round = 2;
//                matches.add(m);
//            }
//        }

        int teamIndex = 0;

        // TODO: Link previous matches
        for (int i = 0; i < getTotalNumberOfRounds(); i++) {
            for (int t = 0; t < getTeamCountForRound(i) / 2; t++) {
                m = new Match();
                m.round = i;

                if(teamIndex + 1 < teamArray.length - 1) {
                    m.team1 = teamArray[teamIndex];
                    m.team2 = teamArray[teamIndex + 1];
                    System.out.println(m.team1.name);
                    System.out.println(m.team2.name);
                }

                teamIndex += 2;

                matches.add(m);
            }
        }
    }

    /* Convert all data to bytes for packet sending */
    public void toBytes(ByteBuf buf) {
        buf.writeInt(round);
        buf.writeInt(getTeams().size());
        for (Team team : getTeams()) team.toBytes(buf);
        buf.writeInt(getMatches().size());
        for (Match match : getMatches()) match.toBytes(buf);
    }

    /* Convert bytes into a tournament for packet receiving */
    public static SingleEliminationTournament fromBytes(ByteBuf buf) {
        SingleEliminationTournament tournament = new SingleEliminationTournament();
        tournament.round = buf.readInt();
        int teams = buf.readInt();
        for (int i = 0; i < teams; i++) tournament.addTeam(Team.fromBytes(buf));
        int matches = buf.readInt();

        for (int i = 0; i < matches; i++) {
            Match m = Match.fromBytes(buf);
            tournament.matches.add(m);
        }

        return tournament;
    }
}
