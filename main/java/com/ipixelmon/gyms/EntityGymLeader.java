package com.ipixelmon.gyms;

import com.ipixelmon.gyms.client.CustomSkinManager;
import com.ipixelmon.teams.Teams;
import com.ipixelmon.uuidmanager.UUIDManager;
import com.mojang.authlib.GameProfile;
import com.pixelmonmod.pixelmon.RandomHelper;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.UUID;

/**
 * Created by Colby McHenry on 9/28/2016.
 */
public class EntityGymLeader extends NPCTrainer {

    public EntityGymLeader(World world) {
        super(world);

        clearAITasks();
        this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
    }

    @Override
    public void initAI() {
        clearAITasks();
        this.tasks.addTask(9, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
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
        if (profile != null)
        {
            if(CustomSkinManager.instance.loadSkin(getPlayerUUID()) != null) {
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
