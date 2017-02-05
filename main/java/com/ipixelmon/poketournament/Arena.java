package com.ipixelmon.poketournament;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.regions.Region;
import com.ipixelmon.mysql.SelectionForm;
import com.ipixelmon.poketournament.server.PacketStopSound;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

public class Arena implements Comparable<Arena> {

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

    @SideOnly(Side.SERVER)
    public void start() {
        for(Match match : tournament.getMatchesForRound(tournament.getRound())){
            match.start();

            for(EntityPlayerMP player : match.team1.players) {
                iPixelmon.network.sendTo(new PacketStopSound("tournamentSong", SoundCategory.MUSIC), player);
            }

            for(EntityPlayerMP player : match.team2.players) {
                iPixelmon.network.sendTo(new PacketStopSound("tournamentSong", SoundCategory.MUSIC), player);
            }
        }
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
