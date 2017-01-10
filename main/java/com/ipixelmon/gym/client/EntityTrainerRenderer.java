package com.ipixelmon.gym.client;

import com.ipixelmon.gym.EntityTrainer;
import com.pixelmonmod.pixelmon.client.render.RenderNPC;
import com.pixelmonmod.pixelmon.client.render.RenderPixelmon;
import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;

/**
 * Created by colby on 1/10/2017.
 */
public class EntityTrainerRenderer extends RenderNPC {

    private RenderPixelmon renderPixelmon;

    public EntityTrainerRenderer(RenderManager manager) {
        super(manager);
        renderPixelmon = new RenderPixelmon(manager);
    }

    @Override
    public void doRender(EntityNPC npc, double d, double d1, double d2, float f, float f1) {
        super.doRender(npc, d, d1, d2, f, f1);

        if (!(npc instanceof EntityTrainer)) return;

        EntityTrainer gymLeader = (EntityTrainer) npc;
        if (com.pixelmonmod.pixelmon.client.ClientProxy.battleManager.battleEnded) {
            if (gymLeader.getPixelmon() != null) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(d, d1 + 2D, d2);
                float scale = 0.05f;
                GlStateManager.translate(scale, scale, scale);
                GlStateManager.rotate(gymLeader.pixelmonDisplayRotY += 0.66F, 0f, 1f, 0f);
                renderPixelmon.renderPixelmon(gymLeader.getPixelmon(), 0, 0, 0, f, f1, false);
                GlStateManager.translate(-scale, -scale, -scale);
                GlStateManager.popMatrix();

                if (gymLeader.pixelmonDisplayRotY >= 360f) {
                    gymLeader.pixelmonDisplayRotY = 0F;
                }

            }
        }

    }

    @Override
    protected ResourceLocation getEntityTexture(EntityNPC entity) {
        EntityTrainer gymLeader = (EntityTrainer) entity;
        return gymLeader.getSkin();
    }

}
