package com.ipixelmon.tablet.client;

import com.google.common.collect.Maps;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.client.apps.camera.Wallpaper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by colby on 10/30/2016.
 */
public class AppHandler {

    private static final Set<App> apps = new TreeSet<>();
    protected static final Map<File, Wallpaper> cachedIcons = Maps.newHashMap();

    private AppHandler() {
    }

    public static Set<App> getApps() {
        return apps;
    }

    public static void registerApp(App app) {
        apps.add(app);
    }

    public static App getApp(Class<? extends App> appClass) {
        for (App app : apps) if (app.getClass().equals(appClass)) return app;

        return null;
    }

}
