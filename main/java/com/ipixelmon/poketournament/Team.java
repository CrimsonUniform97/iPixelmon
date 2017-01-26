package com.ipixelmon.poketournament;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.storage.PixelmonStorage;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.List;

public class Team implements Comparable<Team> {

    public String name;
    public List<EntityPlayerMP> players = Lists.newArrayList();

    public Team(String name) {
        this.name = name;
    }

    public BattleParticipant[] getParticipants() {
        List<PlayerParticipant> playerParticipants = Lists.newArrayList();

        // TODO: Change this to where the player picks the pokemon to use
        for (EntityPlayerMP player : players)
            playerParticipants.add(new PlayerParticipant(player,
                    PixelmonStorage.pokeBallManager.getPlayerStorage(player).get().getFirstAblePokemon(player.worldObj)));

        return playerParticipants.toArray(new PlayerParticipant[playerParticipants.size()]);
    }

    @Override
    public int compareTo(Team o) {
        return o.name.equalsIgnoreCase(name) ? 0 : -999;
    }
}
