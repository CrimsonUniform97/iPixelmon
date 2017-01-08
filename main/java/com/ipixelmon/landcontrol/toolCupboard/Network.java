package com.ipixelmon.landcontrol.toolCupboard;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.LandControl;
import com.ipixelmon.mysql.DeleteForm;
import com.ipixelmon.mysql.InsertForm;
import com.ipixelmon.mysql.SelectionForm;
import com.ipixelmon.mysql.UpdateForm;
import com.ipixelmon.util.ArrayUtil;
import com.ipixelmon.uuidmanager.UUIDManager;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by colby on 1/7/2017.
 */
public class Network {

    private UUID player;

    public Network(UUID player) {
        this.player = player;
    }

    public UUID getID() {
        return player;
    }

    public void create() {
        if (exists()) return;

        InsertForm insertForm = new InsertForm("Networks");
        insertForm.add("player", player.toString());
        insertForm.add("players", "[]");
        iPixelmon.mysql.insert(LandControl.class, insertForm);
    }

    public void delete() {
        if (!exists()) return;

        DeleteForm deleteForm = new DeleteForm("Networks");
        deleteForm.add("player", player.toString());
        iPixelmon.mysql.delete(LandControl.class, deleteForm);
    }

    public boolean exists() {
        ResultSet result = iPixelmon.mysql.selectAllFrom(LandControl.class,
                new SelectionForm("Networks").where("player", player.toString()));

        try {
            if (result.next()) return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public Set<UUID> getPlayers() {
        Set<UUID> players = new TreeSet<>();

        if (!exists()) return players;

        ResultSet result = iPixelmon.mysql.selectAllFrom(LandControl.class, new SelectionForm("Networks")
                .where("player", player.toString()));

        try {
            if (result.next())
                for (String s : ArrayUtil.fromString(result.getString("players"))) {
                    if (!s.isEmpty())
                        players.add(UUID.fromString(s));
                }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        players.add(player);
        return players;
    }

    public void addPlayer(UUID player) {
        Set<UUID> players = getPlayers();

        if (getPlayers().contains(player)) return;

        players.add(player);

        List<String> playersArray = Lists.newArrayList();

        for (UUID uuid : players) playersArray.add(uuid.toString());

        UpdateForm updateForm = new UpdateForm("Networks");
        updateForm.set("players", ArrayUtil.toString(playersArray.toArray(new String[playersArray.size()])));
        updateForm.where("player", this.player.toString());
        iPixelmon.mysql.update(LandControl.class, updateForm);
    }

    public void removePlayer(UUID player) {
        Set<UUID> players = getPlayers();

        if (!getPlayers().contains(player)) return;

        players.remove(player);

        List<String> playersArray = Lists.newArrayList();

        for (UUID uuid : players) playersArray.add(uuid.toString());

        UpdateForm updateForm = new UpdateForm("Networks");
        updateForm.set("players", ArrayUtil.toString(playersArray.toArray(new String[playersArray.size()])));
        updateForm.where("player", this.player.toString());
        iPixelmon.mysql.update(LandControl.class, updateForm);
    }

    public Map<UUID, String> getPlayerMap() {
        Map<UUID, String> players = Maps.newHashMap();

        if (!exists()) return players;

        for (UUID id : getPlayers()) players.put(id, UUIDManager.getPlayerName(id));

        return players;
    }
}
