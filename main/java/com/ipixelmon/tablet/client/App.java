package com.ipixelmon.tablet.client;

import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.util.Rectangle;

/**
 * Created by colby on 10/28/2016.
 */
public abstract class App extends GuiScreen implements Comparable<App> {

    public final String name;
    public final String icon;
    public Rectangle bounds = new Rectangle();

    public App(String name, String icon) {
        this.name = name;
        this.icon = icon;
    }

    @Override
    public int compareTo(App o) {
        return o.name.equalsIgnoreCase(name) ? 0 : 1;
    }
}
