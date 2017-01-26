package com.ipixelmon.itemdisplay;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityGhast;

public class ItemDisplayBlockRenderer extends TileEntitySpecialRenderer<ItemDisplayBlockTileEntity> {

    static Entity entity = null;

    @Override
    public void renderTileEntityAt(ItemDisplayBlockTileEntity te, double x, double y, double z, float partialTicks, int destroyStage) {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.5F, (float) y, (float) z + 0.5F);
        renderMob(te, x, y, z, partialTicks);
        GlStateManager.popMatrix();
    }

    /**
     * Render the mob inside the mob spawner.
     */
    public static void renderMob(ItemDisplayBlockTileEntity te, double posX, double posY, double posZ, float partialTicks) {


        if (entity == null)
            entity = new EntityGhast(te.getWorld());

        if (entity != null) {
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F + te.getxOffset(), 0.5F + te.getyOffset(), 0.0F + te.getzOffset());
            GlStateManager.rotate(te.getDirection().getHorizontalAngle(), 0.0F, 1.0F, 0.0F);
            GlStateManager.scale(te.getScale(), te.getScale(), te.getScale());
            entity.setLocationAndAngles(posX, posY, posZ, 0.0F, 0.0F);

            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableLighting();
            GlStateManager.pushMatrix();
            // Translate to the center of the block and .9 points higher
            GlStateManager.translate(.5, .9, .5);
            GlStateManager.scale(.4f, .4f, .4f);

            GlStateManager.popMatrix();

            Minecraft.getMinecraft().getRenderItem().renderItem(te.getItemStack(), ItemCameraTransforms.TransformType.NONE);

            GlStateManager.popMatrix();
        }
    }

}
