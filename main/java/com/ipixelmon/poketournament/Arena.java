package com.ipixelmon.poketournament;

import com.ipixelmon.gym.BattleController;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.regions.Region;
import com.ipixelmon.mysql.SelectionForm;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

public class Arena implements Comparable<Arena> {

    public static Set<Arena> arenas = new TreeSet<>();

    private Region region;
    private BattleController currentBattle;
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
        if (currentBattle != null)
            currentBattle.endBattle();
        currentBattle = null;
        startTime = null;
    }

    public Region getRegion() {
        return region;
    }

    @Override
    public int compareTo(Arena o) {
        return o.getRegion().getID().equals(getRegion().getID()) ? 0 : -999;
    }

    public static Arena getArena(BattleController battleController) {
        for (Arena arena : arenas) if (arena.currentBattle.equals(battleController)) return arena;
        return null;
    }
}
