package com.ipixelmon.tablet.client;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.util.Rectangle;

/**
 * Created by colby on 10/28/2016.
 */
public abstract class App extends GuiScreen {

    public final String name;
    public final ResourceLocation icon;
    public Rectangle bounds = new Rectangle();

    public App(String name, ResourceLocation icon) {
        this.name = name;
        this.icon = icon;
    }

}
