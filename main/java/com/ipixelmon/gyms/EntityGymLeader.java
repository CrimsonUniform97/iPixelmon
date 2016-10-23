package com.ipixelmon.gyms;

import com.ipixelmon.gyms.client.CustomSkinManager;
import com.ipixelmon.teams.Teams;
import com.ipixelmon.uuidmanager.UUIDManager;
import com.mojang.authlib.GameProfile;
import com.pixelmonmod.pixelmon.AI.AITrainerInBattle;
import com.pixelmonmod.pixelmon.battles.controller.participants.BattleParticipant;
import com.pixelmonmod.pixelmon.comm.SetTrainerData;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.npcs.registry.GymNPCData;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.*;
import com.pixelmonmod.pixelmon.storage.PCServer;
import com.pixelmonmod.pixelmon.worldGeneration.structure.gyms.GymInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Colby McHenry on 9/28/2016.
 */
public class EntityGymLeader extends NPCTrainer {

    public EntityGymLeader(World world) {
        super(world);
    }

    public float clientDisplayRot = 0f;

    private EntityPixelmon pixelmon;

    public EntityGymLeader(World world, BlockPos location, EntityPixelmon pixelmon, UUID playerUUID) {
        super(world);
        clearAITasks();
        this.tasks.addTask(0, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
        this.tasks.addTask(1, new AITrainerInBattle(this));
        update(new SetTrainerData("Name", "Bring it on!", "Eat shit!", "Damn you!", 12, new ItemStack[]{}));
        setPosition(location.getX() + 0.5D, location.getY() + 0.5D, location.getZ() + 0.5D);
        setEncounterMode(EnumEncounterMode.Unlimited);
        loadPokemon(this.pixelmon = pixelmon);

        setPlayerUUID(playerUUID);

        setAIMode(EnumTrainerAI.StandStill);
        setEncounterMode(EnumEncounterMode.Once);
        setBossMode(EnumBossMode.NotBoss);
        setBattleType(EnumBattleType.Single);
        setBattleAIMode(EnumBattleAIMode.Default);

    }

    @Override
    public void initAI() {
    }

    @Override
    public void loseBattle(ArrayList<BattleParticipant> opponents) {
        super.loseBattle(opponents);
//        worldObj.spawnEntityInWorld(this);
    }

    protected void clearAITasks() {
        tasks.taskEntries.clear();
        targetTasks.taskEntries.clear();
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompound) {
        super.writeToNBT(tagCompound);
        tagCompound.setString("playerUUID", getPlayerUUID() != null ? getPlayerUUID().toString() : this.getUniqueID().toString());
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompound) {
        super.readFromNBT(tagCompound);
        setPlayerUUID(UUID.fromString(tagCompound.getString("playerUUID")));
    }

    @Override
    public String getDisplayText() {
        return Teams.getPlayerTeam(getPlayerUUID()).colorChat() + UUIDManager.getPlayerName(getPlayerUUID());
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
        try {
            this.dataWatcher.updateObject(20, playerUUID.toString() + "," + pixelmon.getName() + "," + pixelmon.getIsShiny() + "," +
                    pixelmon.getForm() + "," + pixelmon.getLvl().getLevel() + "," + pixelmon.getGrowth().index);
        } catch (NullPointerException e) {
        }
    }

    public UUID getPlayerUUID() {
        return UUID.fromString(this.dataWatcher.getWatchableObjectString(20).split(",")[0]);
    }

    public void loadPokemon(EntityPixelmon pixelmon) {
        this.getPokemonStorage().addToParty(pixelmon);
    }

    public EntityPixelmon getPixelmon() {

        if (worldObj.isRemote) {
            String[] data = this.dataWatcher.getWatchableObjectString(20).split(",");
            pixelmon = (EntityPixelmon) PixelmonEntityList.createEntityByName(data[1], worldObj);
            pixelmon.setHealth(pixelmon.getMaxHealth());
            pixelmon.setIsShiny(Boolean.parseBoolean(data[2]));
            pixelmon.setForm(Integer.parseInt(data[3]));
            pixelmon.getLvl().setLevel(Integer.parseInt(data[4]));
            pixelmon.setGrowth(EnumGrowth.getGrowthFromIndex(Integer.parseInt(data[5])));
            pixelmon.caughtBall = EnumPokeballs.PokeBall;
            pixelmon.friendship.initFromCapture();
        }

        return pixelmon;
    }
}
