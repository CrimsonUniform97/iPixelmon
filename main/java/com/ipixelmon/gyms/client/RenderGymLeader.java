package com.ipixelmon.gyms.client;

import com.ipixelmon.gyms.EntityGymLeader;
import com.pixelmonmod.pixelmon.client.render.RenderNPC;
import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

/**
 * Created by Colby McHenry on 9/28/2016.
 */
public class RenderGymLeader extends RenderNPC {

    public RenderGymLeader(RenderManager manager) {
        super(manager);
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityNPC entity) {
        EntityGymLeader gymLeader = (EntityGymLeader) entity;
        return gymLeader.getSkin();
    }

}
