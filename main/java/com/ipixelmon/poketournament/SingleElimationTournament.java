package com.ipixelmon.poketournament;

import java.util.Set;
import java.util.TreeSet;

public class SingleElimationTournament {

    /* All participating teams */
    protected Set<Team> teams = new TreeSet<>();

    /* All matches, current and history */
    protected Set<Match> matches = new TreeSet<>();

    /* Add a team to the tournament */
    public void addTeam(Team team) {
        teams.add(team);
    }

    /* Gets all the current matches */
    public Set<Match> getCurrentMatches() {
        Set<Match> currentMatches = new TreeSet<>();

        for (Match match : matches) if (match.winner == null) currentMatches.add(match);

        return currentMatches;
    }

    public void setupMatches() {
        Team[] teamArray = (Team[]) teams.toArray();

        if (matches.isEmpty()) {
            Match m;
            for (int i = 0; i < teamArray.length; i++) {
                if (i + 1 < teamArray.length) {
                    m = new Match();
                    m.team1 = teamArray[i];
                    m.team2 = teamArray[i + 1];
                    matches.add(m);
                } else {
                    if (teams.size() / 2 != matches.size()) {
                        // TODO: Figure out what to do with odd count
                    }
                }
            }
        } else {
            Set<Match> matches = new TreeSet<>();
            /* Add all of the matches */
            matches.addAll(this.matches);
            /* Remove all of the matches without a winner, so we can setup winning teams */
            matches.removeAll(getCurrentMatches());

            Match[] matchArray = (Match[]) matches.toArray();

            Match m;
            for (int i = 0; i < matchArray.length; i++) {
                if (i + 1 < matchArray.length) {
                    m = new Match();
                    m.team1 = matchArray[i].winner;
                    m.team2 = matchArray[i + 1].winner;
                    matches.add(m);
                }
            }
        }
    }
}
