package com.ipixelmon.tablet.client;

import com.google.common.collect.Maps;
import com.ipixelmon.iPixelmon;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.util.Rectangle;

import java.io.*;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by colby on 10/28/2016.
 */
public abstract class App extends GuiScreen implements Comparable<App> {

    public final String name;
    public Rectangle screenBounds = new Rectangle();

    public static App activeApp = null;

    public static final Set<App> apps = new TreeSet<>();
    public static final Map<String, ResourceLocation> cachedIcons = Maps.newHashMap();

    public App(String name, boolean register) {
        this.name = name;
        if (register)
            apps.add(this);
    }

    public void setActiveApp(App app) {
        App.activeApp = app;
        if (App.activeApp != null) {
            App.activeApp.screenBounds = screenBounds;
            App.activeApp.setWorldAndResolution(mc, width, height);
            App.activeApp.initGui();
        }
    }

    @Override
    public int compareTo(App o) {
        return o.name.equalsIgnoreCase(name) ? 0 : 1;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    public void initGui() {
        super.initGui();
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

}
