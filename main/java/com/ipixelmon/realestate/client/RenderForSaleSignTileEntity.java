package com.ipixelmon.realestate.client;

import com.ipixelmon.realestate.RealEstateMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class RenderForSaleSignTileEntity extends TileEntitySpecialRenderer<ForSaleSignTileEntity> {

    static Entity entity = null;


    @Override
    public void renderTileEntityAt(ForSaleSignTileEntity te, double x, double y, double z, float partialTicks, int destroyStage){
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x + 0.5F, (float)y, (float)z + 0.5F);
        renderMob(te, x, y, z, partialTicks);
        GlStateManager.popMatrix();
    }

    /**
     * Render the mob inside the mob spawner.
     */
    public static void renderMob(ForSaleSignTileEntity te, double posX, double posY, double posZ, float partialTicks){


        if(entity == null)
            entity = new EntityGhast(te.getWorld());

        if (entity != null){
            GlStateManager.pushMatrix();
            GlStateManager.translate(0.0F, 0.5F, 0.0F);
            GlStateManager.rotate(te.getDirection().getDegree(), 0.0F, 1.0F, 0.0F);
            GlStateManager.scale(1, 1, 1);
            entity.setLocationAndAngles(posX, posY, posZ, 0.0F, 0.0F);

            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableLighting();
            GlStateManager.pushMatrix();
            // Translate to the center of the block and .9 points higher
            GlStateManager.translate(.5, .9, .5);
            GlStateManager.scale(.4f, .4f, .4f);


            GlStateManager.popMatrix();

            Minecraft.getMinecraft().getRenderItem().renderItem(
                    new ItemStack(Item.getItemFromBlock(RealEstateMod.saleSignStanding)),
                    ItemCameraTransforms.TransformType.NONE);

            GlStateManager.popMatrix();
        }
    }
}
