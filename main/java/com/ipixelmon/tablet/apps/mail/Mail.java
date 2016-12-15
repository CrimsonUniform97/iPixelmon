package com.ipixelmon.tablet.apps.mail;

import com.ipixelmon.tablet.client.App;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import org.lwjgl.opengl.GL11;

import java.awt.*;

/**
 * Created by colby on 12/14/2016.
 */
public class Mail extends App {

    public Mail(String name, boolean register) {
        super(name, register);
    }

    private GuiButton composeBtn;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    private void drawRectFill(int x, int y, int width, int height, Color color) {
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
}
