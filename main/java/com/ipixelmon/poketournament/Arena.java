package com.ipixelmon.poketournament;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.regions.Region;
import com.ipixelmon.mysql.SelectionForm;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

public class Arena implements Comparable<Arena> {

    public static Set<Arena> arenas = new TreeSet<>();

    private Region region;
    private Date startTime;
    private SingleEliminationTournament tournament;
    private String name;
    private boolean started = false;

    public Arena(Region region) {
        this.region = region;
        tournament = new SingleEliminationTournament();
        ResultSet resultSet = iPixelmon.mysql.selectAllFrom(PokeTournamentMod.class,
                new SelectionForm("Arenas").where("region", region.getID().toString()));

        try {
            if (resultSet.next()) {
                name = resultSet.getString("name");
            } else {
                throw new SQLException("Failed to find region.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return name;
    }

    public SingleEliminationTournament getTournament() {
        return tournament;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public boolean isStarted() {
        return started;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public void reset() {
        startTime = null;
        started = false;
    }

    public Region getRegion() {
        return region;
    }

    public void start() {
        for(Match match : tournament.getMatchesForRound(tournament.getRound())) match.start();
    }

    @Override
    public int compareTo(Arena o) {
        return o.getRegion().getID().equals(getRegion().getID()) ? 0 : -999;
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof Arena)) return false;
        return ((Arena) obj).getRegion().getID().equals(getRegion().getID());
    }

}
