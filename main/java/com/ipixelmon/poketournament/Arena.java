package com.ipixelmon.poketournament;

import com.ipixelmon.gym.BattleController;
import com.ipixelmon.landcontrol.regions.Region;
import java.util.Date;
import java.util.Set;
import java.util.TreeSet;

public class Arena implements Comparable<Arena> {

    public static Set<Arena> arenas = new TreeSet<>();

    private Region region;
    private BattleController currentBattle;
    private Date startTime;
    private SingleElimationTournament tournament;

    public Arena(Region region) {
        this.region = region;
        tournament = new SingleElimationTournament();
    }

    public SingleElimationTournament getTournament() {
        return tournament;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Region getRegion() {
        return region;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void reset() {
        if (currentBattle != null)
            currentBattle.endBattle();
        currentBattle = null;
        startTime = null;
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
