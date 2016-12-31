package com.ipixelmon.util;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.*;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Dimension;

import java.awt.*;
import java.nio.FloatBuffer;

@SideOnly(Side.CLIENT)
public class GuiUtil {

    private static final Vec3 LIGHT0_POS = (new Vec3(0.20000000298023224D, 1.0D, -0.699999988079071D)).normalize();
    private static final Vec3 LIGHT1_POS = (new Vec3(-0.20000000298023224D, 1.0D, 0.699999988079071D)).normalize();

    private static FloatBuffer setColorBuffer(double p_74517_0_, double p_74517_2_, double p_74517_4_, double p_74517_6_) {
        /**
         * Update and return colorBuffer with the RGBA values passed as arguments
         */
        return setColorBuffer((float) p_74517_0_, (float) p_74517_2_, (float) p_74517_4_, (float) p_74517_6_);
    }

    /**
     * Update and return colorBuffer with the RGBA values passed as arguments
     */
    @SideOnly(Side.CLIENT)
    private static FloatBuffer colorBuffer = GLAllocation.createDirectFloatBuffer(16);

    private static FloatBuffer setColorBuffer(float p_74521_0_, float p_74521_1_, float p_74521_2_, float p_74521_3_) {
        colorBuffer.clear();
        colorBuffer.put(p_74521_0_).put(p_74521_1_).put(p_74521_2_).put(p_74521_3_);
        colorBuffer.flip();
        /** Float buffer used to set OpenGL material colors */
        return colorBuffer;
    }

