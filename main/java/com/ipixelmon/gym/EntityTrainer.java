package com.ipixelmon.gym;

import com.ipixelmon.team.TeamMod;
import com.ipixelmon.util.SkinUtil;
import com.ipixelmon.uuidmanager.UUIDManager;
import com.mojang.authlib.GameProfile;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.*;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import java.util.UUID;

public class EntityTrainer extends NPCTrainer implements Comparable<EntityTrainer> {

    private String playerName;
    private UUID playerID;
    private BlockPos pos;
    private EntityPixelmon pixelmon;

    public float pixelmonDisplayRotY = 0.0F;

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

//    public void setPlayerUUID(UUID playerUUID) {
//        try {
//            this.dataWatcher.updateObject(20, playerUUID.toString() + "," + pixelmon.getName() + "," + pixelmon.getIsShiny() + "," +
//                    pixelmon.getForm() + "," + pixelmon.getLvl().getLevel() + "," + pixelmon.getGrowth().index);
//        } catch (NullPointerException e) {
//        }
//    }
//    // TODO: Don't allow taking damage
//    public UUID getPlayerUUID() {
//        return UUID.fromString(this.dataWatcher.getWatchableObjectString(20).split(",")[0]);
//    }

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

    /**
     * Disables damage to the entity by not having any code inside method
     */
    @Override
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {}

    public ResourceLocation getSkin() {
        ResourceLocation resourcelocation = DefaultPlayerSkin.getDefaultSkinLegacy();
        GameProfile profile = new GameProfile(playerID, UUIDManager.getPlayerName(playerID));
        if (profile != null) {
            if (SkinUtil.loadSkin(playerID) != null) {
                resourcelocation = SkinUtil.loadSkin(playerID);
            }
        }
        return resourcelocation;
    }

    // TODO: May need to change this for culling
    @Override
    public AxisAlignedBB getEntityBoundingBox() {
        return super.getEntityBoundingBox();
    }
}
