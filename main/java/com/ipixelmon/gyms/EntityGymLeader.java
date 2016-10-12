package com.ipixelmon.gyms;

import com.ipixelmon.gyms.client.CustomSkinManager;
import com.ipixelmon.teams.Teams;
import com.ipixelmon.uuidmanager.UUIDManager;
import com.mojang.authlib.GameProfile;
import com.pixelmonmod.pixelmon.comm.SetTrainerData;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumEncounterMode;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.UUID;

/**
 * Created by Colby McHenry on 9/28/2016.
 */
public class EntityGymLeader extends NPCTrainer {

    public EntityGymLeader(World world) {
        super(world);
    }

    public EntityGymLeader(World world, BlockPos location, EntityPixelmon pixelmon, UUID playerUUID) {
        super(world);
        clearAITasks();
        this.tasks.addTask(0, new EntityMoveToLocationAI(this, 1.0D, location.getX() + 0.5D, location.getY() + 0.5D, location.getZ() + 0.5D));
        this.tasks.addTask(1, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
        setPlayerUUID(playerUUID);
        update(new SetTrainerData("Name", "Greeting", "Win", "Loss", 12, new ItemStack[]{}));
        setPosition(location.getX() + 0.5D, location.getY() + 0.5D, location.getZ() + 0.5D);
        setEncounterMode(EnumEncounterMode.Unlimited);
        loadPokemon(pixelmon);
    }


    @Override
    public void initAI() {

    }

    @Override
    public boolean isAIDisabled() {
        return false;
    }

    protected void clearAITasks() {
        tasks.taskEntries.clear();
        targetTasks.taskEntries.clear();
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompund) {
        super.writeToNBT(tagCompund);
        tagCompund.setString("playerUUID", getPlayerUUID() != null ? getPlayerUUID().toString() : this.getUniqueID().toString());
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompund) {
        super.readFromNBT(tagCompund);
        setPlayerUUID(UUID.fromString(tagCompund.getString("playerUUID")));
    }

    @Override
    public String getDisplayText() {
        return "Gym Boss: " + Teams.getPlayerTeam(getPlayerUUID()).colorChat() + UUIDManager.getPlayerName(getPlayerUUID());
    }

    public ResourceLocation getSkin() {
        ResourceLocation resourcelocation = DefaultPlayerSkin.getDefaultSkinLegacy();
        GameProfile profile = new GameProfile(getPlayerUUID(), UUIDManager.getPlayerName(getPlayerUUID()));
        if (profile != null) {
            if (CustomSkinManager.instance.loadSkin(getPlayerUUID()) != null) {
                resourcelocation = CustomSkinManager.instance.loadSkin(getPlayerUUID());
            }
        }
        return resourcelocation;
    }

    public void setPlayerUUID(UUID playerUUID) {
        this.dataWatcher.updateObject(20, playerUUID.toString());
    }

    public UUID getPlayerUUID() {
        return UUID.fromString(this.dataWatcher.getWatchableObjectString(20));
    }

    public void loadPokemon(EntityPixelmon pixelmon) {
        this.getPokemonStorage().addToParty(pixelmon);
    }

}
