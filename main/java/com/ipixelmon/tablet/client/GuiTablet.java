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
import java.util.Iterator;

/**
 * Created by colby on 10/28/2016.
 */
public class GuiTablet extends GuiScreen {

    public static App activeApp = null;

    private static final ResourceLocation bgTexture = new ResourceLocation(iPixelmon.id, "textures/gui/tablet/tablet.png");
    private static final ResourceLocation defaultWallpaper = new ResourceLocation(iPixelmon.id, "textures/gui/tablet/default_wallpaper.png");
    private Dimension bgSize = new Dimension(614, 378);
    private Rectangle bgBounds, screenBounds = new Rectangle();

    private static final int columns = 4, rows = 2, iconWidth = 64, iconHeight = 64;

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
        App app;

        AppIterator appIterator = new AppIterator(screenBounds, columns, rows, iconWidth, iconHeight, AppHandler.getApps().toArray().length);

        while (appIterator.hasNext()) {
            app = (App) AppHandler.getApps().toArray()[appIterator.next()];

            // TODO: Work on clicking and hovering over icons

            //ascii.png for unicode font
            mc.getTextureManager().bindTexture(new ResourceLocation("minecraft:textures/font/ascii_sga.png"));
            int fontOffset = (iconWidth - mc.fontRendererObj.getStringWidth(app.name)) / 2;
            mc.fontRendererObj.drawString(app.name, screenBounds.getX() + appIterator.getxOffset()
                    + fontOffset, screenBounds.getY() + appIterator.getyOffset() + iconHeight + 2, 0xFFFFFF, true);

            GlStateManager.enableTexture2D();
            AppHandler.getAppIcon(app.getClass()).drawWallpaper(screenBounds.getX() + appIterator.getxOffset(), screenBounds.getY() + appIterator.getyOffset(), iconWidth, iconHeight);

        }

    }

    private void drawHoverBox() {

    }

    // TODO: Make an abstract iterator that can take a type when it returns but comes with all the nice methods you included.
    private class AppIterator implements Iterator<Integer> {

        private int i = 0;
        private int column = 0, row = 0;
        private int xOffset = 0, yOffset = 0;
        private int columns, rows, iconWidth, iconHeight, maxIcons, maxApps;

        private Rectangle screenBounds;

        public AppIterator(Rectangle bounds, int columns, int rows, int iconWidth, int iconHeight, int maxApps) {
            this.screenBounds = bounds;
            this.columns = columns;
            this.rows = rows;
            this.iconWidth = iconWidth;
            this.iconHeight = iconHeight;
            this.maxIcons = columns * rows;
            this.maxApps = maxApps;
        }

        @Override
        public boolean hasNext() {
            return i < maxIcons && i < maxApps;
        }

        @Override
        public Integer next() {

            xOffset = (screenBounds.getWidth() / columns) * column;
            yOffset = (screenBounds.getHeight() / rows) * row;

            xOffset += ((screenBounds.getWidth() / columns) - iconWidth) / 2;
            yOffset += ((screenBounds.getHeight() / rows) - iconHeight) / 2;

            if ((i + 1) % columns == 0) {
                column = 0;
                row++;
            } else {
                column++;
            }

            return i++;
        }

        public int getColumn() {
            return column;
        }

        public int getRow() {
            return row;
        }

        public int getxOffset() {
            return xOffset;
        }

        public int getyOffset() {
            return yOffset;
        }

    }

}
