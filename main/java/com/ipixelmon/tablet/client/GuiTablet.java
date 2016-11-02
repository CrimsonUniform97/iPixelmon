package com.ipixelmon.tablet.client;

import com.google.common.collect.Maps;
import com.ipixelmon.GuiUtil;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.pixelegg.client.Animation;
import com.ipixelmon.tablet.client.apps.camera.Gallery;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.util.Dimension;
import org.lwjgl.util.Rectangle;
import org.w3c.dom.css.Rect;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by colby on 10/28/2016.
 */
public class GuiTablet extends GuiScreen {

    public static App activeApp = null;

    private static final ResourceLocation bgTexture = new ResourceLocation(iPixelmon.id, "textures/gui/tablet/tablet.png");
    private static final ResourceLocation defaultWallpaper = new ResourceLocation(iPixelmon.id, "textures/gui/tablet/default_wallpaper.png");
    private Dimension bgSize = new Dimension(614, 378);
    private Rectangle bgBounds, screenBounds = new Rectangle();

    private Map<App, Animation> animations = Maps.newHashMap();

    private static final int columns = 4, rows = 2, iconWidth = 64, iconHeight = 64;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        drawTablet();
        drawWallpaper();
        drawApps(mouseX, mouseY);
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
        if (activeApp != null) {
            activeApp.bounds = screenBounds;
            return;
        }

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

    private void drawApps(int mouseX, int mouseY) {
        App app;
        Rectangle rec;

        TileIterator tileIterator = new TileIterator(screenBounds, columns, rows, AppHandler.getApps().toArray());

        while(tileIterator.hasNext()) {
            app = (App) tileIterator.next();
            rec = tileIterator.getTileBounds();

            mc.getTextureManager().bindTexture(new ResourceLocation("minecraft:textures/font/ascii.png"));

            int xOffset = (rec.getWidth() - iconWidth) / 2;
            int yOffset = (rec.getHeight() - iconHeight) / 2;

            mc.fontRendererObj.setUnicodeFlag(true);
            int fontOffset = (iconWidth - mc.fontRendererObj.getStringWidth(app.name)) / 2;
            mc.fontRendererObj.drawString(app.name, rec.getX() + xOffset + fontOffset, rec.getY() + yOffset + iconHeight + 1, 0xFFFFFF, true);
            mc.fontRendererObj.setUnicodeFlag(false);

            // TODO: Add animation for font

            // animation and drawing for icon
            GlStateManager.pushMatrix();
            GlStateManager.translate(rec.getX() + xOffset, rec.getY() + yOffset, 1);
            GlStateManager.translate((float)iconWidth / 2,(float) iconHeight / 2, 1 / 2);

            if(!animations.containsKey(app)) {
                animations.put(app, new Animation(rec.getX() + xOffset, rec.getY() + yOffset, 1).scale(1f));
            }

            float scaleTo = 1.2f;

            if(rec.contains(mouseX, mouseY))
            {
                if(animations.get(app).getActions().isEmpty() && animations.get(app).scalar() == 1f) {
                    animations.put(app, new Animation(rec.getX() + xOffset, rec.getY() + yOffset, 1).scale(1f).scaleTo(scaleTo, 0.02f));
                }
            } else {
                if(animations.get(app).getActions().isEmpty() && animations.get(app).scalar() == scaleTo) {
                    animations.put(app, new Animation(rec.getX() + xOffset, rec.getY() + yOffset, 1).scale(scaleTo).scaleTo(1f, 0.02f));
                }
            }

            animations.get(app).animate();
            GlStateManager.scale(animations.get(app).scalar(), animations.get(app).scalar(), animations.get(app).scalar());

            GlStateManager.translate((float)-iconWidth / 2, (float)-iconHeight / 2, -1 / 2);
            AppHandler.getAppIcon(app.getClass()).drawWallpaper(0, 0, iconWidth, iconHeight);
            GlStateManager.popMatrix();
        }


    }

    // TODO: Make an abstract iterator that can take a type when it returns but comes with all the nice methods you included.

}
