package com.ipixelmon.poketournament;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.storage.PixelmonStorage;
import com.pixelmonmod.pixelmon.storage.PlayerStorage;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.Optional;

public class Team implements Comparable<Team> {

    public String name;
    public List<EntityPlayerMP> players = Lists.newArrayList();

    public List<String> playerNames = Lists.newArrayList();

    public Team(String name) {
        this.name = name;
    }

    @SideOnly(Side.SERVER)
    public PlayerParticipant[] getParticipants() {

        List<PlayerParticipant> playerParticipants = Lists.newArrayList();

        for(EntityPlayerMP player : players) {
            Optional optstorage1 = PixelmonStorage.pokeBallManager.getPlayerStorage(player);

            if(optstorage1.isPresent()) {
                PlayerStorage storage1 = (PlayerStorage)optstorage1.get();
                EntityPixelmon player1FirstPokemon = storage1.getFirstAblePokemon(player.worldObj);
                playerParticipants.add(new PlayerParticipant(player, new EntityPixelmon[]{player1FirstPokemon}));
            }
        }

        return playerParticipants.toArray(new PlayerParticipant[playerParticipants.size()]);
    }

    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, name);
        buf.writeInt(players.size());
        for (EntityPlayerMP player : players) {
            ByteBufUtils.writeUTF8String(buf, player.getDisplayNameString());
        }
    }

    public static Team fromBytes(ByteBuf buf) {
        Team team = new Team(ByteBufUtils.readUTF8String(buf));
        int playerSize = buf.readInt();

        for (int i = 0; i < playerSize; i++) {
            team.playerNames.add(ByteBufUtils.readUTF8String(buf));
        }

        return team;
    }

    @Override
    public int compareTo(Team o) {
        return o.name.equalsIgnoreCase(name) ? 0 : -999;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Team)) return false;
        return ((Team) obj).name.equalsIgnoreCase(name);
    }
}
