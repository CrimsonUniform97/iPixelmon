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
        GlStateManager.pushMatrix();
        GlStateManager.translate(0, -1, 0);
        super.doRender(npc, d, d1, d2, f, f1);
        GlStateManager.popMatrix();

        if (!(npc instanceof EntityTrainer)) return;
        EntityTrainer trainer = (EntityTrainer) npc;

        if(trainer.getPlayerID() == null) {
            trainer.loadData();
            return;
        }

        if (com.pixelmonmod.pixelmon.client.ClientProxy.battleManager.battleEnded) {
            if (trainer.getPixelmon() != null) {
                if(d == 0.0 && d1 == 0.0 && d2 == 0.0) return;
                GlStateManager.pushMatrix();
                GlStateManager.translate(d, d1 + 1.3D, d2);

                if (trainer.getPixelmon().baseStats.canSurf) {
                    GlStateManager.translate(0.0F, 1.0F, 0.0F);
                }

                float scale = 0.05f;
                GlStateManager.translate(scale, scale, scale);
                GlStateManager.rotate(trainer.pixelmonDisplayRotY += 0.66F, 0f, 1f, 0f);
                renderPixelmon.renderPixelmon(trainer.getPixelmon(), 0, 0, 0, f, f1, false);
                GlStateManager.translate(-scale, -scale, -scale);
                GlStateManager.popMatrix();

                if (trainer.pixelmonDisplayRotY >= 360f) {
                    trainer.pixelmonDisplayRotY = 0F;
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