    public void setBrightness(float f, float f1, float f2) {
        GlStateManager.enableLighting();
        GlStateManager.enableLight(0);
        GlStateManager.enableLight(1);
        GlStateManager.enableColorMaterial();
        GlStateManager.colorMaterial(1032, 5634);
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, (FloatBuffer) setColorBuffer(LIGHT0_POS.xCoord, LIGHT0_POS.yCoord, LIGHT0_POS.zCoord, 0.0D));
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, (FloatBuffer) setColorBuffer(f1, f1, f1, 1.0F));
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, (FloatBuffer) setColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, (FloatBuffer) setColorBuffer(f2, f2, f2, 1.0F));
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_POSITION, (FloatBuffer) setColorBuffer(LIGHT1_POS.xCoord, LIGHT1_POS.yCoord, LIGHT1_POS.zCoord, 0.0D));
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_DIFFUSE, (FloatBuffer) setColorBuffer(f1, f1, f1, 1.0F));
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_AMBIENT, (FloatBuffer) setColorBuffer(0.0F, 0.0F, 0.0F, 1.0F));
        GL11.glLight(GL11.GL_LIGHT1, GL11.GL_SPECULAR, (FloatBuffer) setColorBuffer(f2, f2, f2, 1.0F));
        GlStateManager.shadeModel(7424);
        GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, (FloatBuffer) setColorBuffer(f, f, f, 1.0F));
        GlStateManager.enableBlend();
    }

    public Dimension getScaledDimension(Dimension imgSize, Dimension boundary) {

        int original_width = imgSize.getWidth();
        int original_height = imgSize.getHeight();
        int bound_width = boundary.getWidth();
        int bound_height = boundary.getHeight();
        int new_width = original_width;
        int new_height = original_height;

        // first check if we need to scale width
        if (original_width > bound_width) {
            //scale width to fit
            new_width = bound_width;
            //scale height to maintain aspect ratio
            new_height = (new_width * original_height) / original_width;
        }

        // then check if we need to scale even with the new height
        if (new_height > bound_height) {
            //scale height to fit instead
            new_height = bound_height;
            //scale width to maintain aspect ratio
            new_width = (new_height * original_width) / original_height;
        }

        return new Dimension(new_width, new_height);
    }

    public void drawImage(float x, float y, float width, float height) {
        GuiHelper.drawImageQuad(x, y, width, height, 0.0D, 0.0D, 1.0D, 1.0D, 0.0F);
    }

    public void drawRectFill(int x, int y, int width, int height, Color color) {
        GL11.glDisable(GL11.GL_CULL_FACE);
        GlStateManager.disableTexture2D();
        GL11.glColor4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);

        GL11.glBegin(GL11.GL_QUADS);
        {
            GL11.glVertex2f(x, y);
            GL11.glVertex2f(x + width, y);
            GL11.glVertex2f(x + width, y + height);
            GL11.glVertex2f(x, y + height);
        }
        GL11.glEnd();

        GL11.glColor4f(1, 1, 1, 1);
        GlStateManager.enableTexture2D();
        GL11.glEnable(GL11.GL_CULL_FACE);
    }

    public void drawRectFillBorder(int x, int y, int width, int height, Color fill, Color border, int borderThickness) {
        drawRectFill(x, y, width, height, border);
        drawRectFill(x + borderThickness, y + borderThickness,
                width - (borderThickness * 2), height - (borderThickness * 2),
                fill);
    }

    public void drawHoveringText(java.util.List<String> textLines, int x, int y) {
        float zLevel = 400f;
        RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
        FontRenderer font = Minecraft.getMinecraft().fontRendererObj;

        if (!textLines.isEmpty()) {
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            int i = 0;

            for (String s : textLines) {
                int j = font.getStringWidth(s);

                if (j > i) {
                    i = j;
                }
            }

            int l1 = x + 12;
            int i2 = y - 12;
            int k = 8;

            if (textLines.size() > 1) {
                k += 2 + (textLines.size() - 1) * 10;
            }

//            if (l1 + i > this.width)
//            {
//                l1 -= 28 + i;
//            }
//
//            if (i2 + k + 6 > this.height)
//            {
//                i2 = this.height - k - 6;
//            }

            zLevel = 300.0F;
            itemRender.zLevel = 300.0F;
            int l = -267386864;
            drawGradientRect(l1 - 3, i2 - 4, l1 + i + 3, i2 - 3, l, l);
            drawGradientRect(l1 - 3, i2 + k + 3, l1 + i + 3, i2 + k + 4, l, l);
            drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 + k + 3, l, l);
            drawGradientRect(l1 - 4, i2 - 3, l1 - 3, i2 + k + 3, l, l);
            drawGradientRect(l1 + i + 3, i2 - 3, l1 + i + 4, i2 + k + 3, l, l);
            int i1 = 1347420415;
            int j1 = (i1 & 16711422) >> 1 | i1 & -16777216;
            drawGradientRect(l1 - 3, i2 - 3 + 1, l1 - 3 + 1, i2 + k + 3 - 1, i1, j1);
            drawGradientRect(l1 + i + 2, i2 - 3 + 1, l1 + i + 3, i2 + k + 3 - 1, i1, j1);
            drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 - 3 + 1, i1, i1);
            drawGradientRect(l1 - 3, i2 + k + 2, l1 + i + 3, i2 + k + 3, j1, j1);

            for (int k1 = 0; k1 < textLines.size(); ++k1) {
                String s1 = (String) textLines.get(k1);
                font.drawStringWithShadow(s1, (float) l1, (float) i2, -1);

                if (k1 == 0) {
                    i2 += 2;
                }

                i2 += 10;
            }

            zLevel = 0.0F;
            itemRender.zLevel = 0.0F;
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
        }
    }

    public void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
        float zLevel = 300f;
        float f = (float) (startColor >> 24 & 255) / 255.0F;
        float f1 = (float) (startColor >> 16 & 255) / 255.0F;
        float f2 = (float) (startColor >> 8 & 255) / 255.0F;
        float f3 = (float) (startColor & 255) / 255.0F;
        float f4 = (float) (endColor >> 24 & 255) / 255.0F;
        float f5 = (float) (endColor >> 16 & 255) / 255.0F;
        float f6 = (float) (endColor >> 8 & 255) / 255.0F;
        float f7 = (float) (endColor & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos((double) right, (double) top, (double) zLevel).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos((double) left, (double) top, (double) zLevel).color(f1, f2, f3, f).endVertex();
        worldrenderer.pos((double) left, (double) bottom, (double) zLevel).color(f5, f6, f7, f4).endVertex();
        worldrenderer.pos((double) right, (double) bottom, (double) zLevel).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void renderLabel(String str, double x, double y, double z, float scale, boolean renderBox, FontRenderer fontRenderer, RenderManager renderManager) {
        float f1 = 0.016666668F * scale;
        GlStateManager.pushMatrix();
        GlStateManager.translate((float) x + 0.0F, (float) y, (float) z);
        GL11.glNormal3f(0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(-renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate(renderManager.playerViewX, 1.0F, 0.0F, 0.0F);
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

        if(renderBox) {
            int j = fontRenderer.getStringWidth(str) / 2;
            GlStateManager.disableTexture2D();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
            worldrenderer.pos((double) (-j - 1), (double) (-1 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            worldrenderer.pos((double) (-j - 1), (double) (8 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            worldrenderer.pos((double) (j + 1), (double) (8 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            worldrenderer.pos((double) (j + 1), (double) (-1 + i), 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            tessellator.draw();
            GlStateManager.enableTexture2D();
        }
        fontRenderer.drawString(str, -fontRenderer.getStringWidth(str) / 2, i, 553648127);
        GlStateManager.enableDepth();
        GlStateManager.depthMask(true);
        fontRenderer.drawString(str, -fontRenderer.getStringWidth(str) / 2, i, -1);
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
    }

    public static void RenderFloatingText(String[] text, float x, float y, float z, int color, boolean renderBlackBox, float partialTickTime, float scale) {
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
        scale *= 0.45f;

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
