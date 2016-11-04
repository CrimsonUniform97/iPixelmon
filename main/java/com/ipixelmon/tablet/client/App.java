package com.ipixelmon.tablet.client;

import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.Rectangle;

import java.io.IOException;

/**
 * Created by colby on 10/28/2016.
 */
public abstract class App extends GuiScreen implements Comparable<App> {

    public final String name;
    public final String icon;
    public Rectangle screenBounds = new Rectangle();

    public App(String name, String icon) {
        this.name = name;
        this.icon = icon;
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
}
