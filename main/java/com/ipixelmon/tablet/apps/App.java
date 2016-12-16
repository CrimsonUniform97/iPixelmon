package com.ipixelmon.tablet.apps;

import com.google.common.collect.Maps;
import com.ipixelmon.GuiUtil;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.apps.gallery.Gallery;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.Dimension;
import org.lwjgl.util.Rectangle;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by colby on 10/28/2016.
 */
public abstract class App extends GuiScreen implements Comparable<App> {

    public final String name;
    private Rectangle screenBounds = new Rectangle();
    private Rectangle bgBounds = new Rectangle();

    private static final ResourceLocation bgTexture = new ResourceLocation(iPixelmon.id, "textures/gui/tablet/tablet.png");
    private static final ResourceLocation defaultWallpaper = new ResourceLocation(iPixelmon.id, "textures/gui/tablet/default_wallpaper.png");

    private static App activeApp = null;

    public static final Set<App> apps = new TreeSet<>();
    public static final Map<String, ResourceLocation> cachedIcons = Maps.newHashMap();

    public App(String name, boolean register) {
        this.name = name;
        if (register)
            apps.add(this);
    }

    public void setActiveApp(App app) {
        Minecraft.getMinecraft().displayGuiScreen(app);
    }

    public static App getActiveApp() {
        return activeApp;
    }

    @Override
    public int compareTo(App o) {
        return o.name.equalsIgnoreCase(name) ? 0 : 1;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.enableBlend();
        drawTablet();
        drawWallpaper();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public void initGui() {
        super.initGui();

        Dimension bgSize = new Dimension(614, 378);

        Dimension boundary = GuiUtil.instance.getScaledDimension(bgSize, new Dimension(width, height));

        float ratioW = (float) boundary.getWidth() / (float) bgSize.getWidth();
        float ratioH = (float) boundary.getHeight() / (float) bgSize.getHeight();

        int xOffset = ((int) (ratioW * 39));
        int yOffset = ((int) (ratioH * 38));

        bgBounds = new Rectangle((width - boundary.getWidth()) / 2, (height - boundary.getHeight()) / 2, boundary.getWidth(), boundary.getHeight());
        screenBounds = new Rectangle(bgBounds.getX() + xOffset, bgBounds.getY() + yOffset, boundary.getWidth() - (xOffset * 2), boundary.getHeight() - (yOffset * 2));
    }

    public Rectangle getBgBounds() {
        return bgBounds;
    }

    public Rectangle getScreenBounds() {
        return screenBounds;
    }

    public ResourceLocation getIcon() {
        if (cachedIcons.containsKey(name)) return cachedIcons.get(name);

        ResourceLocation res = new ResourceLocation(iPixelmon.id, "textures/apps/" + name + "/icon.png");
        cachedIcons.put(name, res);
        return res;
    }

    public static App getApp(Class<? extends App> appClass) {
        for (App app : apps) if (app.getClass().equals(appClass)) return app;

        return null;
    }

    private void drawWallpaper() {
        GlStateManager.enableTexture2D();
        GlStateManager.enableBlend();
        if (Gallery.getWallpaper() != null) {
            Gallery.getWallpaper().drawWallpaper(getScreenBounds().getX(), getScreenBounds().getY(), getScreenBounds().getWidth(), getScreenBounds().getHeight());
        } else {
            mc.getTextureManager().bindTexture(defaultWallpaper);
            GuiUtil.instance.drawImage(getScreenBounds().getX(), getScreenBounds().getY(), getScreenBounds().getWidth(), getScreenBounds().getHeight());
        }

        GlStateManager.disableTexture2D();
        GlStateManager.color(0/255f, 0/255f, 0/255f, 128f/255f);
        this.drawTexturedModalRect(getScreenBounds().getX(), getScreenBounds().getY(), 0, 0, getScreenBounds().getWidth(), getScreenBounds().getHeight());
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.enableTexture2D();
    }

    private void drawTablet() {
        mc.getTextureManager().bindTexture(bgTexture);
        GuiUtil.instance.drawImage(getBgBounds().getX(), getBgBounds().getY(), getBgBounds().getWidth(), getBgBounds().getHeight());
    }

}
