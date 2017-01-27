package com.ipixelmon.poketournament;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class Bracket {

    private Map<Integer, Set<Team>> teams = Maps.newHashMap();

    public void addTeam(int bracket, Team team) {
        if(teams.containsKey(bracket)) {
            Set<Team> t = teams.get(bracket);
            t.add(team);
            teams.put(bracket, t);
        } else {
            Set<Team> t = new TreeSet<>();
            t.add(team);
            teams.put(bracket, t);
        }
    }

}
