package com.ipixelmon;

import com.ipixelmon.pixelbay.gui.ColorPicker;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.Vec3;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.Dimension;

import java.awt.*;
import java.nio.FloatBuffer;

public class GuiUtil
{

    public static final GuiUtil instance = new GuiUtil();

    private GuiUtil(){}

    private static final Vec3 LIGHT0_POS = (new Vec3(0.20000000298023224D, 1.0D, -0.699999988079071D)).normalize();
    private static final Vec3 LIGHT1_POS = (new Vec3(-0.20000000298023224D, 1.0D, 0.699999988079071D)).normalize();

    private static FloatBuffer setColorBuffer(double p_74517_0_, double p_74517_2_, double p_74517_4_, double p_74517_6_)
    {
        /**
         * Update and return colorBuffer with the RGBA values passed as arguments
         */
        return setColorBuffer((float)p_74517_0_, (float)p_74517_2_, (float)p_74517_4_, (float)p_74517_6_);
    }

    /**
     * Update and return colorBuffer with the RGBA values passed as arguments
     */
    private static FloatBuffer colorBuffer = GLAllocation.createDirectFloatBuffer(16);
    private static FloatBuffer setColorBuffer(float p_74521_0_, float p_74521_1_, float p_74521_2_, float p_74521_3_)
    {
        colorBuffer.clear();
        colorBuffer.put(p_74521_0_).put(p_74521_1_).put(p_74521_2_).put(p_74521_3_);
        colorBuffer.flip();
        /** Float buffer used to set OpenGL material colors */
        return colorBuffer;
    }

    public void setBrightness(float f, float f1, float f2)
    {
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

    public static void drawImage(float x, float y, float width, float height) {
        GuiHelper.drawImageQuad(x, y, width, height, 0.0D, 0.0D, 1.0D, 1.0D, 0.0F);
    }

    public static void drawRectFill(int x, int y, int width, int height, Color color) {
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
    public static void drawRectFillBorder(int x, int y, int width, int height, Color fill, Color border, int borderThickness) {
        GuiUtil.drawRectFill(x, y, width, height, border);
        GuiUtil.drawRectFill(x + borderThickness, y + borderThickness,
                width - (borderThickness * 2), height - (borderThickness * 2),
                fill);
    }

}
