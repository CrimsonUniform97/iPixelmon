package com.ipixelmon.gyms.client;

import com.ipixelmon.gyms.BlockGymInfo;
import com.ipixelmon.gyms.TileEntityGymInfo;
import com.ipixelmon.pixelegg.client.GuiUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.opengl.GL11;

/**
 * Created by colby on 10/16/2016.
 */
public class GymInfoRenderer extends TileEntitySpecialRenderer {

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage) {
        TileEntityGymInfo tile = (TileEntityGymInfo) te;

        if (tile != null) {
            BlockPos pos = new BlockPos(x,y,z);

            // Create Empty EntityItem to help with Rendering Positionining.
            EntityItem entity = new EntityItem(te.getWorld(),pos.getX(),pos.getY(), pos.getZ());
            if (entity.ticksExisted == 0)
            {
                entity.lastTickPosX = entity.posX;
                entity.lastTickPosY = entity.posY;
                entity.lastTickPosZ = entity.posZ;
            }

            GlStateManager.pushMatrix();
            GlStateManager.translate(x + 0.5D, y + 0.5D, z + 0.5D);
            GL11.glScaled(2D,2D,2D);

            Minecraft.getMinecraft().getRenderItem().renderItem(new ItemStack(Item.getItemFromBlock(BlockGymInfo.instance)), ItemCameraTransforms.TransformType.FIXED);
            GlStateManager.popMatrix();

            if(tile.getGym() != null) {
                try {
                    GlStateManager.pushMatrix();
                    GlStateManager.translate(x + 0.5D, y + 0.5D, z + 0.5D);
                    // TODO: Work on making this a little more... pretty.
                    renderLivingLabel(tile, EnumChatFormatting.RED + "Power: ★ " + tile.getGym().getPower(), 0, 3, 0, 50);
                    GlStateManager.popMatrix();
                }catch(Exception e){}
            }
        }
    }

    protected void renderLivingLabel(TileEntityGymInfo entityIn, String str, double x, double y, double z, int maxDistance) {
        double d0 = entityIn.getDistanceSq(this.rendererDispatcher.staticPlayerX, this.rendererDispatcher.staticPlayerY, this.rendererDispatcher.staticPlayerZ);

        if (d0 <= (double) (maxDistance * maxDistance)) {
            FontRenderer fontrenderer = this.getFontRenderer();
            fontrenderer.setUnicodeFlag(true);
            float f = 1.6F;
            float f1 = 0.016666668F * f;
            GlStateManager.pushMatrix();
            GlStateManager.translate((float) x + 0.0F, (float) y + 0.5F, (float) z);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-this.rendererDispatcher.entityYaw, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(this.rendererDispatcher.entityPitch, 1.0F, 0.0F, 0.0F);
            GlStateManager.scale(-f1, -f1, f1);
            GlStateManager.disableLighting();
            GlStateManager.depthMask(false);
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            int i = 0;

            if (str.equals("deadmau5")) {
                i = -10;
            }

            int j = fontrenderer.getStringWidth(str) / 2;
            GlStateManager.disableTexture2D();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
            worldrenderer.pos((double) (-j - 1), (double) (-1 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            worldrenderer.pos((double) (-j - 1), (double) (8 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            worldrenderer.pos((double) (j + 1), (double) (8 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            worldrenderer.pos((double) (j + 1), (double) (-1 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            tessellator.draw();
            GlStateManager.enableTexture2D();
            fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, 553648127);
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            fontrenderer.drawString(str, -fontrenderer.getStringWidth(str) / 2, i, -1);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
            fontrenderer.setUnicodeFlag(false);
        }
    }

}
