package com.ipixelmon.poketournament;

import io.netty.buffer.ByteBuf;

import java.util.*;

public class SingleEliminationTournament {

    /* All participating teams */
    protected Set<Team> teams = new TreeSet<>();

    /* All matches, current and history */
    protected Set<Match> matches = new TreeSet<>();

    protected int round = 1;

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

    /* Gets all the matches for the given round */
    public List<Match> getMatchesForRound(int round) {
        Set<Match> matchesForRound = new TreeSet<>();

        for (Match match : matches) if (match.round == round) matchesForRound.add(match);

        List<Match> arrayList = new ArrayList<>();
        arrayList.addAll(matchesForRound);

        return arrayList;
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round < 1 ? 1 : round;
        if (getTotalNumberOfRounds() > 0)
            this.round = this.round > getTotalNumberOfRounds() ? getTotalNumberOfRounds() : this.round;
    }

    public int getTotalNumberOfRounds() {
        for (int i = 0; i < 1000; i++) {
            if (getTeamCountForRound(i) <= 1) return i;
        }

        return 0;
    }

    public Team getWinner() {
        if(getMatchesForRound(getTotalNumberOfRounds() - 1).isEmpty()) return null;
        Match match = getMatchesForRound(getTotalNumberOfRounds() - 1).get(0);
        if (match != null && match.winner != null) return match.winner;
        return null;
    }

    /* Setups up matches after all current matches are complete */
    public void setupRounds() throws Exception {
        if (getTeams().isEmpty() || getTeams().size() < 2) throw new Exception("Not enough participants.");

        matches.clear();

        Team[] teamArray = teams.toArray(new Team[teams.size()]);

        Match m;

        for (int i = 1; i < getTotalNumberOfRounds(); i++) {
            for (int t = 0; t < getTeamCountForRound(i) / 2; t++) {
                m = new Match();
                m.round = i;
                matches.add(m);
            }
        }

        for (int i = 2; i < getTotalNumberOfRounds(); i++) {
            int matchCount = 0;
            Match[] prevMatches = getMatchesForRound(i - 1).toArray(new Match[getMatchesForRound(i - 1).size()]);
            for (Match match : getMatchesForRound(i)) {
                if(matchCount < prevMatches.length)
                match.prevMatch1 = prevMatches[matchCount++];
                if(matchCount < prevMatches.length)
                match.prevMatch2 = prevMatches[matchCount++];
            }
        }

        int i = 0;
        for (Match match : getMatchesForRound(1)) {
            match.team1 = teamArray[i++];
            match.team2 = teamArray[i++];
        }

        for(Match match : getMatchesForRound(2)) {
            if(match.prevMatch1 == null)
                match.team1 = teamArray[i++];
            if(match.prevMatch2 == null)
                match.team2 = teamArray[i++];
        }

    }

    public void setWinner(Match match, Team team) {
        match.winner = team;
        for (Match m : getMatchesForRound(match.round + 1)) {
            if (m.prevMatch1 != null && m.prevMatch1 == match) {
                if (match.round == 1) {
                    m.team1 = team;
                } else {
                    m.team2 = team;
                }
            } else if (m.prevMatch2 != null && m.prevMatch2 == match) {
                if (match.round == 1) {
                    m.team2 = team;
                } else {
                    m.team1 = team;
                }
            }
        }
    }

    public boolean isRoundOver(int round) {
        for(Match m : getMatchesForRound(round)) if(m.winner == null) return false;
        return true;
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
