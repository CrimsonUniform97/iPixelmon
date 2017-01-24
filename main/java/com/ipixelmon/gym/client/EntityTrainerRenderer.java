package com.ipixelmon.gym.client;

import com.ipixelmon.gym.EntityTrainer;
import com.ipixelmon.util.PixelmonAPI;
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
        EntityTrainer trainer = (EntityTrainer) npc;

        if (trainer.getPlayerID() == null) return;

        if (com.pixelmonmod.pixelmon.client.ClientProxy.battleManager.battleEnded) {
            if (trainer.getPixelmon() != null) {
                if (d == 0.0 && d1 == 0.0 && d2 == 0.0) return;
                GlStateManager.pushMatrix();
                GlStateManager.translate(d, d1 + 2.3D, d2);
                GlStateManager.rotate(180, 1, 0, 0);
                PixelmonAPI.Client.renderPixelmon3D(trainer.getPixelmon(), 0, 0, 0,0.3F, trainer.pixelmonDisplayRotY += 0.66F);
                GlStateManager.popMatrix();
            }
        }

    }

    @Override
    protected ResourceLocation getEntityTexture(EntityNPC entity) {
        EntityTrainer gymLeader = (EntityTrainer) entity;
        return gymLeader.getSkin();
    }

}
