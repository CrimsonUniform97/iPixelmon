package com.ipixelmon.poketournament;

import com.google.common.collect.Lists;
import com.ipixelmon.gym.BattleController;
import com.ipixelmon.landcontrol.regions.Region;
import com.pixelmonmod.pixelmon.battles.controller.participants.Spectator;
import com.pixelmonmod.pixelmon.enums.EnumBattleType;
import net.minecraft.entity.player.EntityPlayerMP;

import javax.annotation.Nullable;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Arena implements Comparable<Arena> {

    public static Set<Arena> arenas = new TreeSet<>();

    private Region region;
    private Set<Team> teams = new TreeSet<>();
    private List<EntityPlayerMP> spectators = Lists.newArrayList();
    private BattleController currentBattle;
    private Date startTime;
    private int round = 0;
    private Team currentTeam1, currentTeam2;

    public Arena(Region region) {
        this.region = region;
    }

    // TODO: Add bracket rounds, not winner of first game keeps going on and on. Need to have brackets
    public void start() throws Exception {
        currentTeam1 = (Team) teams.toArray()[round];
        currentTeam2 = (Team) teams.toArray()[round + 1];

        spectators.clear();

        currentBattle = new BattleController(currentTeam1.getParticipants(), currentTeam2.getParticipants(), EnumBattleType.Single);

        for (Team team : teams) {
            if (team != currentTeam1 && team != currentTeam2) {
                for (EntityPlayerMP player : team.players) spectators.add(player);
            }
        }

        for (EntityPlayerMP player : spectators) currentBattle.addSpectator(new Spectator(player, player.getName()));

        round += 1;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Set<Team> getTeams() {
        return teams;
    }

    public List<EntityPlayerMP> getSpectators() {
        return spectators;
    }

    public Region getRegion() {
        return region;
    }

    @Nullable
    public Team getCurrentTeam1() {
        return currentTeam1;
    }

    @Nullable
    public Team getCurrentTeam2() {
        return currentTeam2;
    }

    @Nullable
    public Date getStartTime() {
        return startTime;
    }

    public void reset() {
        spectators.clear();
        if (currentBattle != null)
            currentBattle.endBattle();
        currentBattle = null;
        round = 0;
        teams.clear();
        startTime = null;
        currentTeam1 = null;
        currentTeam2 = null;
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
