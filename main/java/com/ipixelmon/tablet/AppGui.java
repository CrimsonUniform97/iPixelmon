package com.ipixelmon.tablet;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.app.gallery.GalleryGui;
import com.ipixelmon.util.GuiUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.util.Dimension;
import org.lwjgl.util.Rectangle;

import java.io.IOException;


/**
 * Created by colby on 12/31/2016.
 */
public abstract class AppGui extends GuiScreen {

    protected Rectangle screenBounds = new Rectangle();
    protected Rectangle bgBounds = new Rectangle();

    private static final ResourceLocation bgTexture = new ResourceLocation(iPixelmon.id, "textures/gui/tablet/tablet.png");
    private static final ResourceLocation defaultWallpaper = new ResourceLocation(iPixelmon.id, "textures/gui/tablet/default_wallpaper.png");

    public AppGui(Object[] objects) {}

    @Override
    public final void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.enableBlend();
        drawTablet();
        drawWallpaper();
        drawScreen(mouseX, mouseY, Mouse.getDWheel(), partialTicks);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if(!(Minecraft.getMinecraft().currentScreen instanceof GuiTablet) && keyCode == Keyboard.KEY_ESCAPE) {
            Minecraft.getMinecraft().displayGuiScreen(new GuiTablet(null));
        } else {
            super.keyTyped(typedChar, keyCode);
        }
    }

    @Override
    public void initGui() {
        super.initGui();

        Dimension bgSize = new Dimension(614, 378);

        Dimension boundary = GuiUtil.getScaledDimension(bgSize, new Dimension(width, height));

        float ratioW = (float) boundary.getWidth() / (float) bgSize.getWidth();
        float ratioH = (float) boundary.getHeight() / (float) bgSize.getHeight();

        int xOffset = ((int) (ratioW * 39));
        int yOffset = ((int) (ratioH * 38));

        bgBounds = new Rectangle((width - boundary.getWidth()) / 2, (height - boundary.getHeight()) / 2, boundary.getWidth(), boundary.getHeight());
        screenBounds = new Rectangle(bgBounds.getX() + xOffset, bgBounds.getY() + yOffset, boundary.getWidth() - (xOffset * 2), boundary.getHeight() - (yOffset * 2));
    }

    public abstract void drawScreen(int mouseX, int mouseY, int dWheel, float partialTicks);

    private void drawWallpaper() {
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        if (GalleryGui.getWallpaper() != null) {
            GalleryGui.getWallpaper().drawWallpaper(screenBounds.getX(), screenBounds.getY(),
                    screenBounds.getWidth(), screenBounds.getHeight());
        } else {
            mc.getTextureManager().bindTexture(defaultWallpaper);
            GuiUtil.drawImage(screenBounds.getX(), screenBounds.getY(),
                    screenBounds.getWidth(), screenBounds.getHeight());
        }

        GlStateManager.disableTexture2D();
        GlStateManager.color(0/255f, 0/255f, 0/255f, 128f/255f);
        this.drawTexturedModalRect(screenBounds.getX(), screenBounds.getY(), 0, 0,
                screenBounds.getWidth(), screenBounds.getHeight());
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.enableTexture2D();
    }

    private void drawTablet() {
        mc.getTextureManager().bindTexture(bgTexture);
        GuiUtil.drawImage(bgBounds.getX(), bgBounds.getY(), bgBounds.getWidth(), bgBounds.getHeight());
    }

}
