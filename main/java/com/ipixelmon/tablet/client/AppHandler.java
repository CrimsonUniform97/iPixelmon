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
    private static final Map<File, Wallpaper> cachedIcons = Maps.newHashMap();

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

    public static Wallpaper getAppIcon(Class<? extends App> appClass) {
        for (App app : apps) {
            if (app.getClass().equals(appClass)) {
                try {
                    File file = new File(app.icon);

                    if(cachedIcons.containsKey(file)) return cachedIcons.get(file);

                    OutputStream outputStream = new FileOutputStream(file);
                    InputStream inputStream = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(iPixelmon.id, "textures/apps/" + app.icon + ".png")).getInputStream();
                    IOUtils.copy(inputStream, outputStream);
                    outputStream.close();
                    cachedIcons.put(file, new Wallpaper(file));
                    return cachedIcons.get(file);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }


}
