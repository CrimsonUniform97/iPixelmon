package com.ipixelmon.gyms;

import com.ipixelmon.uuidmanager.UUIDManager;
import com.pixelmonmod.pixelmon.entities.npcs.NPCTrainer;
import net.minecraft.entity.ai.*;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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

    public void setPlayerUUID(UUID playerUUID) {
        this.dataWatcher.updateObject(20, playerUUID.toString());
    }

    public UUID getPlayerUUID() {
        return UUID.fromString(this.dataWatcher.getWatchableObjectString(20));
    }
}
