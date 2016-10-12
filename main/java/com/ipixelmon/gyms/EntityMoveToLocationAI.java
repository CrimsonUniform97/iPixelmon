package com.ipixelmon.gyms;

import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

/**
 * Created by colby on 10/11/2016.
 */
public class EntityMoveToLocationAI extends EntityAIBase {

    private EntityCreature entity;
    private double xPosition;
    private double yPosition;
    private double zPosition;
    private double speed;

    public EntityMoveToLocationAI(EntityCreature creatureIn, double speedIn, double xPosition, double yPosition, double zPosition)
    {
        this.entity = creatureIn;
        this.speed = speedIn;
        this.setMutexBits(1);
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.zPosition = zPosition;
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    @Override
    public boolean shouldExecute()
    {
        return entity.posX != xPosition && entity.posY != yPosition && entity.posZ != zPosition;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    @Override
    public boolean continueExecuting()
    {
        return shouldExecute();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    @Override
    public void startExecuting()
    {
        System.out.println(shouldExecute());
        this.entity.getNavigator().tryMoveToXYZ(this.xPosition, this.yPosition, this.zPosition, this.speed);
    }

}
