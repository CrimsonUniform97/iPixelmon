package com.ipixelmon.gym;

import com.ipixelmon.gym.packet.EntityTrainerSyncPacket;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.team.TeamMod;
import com.ipixelmon.util.PixelmonAPI;
import com.ipixelmon.util.SkinUtil;
import com.ipixelmon.uuidmanager.UUIDManager;
import com.mojang.authlib.GameProfile;
import com.pixelmonmod.pixelmon.AI.AITrainerInBattle;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.comm.SetTrainerData;
import com.pixelmonmod.pixelmon.config.PixelmonEntityList;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.enums.*;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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
        this.tasks.addTask(0, new AITrainerInBattle(this));
        setPosition(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
        setEncounterMode(EnumEncounterMode.Unlimited);
        this.getPokemonStorage().addToParty(pixelmon);
        setAIMode(EnumTrainerAI.StandStill);
        setBossMode(EnumBossMode.NotBoss);
        setBattleType(EnumBattleType.Single);
        setBattleAIMode(EnumBattleAIMode.Tactical);
        this.playerName = TeamMod.getPlayerTeam(playerID).colorChat().toString() + UUIDManager.getPlayerName(playerID);
        enablePersistence();

        update(new SetTrainerData(playerName, "Bring it on!", "Eat shit!", "Damn you!", 12, new ItemStack[]{}));
    }

    @Override
    public float getEyeHeight() {
        return this.height * 0.35f;
    }

    @Override
    public void initAI() {
    }

    @Override
    public void onUpdate() {
        super.onUpdate();

        if (!worldObj.isRemote) {
            Gym gym = GymAPI.Server.getGym(getPosition());

            if(gym == null) {
                worldObj.removeEntity(this);
                return;
            }

            if(!gym.getSeats().containsKey(getPosition().down(1))) {
                worldObj.removeEntity(this);
                return;
            }

            NBTTagCompound tagCompound = new NBTTagCompound();
            writeToNBT(tagCompound);
            iPixelmon.network.sendToAllAround(new EntityTrainerSyncPacket(getEntityId(), tagCompound),
                    new NetworkRegistry.TargetPoint(dimension, posX, posY, posZ, 40));
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound tagCompund) {
        super.readFromNBT(tagCompund);
        if(pixelmon == null) {
            pixelmon = (EntityPixelmon) PixelmonEntityList.createEntityByName(tagCompund.getString("pixelmonName"), worldObj);
            PixelmonData pixelmonData = PixelmonAPI.pixelmonDataFromString(tagCompund.getString("pixelmon"));
            pixelmonData.updatePokemon(pixelmon.getEntityData());
            playerName = tagCompund.getString("playerName");
            playerID = UUID.fromString(tagCompund.getString("playerID"));
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound tagCompund) {
        super.writeToNBT(tagCompund);
        tagCompund.setString("pixelmonName", pixelmon.getName());
        tagCompund.setString("pixelmon", PixelmonAPI.pixelmonDataToString(new PixelmonData(pixelmon)));
        tagCompund.setString("playerName", playerName);
        tagCompund.setString("playerID", playerID.toString());
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
