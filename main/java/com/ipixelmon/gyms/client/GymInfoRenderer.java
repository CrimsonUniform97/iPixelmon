package com.ipixelmon.gyms.client;

import com.google.common.base.Function;
import com.ipixelmon.gyms.BlockGymInfo;
import com.ipixelmon.iPixelmon;
import com.pixelmonmod.pixelmon.worldGeneration.structure.gyms.GymInfo;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.BlockModelRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.renderer.block.model.ModelBlockDefinition;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.TRSRTransformation;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.List;

/**
 * Created by colby on 10/16/2016.
 */
public class GymInfoRenderer extends TileEntitySpecialRenderer {

    Function<ResourceLocation, TextureAtlasSprite> textureGetter = new Function<ResourceLocation, TextureAtlasSprite>() {
        public TextureAtlasSprite apply(ResourceLocation location) {
            return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
        }
    };

    @Override
    public void renderTileEntityAt(TileEntity te, double x, double y, double z, float partialTicks, int destroyStage) {
        TileEntityGymInfo tile = (TileEntityGymInfo) te;

        if (tile != null) {
            Tessellator tessellator = Tessellator.getInstance();
        // TODO: Get rendering to work.
            WorldRenderer worldRenderer = tessellator.getWorldRenderer();
            GL11.glPushMatrix();
            GL11.glTranslated(x, y, z);
            IModel crystalModel = null;
            try {
                crystalModel = ModelLoaderRegistry.getModel(new ResourceLocation("ipixelmon:block/gym_info"));
                this.bindTexture(new ResourceLocation("minecraft:textures/blocks/iron_block.png"));
            } catch (IOException e) {
                crystalModel = ModelLoaderRegistry.getMissingModel();
                e.printStackTrace();
            }
            IBakedModel bakedModel = crystalModel.bake((TRSRTransformation.identity()), Attributes.DEFAULT_BAKED_FORMAT, textureGetter);
            worldRenderer.begin(7, Attributes.DEFAULT_BAKED_FORMAT);// StartDrawingQuads

            List<BakedQuad> generalQuads = bakedModel.getGeneralQuads();
            for (BakedQuad q : generalQuads)
            {
                int[] vd = q.getVertexData();
                worldRenderer.addVertexData(vd);
            }
            for (EnumFacing face : EnumFacing.values())
            {
                List<BakedQuad> faceQuads = bakedModel.getFaceQuads(face);
                for (BakedQuad q : faceQuads)
                {
                    int[] vd = q.getVertexData();
                    worldRenderer.addVertexData(vd);
                }
            }
            tessellator.draw();
            GL11.glPopMatrix();
        }
    }

    protected void renderLivingLabel(TileEntityGymInfo entityIn, String str, double x, double y, double z, int maxDistance) {
        double d0 = entityIn.getDistanceSq(this.rendererDispatcher.staticPlayerX, this.rendererDispatcher.staticPlayerY, this.rendererDispatcher.staticPlayerZ);

        if (d0 <= (double) (maxDistance * maxDistance)) {
            FontRenderer fontrenderer = this.getFontRenderer();
            float f = 1.6F;
            float f1 = 0.016666668F * f;
            GlStateManager.pushMatrix();
            GlStateManager.translate((float) x + 0.0F, (float) y + entityIn.height + 0.5F, (float) z);
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
        }
    }

}
