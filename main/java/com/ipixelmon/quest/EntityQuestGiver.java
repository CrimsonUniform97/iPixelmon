package com.ipixelmon.quest;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIWatchClosest2;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;

/**
 * Created by colbymchenry on 12/28/16.
 */
public class EntityQuestGiver extends EntityLiving {

    public EntityQuestGiver(World worldIn) {
        super(worldIn);
        this.setSize(0.6F, 1.8F);
        this.tasks.taskEntries.clear();
        this.targetTasks.taskEntries.clear();
        this.tasks.addTask(0, new EntityAIWatchClosest2(this, EntityPlayer.class, 3.0F, 1.0F));

    }

    @Override
    protected boolean interact(EntityPlayer player) {
        return super.interact(player);
    }

    /**
     * Disables damage to the entity by not having any code inside method
     */
    @Override
    protected void damageEntity(DamageSource damageSrc, float damageAmount) {
    }

}
