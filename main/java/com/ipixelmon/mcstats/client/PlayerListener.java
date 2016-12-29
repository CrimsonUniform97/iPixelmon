package com.ipixelmon.mcstats.client;

import com.ipixelmon.mcstats.McStatsAPI;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.opengl.GL11;

/**
 * Created by colbymchenry on 12/29/16.
 */
public class PlayerListener {

    @SubscribeEvent
    public void worldRender(RenderWorldLastEvent event) {
        if(McStatsAPI.Client.renderPos != null) {
            // TODO: Work on animation, look at NotificationOverlay
            RenderFloatingText(new String[]{"" + McStatsAPI.Client.renderEXP}, McStatsAPI.Client.renderPos.getX() + 0.5f,
                    McStatsAPI.Client.renderPos.getY(), McStatsAPI.Client.renderPos.getZ() + 0.5f, 0xed4e1e,
                    false, event.partialTicks);
        }
    }


    public static void RenderFloatingText(String[] text, float x, float y, float z, int color, boolean renderBlackBox, float partialTickTime) {
        //Thanks to Electric-Expansion mod for the majority of this code
        //https://github.com/Alex-hawks/Electric-Expansion/blob/master/src/electricexpansion/client/render/RenderFloatingText.java
        Minecraft mc = Minecraft.getMinecraft();
        RenderManager renderManager = mc.getRenderManager();
        FontRenderer fontRenderer = mc.fontRendererObj;

        float playerX = (float) (mc.thePlayer.lastTickPosX + (mc.thePlayer.posX - mc.thePlayer.lastTickPosX) * partialTickTime);
        float playerY = (float) (mc.thePlayer.lastTickPosY + (mc.thePlayer.posY - mc.thePlayer.lastTickPosY) * partialTickTime);
        float playerZ = (float) (mc.thePlayer.lastTickPosZ + (mc.thePlayer.posZ - mc.thePlayer.lastTickPosZ) * partialTickTime);

        float dx = x - playerX;
        float dy = y - playerY;
        float dz = z - playerZ;
        float distance = (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
        float multiplier = distance / 120f; //mobs only render ~120 blocks away
        float scale = 0.45f * multiplier;

        GL11.glColor4f(1f, 1f, 1f, 0.5f);
        GL11.glPushMatrix();
        GL11.glTranslatef(dx, dy, dz);
        GL11.glRotatef(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
        GL11.glScalef(-scale, -scale, scale);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        int textWidth = 0;
        for (String thisMessage : text) {
            int thisMessageWidth = mc.fontRendererObj.getStringWidth(thisMessage);

            if (thisMessageWidth > textWidth)
                textWidth = thisMessageWidth;
        }

        int lineHeight = 10;

        if (renderBlackBox) {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glBegin(GL11.GL_QUADS);
            int stringMiddle = textWidth / 2;
            GL11.glColor4f(0.0F, 0.0F, 0.0F, 0.5F);
            GL11.glVertex3d(-stringMiddle - 1, -1 + 0, 0.0D);
            GL11.glVertex3d(-stringMiddle - 1, 8 + lineHeight * text.length - lineHeight, 0.0D);
            GL11.glVertex3d(stringMiddle + 1, 8 + lineHeight * text.length - lineHeight, 0.0D);
            GL11.glVertex3d(stringMiddle + 1, -1 + 0, 0.0D);
            GL11.glEnd();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glColor4f(1f, 1f, 1f, 1f);
        }

        int i = 0;
        for (String message : text) {
            fontRenderer.drawString(message, -textWidth / 2, i * lineHeight, color);
            i++;
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glPopMatrix();
    }

}
