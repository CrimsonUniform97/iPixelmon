package ipixelmon.pokeegg.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

public class OpenGLHelper
{

    public static void transform(int x, int y, int width, int height, int rotX, int rotY, int rotZ, int scale)
    {
        //move to position
        GlStateManager.translate(x, y, 0);

        // translate to center of egg
        int z = 100;
        GlStateManager.translate(width / 2,height / 2, z);
        Minecraft.getMinecraft().fontRendererObj.drawString("|", 0, 0, 0xFFFFFF);

        // TODO: Rotating around the text, odd. Need to fix this
        // scale
        GlStateManager.scale(scale, scale, scale);

        // rotate
        GlStateManager.rotate(rotX, 1, 0, 0);
        GlStateManager.rotate(rotY, 0, 1, 0);
        GlStateManager.rotate(rotZ, 0, 0, 1);

        // translate back
        GlStateManager.translate(-(width / 2), -(height / 2), -z);
    }

}
