package com.ipixelmon.gyms.client;

import com.ipixelmon.gyms.EntityGymLeader;
import com.ipixelmon.gyms.Gym;
import com.ipixelmon.gyms.Gyms;
import com.ipixelmon.landcontrol.LandControl;
import com.pixelmonmod.pixelmon.client.*;
import com.pixelmonmod.pixelmon.client.ClientProxy;
import com.pixelmonmod.pixelmon.client.gui.battles.ClientBattleManager;
import com.pixelmonmod.pixelmon.client.render.RenderNPC;
import com.pixelmonmod.pixelmon.client.render.RenderPixelmon;
import com.pixelmonmod.pixelmon.entities.npcs.EntityNPC;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;

import java.util.Map;

/**
 * Created by Colby McHenry on 9/28/2016.
 */
public class RenderGymLeader extends RenderNPC {

    private RenderPixelmon renderPixelmon;

    public RenderGymLeader(RenderManager manager) {
        super(manager);
        renderPixelmon = new RenderPixelmon(manager);
    }

    @Override
    public void doRender(EntityNPC npc, double d, double d1, double d2, float f, float f1) {
        super.doRender(npc, d, d1, d2, f, f1);

        if (!(npc instanceof EntityGymLeader)) return;

        EntityGymLeader gymLeader = (EntityGymLeader) npc;
        if(ClientProxy.battleManager.battleEnded) {
            if (gymLeader.getPixelmon() != null) {
                GlStateManager.pushMatrix();
                GlStateManager.translate(d, d1 + 2D, d2);
                float scale = 0.05f;
                GlStateManager.translate(scale, scale, scale);
                GlStateManager.rotate(gymLeader.clientDisplayRot += 0.66F, 0f, 1f, 0f);
                renderPixelmon.renderPixelmon(gymLeader.getPixelmon(), 0, 0, 0, f, f1, false);
                GlStateManager.translate(-scale, -scale, -scale);
                GlStateManager.popMatrix();
                if (gymLeader.clientDisplayRot >= 360f) {
                    gymLeader.clientDisplayRot = 0F;
                }
            }
        }

    }

    @Override
    protected ResourceLocation getEntityTexture(EntityNPC entity) {
        EntityGymLeader gymLeader = (EntityGymLeader) entity;
        return gymLeader.getSkin();
    }

}
