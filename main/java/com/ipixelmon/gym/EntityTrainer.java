package com.ipixelmon.gym;

import com.ipixelmon.team.TeamMod;
import com.ipixelmon.uuidmanager.UUIDManager;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.*;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.UUID;

public class EntityTrainer extends NPCTrainer implements Comparable<EntityTrainer> {

    private String playerName;
    private UUID playerID;
    private BlockPos pos;
    private EntityPixelmon pixelmon;

    // http://pixelmonmod.com/wiki/index.php?title=NPC_Editor
    public EntityTrainer(World world, BlockPos pos, UUID playerID, EntityPixelmon pixelmon) {
        super(world);
        this.pos = pos;
        this.playerID = playerID;
        this.pixelmon = pixelmon;

        this.targetTasks.taskEntries.clear();
        this.tasks.taskEntries.clear();
        setPosition(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
        setEncounterMode(EnumEncounterMode.Unlimited);
        this.getPokemonStorage().addToParty(pixelmon);
        setAIMode(EnumTrainerAI.StandStill);
        setBossMode(EnumBossMode.NotBoss);
        setBattleType(EnumBattleType.Single);
        setBattleAIMode(EnumBattleAIMode.Tactical);
        this.playerName = UUIDManager.getPlayerName(playerID);
    }

    @Override
    public void initAI() {}

    @Override
    public void writeToNBT(NBTTagCompound tagCompund) {
        super.writeToNBT(tagCompund);
        tagCompund.setString("playerID", playerID.toString());
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompund) {
        super.readFromNBT(tagCompund);
        playerID = UUID.fromString(tagCompund.getString("playerID"));
    }

    @Override
    public String getDisplayText() {
        return TeamMod.getPlayerTeam(playerID).colorChat() + playerName;
    }

    public EntityPixelmon getPixelmon() {
        return pixelmon;
    }

    @Override
    public int compareTo(EntityTrainer o) {
        return o.playerID.equals(playerID) ? 0 : -999;
    }
}
