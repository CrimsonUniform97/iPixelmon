package com.ipixelmon.gym;

import com.ipixelmon.team.TeamMod;
import com.ipixelmon.util.ArrayUtil;
import com.ipixelmon.util.PixelmonAPI;
import com.ipixelmon.util.SkinUtil;
import com.ipixelmon.uuidmanager.UUIDManager;
import com.mojang.authlib.GameProfile;
import com.pixelmonmod.pixelmon.AI.AITrainerInBattle;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.*;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.actors.threadpool.Arrays;

import java.util.List;
import java.util.UUID;

public class EntityTrainer extends NPCTrainer implements Comparable<EntityTrainer> {

    private String playerName;
    private UUID playerID;
    private EntityPixelmon pixelmon;

    public float pixelmonDisplayRotY = 0.0F;

    public EntityTrainer(World world) {
        super(world);
    }

    // http://pixelmonmod.com/wiki/index.php?title=NPC_Editor
    public EntityTrainer(World world, BlockPos pos, UUID playerID, EntityPixelmon pixelmon) {
        super(world);
        this.pixelmon = pixelmon;
        this.playerID = playerID;
        this.targetTasks.taskEntries.clear();
        this.tasks.taskEntries.clear();
        this.tasks.addTask(0, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));
        this.tasks.addTask(1, new AITrainerInBattle(this));
        setPosition(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
        setEncounterMode(EnumEncounterMode.Unlimited);
        this.getPokemonStorage().addToParty(pixelmon);
        setAIMode(EnumTrainerAI.StandStill);
        setBossMode(EnumBossMode.NotBoss);
        setBattleType(EnumBattleType.Single);
        setBattleAIMode(EnumBattleAIMode.Tactical);
        this.playerName = UUIDManager.getPlayerName(playerID);
        enablePersistence();

        sendData();
    }

    @Override
    public float getEyeHeight() {
        return this.height * 0.35f;
    }

    @Override
    public void initAI() {}

    public void sendData() {
        try {
            String[] array = new String[]{
                    playerID.toString(),
                    TeamMod.getPlayerTeam(playerID).colorChat().toString() + playerName,
                    PixelmonAPI.pixelmonDataToString(new PixelmonData(pixelmon))
            };

            this.dataWatcher.updateObject(20, ArrayUtil.toString(array));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @SideOnly(Side.CLIENT)
    public List<String> loadData() {
        String[] array = ArrayUtil.fromString(this.dataWatcher.getWatchableObjectString(20));
        if (array.length == 3 && !array[0].isEmpty() && !array[1].isEmpty() && !array[2].isEmpty()) {
            playerID = UUID.fromString(array[0]);
            playerName = array[1];
            PixelmonData pixelmonData = PixelmonAPI.pixelmonDataFromString(array[2]);

            if (pixelmon == null) {
                pixelmon = (EntityPixelmon) PixelmonEntityList.createEntityByName(pixelmonData.name, worldObj);
                pixelmonData.updatePokemon(pixelmon.getEntityData());
            }
        }

        return Arrays.asList(array);
    }

    public EntityPixelmon getPixelmon() {
        return pixelmon;
    }

    public UUID getPlayerID() {
        return playerID;
    }

    @Override
    public String getDisplayText() {
        return playerName;
    }

    @Override
    public IChatComponent getDisplayName() {
        return new ChatComponentText(playerName);
    }

    @Override
    public int compareTo(EntityTrainer o) {
        return o.getPlayerID().equals(getPlayerID()) ? 0 : -999;
    }

    /**
     * Disables damage to the entity by not having any code inside method
     */
    @Override
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
    }

    @SideOnly(Side.CLIENT)
    public ResourceLocation getSkin() {
        ResourceLocation resourcelocation = DefaultPlayerSkin.getDefaultSkinLegacy();

        if (playerID != null && playerName != null) {
            GameProfile profile = new GameProfile(playerID, playerName);
            if (profile != null) {
                if (SkinUtil.loadSkin(getPlayerID()) != null) {
                    resourcelocation = SkinUtil.loadSkin(getPlayerID());
                }
            }
        }

        return resourcelocation;
    }

    @Override
    public AxisAlignedBB getCollisionBoundingBox() {
        return null;
    }


    @Override
    public AxisAlignedBB getEntityBoundingBox() {
        float scale = 2.0f;
        return AxisAlignedBB.fromBounds(posX - scale, posY, posZ - scale, posX + scale, posY + scale, posZ + scale);
    }

}
