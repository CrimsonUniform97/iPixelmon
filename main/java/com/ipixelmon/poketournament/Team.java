package com.ipixelmon.poketournament;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.storage.PixelmonStorage;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

public class Team implements Comparable<Team> {

    public String name;
    public List<EntityPlayerMP> players = Lists.newArrayList();

    public List<String> playerNames = Lists.newArrayList();

    public Team(String name) {
        this.name = name;
    }

    @SideOnly(Side.SERVER)
    public BattleParticipant[] getParticipants() {
        List<PlayerParticipant> playerParticipants = Lists.newArrayList();

        // TODO: Change this to where the player picks the pokemon to use
        for (EntityPlayerMP player : players)
            playerParticipants.add(new PlayerParticipant(player,
                    PixelmonStorage.pokeBallManager.getPlayerStorage(player).get().getFirstAblePokemon(player.worldObj)));

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

        for(int i = 0; i < playerSize; i++) {
            team.playerNames.add(ByteBufUtils.readUTF8String(buf));
        }

        return team;
    }

    @Override
    public int compareTo(Team o) {
        return o.name.equalsIgnoreCase(name) ? 0 : -999;
    }
}
