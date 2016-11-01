package com.ipixelmon.tablet.client;

import com.ipixelmon.GuiUtil;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.client.apps.camera.Gallery;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.util.Dimension;
import org.lwjgl.util.Rectangle;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Created by colby on 10/28/2016.
 */
public class GuiTablet extends GuiScreen {

    public static App activeApp = null;

    private static final ResourceLocation bgTexture = new ResourceLocation(iPixelmon.id, "textures/gui/tablet/tablet.png");
    private static final ResourceLocation defaultWallpaper = new ResourceLocation(iPixelmon.id, "textures/gui/tablet/default_wallpaper.png");
    private Dimension bgSize = new Dimension(614, 378);
    private Rectangle bgBounds, screenBounds = new Rectangle();
    private static ResourceLocation fontTexture;

    public GuiTablet() {

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        drawTablet();
        drawWallpaper();
        drawApps();
        if (activeApp != null) activeApp.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick) {
        super.mouseClickMove(mouseX, mouseY, clickedMouseButton, timeSinceLastClick);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
    }

    @Override
    public void initGui() {
        super.initGui();
        Dimension boundary = GuiUtil.instance.getScaledDimension(bgSize, new Dimension(width, height));

        float ratioW = (float) boundary.getWidth() / (float) bgSize.getWidth();
        float ratioH = (float) boundary.getHeight() / (float) bgSize.getHeight();

        int xOffset = ((int) (ratioW * 39));
        int yOffset = ((int) (ratioH * 38));


        bgBounds = new Rectangle((width - boundary.getWidth()) / 2, (height - boundary.getHeight()) / 2, boundary.getWidth(), boundary.getHeight());
        screenBounds = new Rectangle(bgBounds.getX() + xOffset, bgBounds.getY() + yOffset, boundary.getWidth() - (xOffset * 2), boundary.getHeight() - (yOffset * 2));
        if (activeApp != null)
            activeApp.bounds = screenBounds;
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    private void drawTablet() {
        mc.getTextureManager().bindTexture(bgTexture);
        GuiUtil.instance.drawImage(bgBounds.getX(), bgBounds.getY(), bgBounds.getWidth(), bgBounds.getHeight());
    }

    private void drawWallpaper() {

        if (Gallery.currentWallpaper != null) {
            Gallery.currentWallpaper.drawWallpaper(screenBounds.getX(), screenBounds.getY(), screenBounds.getWidth(), screenBounds.getHeight());
        } else {
            mc.getTextureManager().bindTexture(defaultWallpaper);
            GuiUtil.instance.drawImage(screenBounds.getX(), screenBounds.getY(), screenBounds.getWidth(), screenBounds.getHeight());
        }

        this.drawGradientRect(screenBounds.getX(), screenBounds.getY(), screenBounds.getX() + screenBounds.getWidth(), screenBounds.getY() + screenBounds.getHeight(), -1072689136, -804253680);
    }

    private void drawApps() {

        if(fontTexture == null) {
            try {
                Field locationFontTexture = FontRenderer.class.
                        getDeclaredField("locationFontTexture");

                locationFontTexture.setAccessible(true);

                fontTexture = (ResourceLocation) locationFontTexture.get(mc.fontRendererObj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(fontTexture == null) return;

        int columns = 4, rows = 2;
        int iconWidth = 64, iconHeight = 64;


        int column = 0, row = 0, xOffset = 0, yOffset = 0;
        App app = null;
        for (int i = 0; i < 8; ++i) {

            if (i < AppHandler.getApps().toArray().length) {

                app = (App) AppHandler.getApps().toArray()[i];

                xOffset = (screenBounds.getWidth() / columns) * column;
                yOffset = (screenBounds.getHeight() / rows) * row;

                xOffset += ((screenBounds.getWidth() / columns) - iconWidth) / 2;
                yOffset += ((screenBounds.getHeight() / rows) - iconHeight) / 2;

                // TODO: Work on clicking and hovering over icons

                //ascii.png for unicode font
                mc.getTextureManager().bindTexture(new ResourceLocation("minecraft:textures/font/ascii_sga.png"));
                int fontOffset = (iconWidth - mc.fontRendererObj.getStringWidth(app.name)) / 2;
                mc.fontRendererObj.drawString(app.name, screenBounds.getX() + xOffset + fontOffset, screenBounds.getY() + yOffset + iconHeight + 2, 0xFFFFFF, true);

                AppHandler.getAppIcon(app.getClass()).drawWallpaper(screenBounds.getX() + xOffset, screenBounds.getY() + yOffset, iconWidth, iconHeight);

                if ((i + 1) % columns == 0) {
                    column = 0;
                    row++;
                } else {
                    column++;
                }
            }
        }
    }

}
