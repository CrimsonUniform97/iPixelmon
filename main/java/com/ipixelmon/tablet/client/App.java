package com.ipixelmon.tablet.client;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.client.apps.camera.Wallpaper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.compress.utils.IOUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.Rectangle;

import java.io.*;

/**
 * Created by colby on 10/28/2016.
 */
public abstract class App extends GuiScreen implements Comparable<App> {

    public final String name;
    public Rectangle screenBounds = new Rectangle();

    public App(String name) {
        this.name = name;
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

    public Wallpaper getIcon() {
        try {
            File file = new File(name.toLowerCase());

            if(AppHandler.cachedIcons.containsKey(file)) return AppHandler.cachedIcons.get(file);

            OutputStream outputStream = new FileOutputStream(file);
            InputStream inputStream = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation(iPixelmon.id, "textures/apps/" + name.toLowerCase() + "/icon.png")).getInputStream();
            IOUtils.copy(inputStream, outputStream);
            outputStream.close();
            AppHandler.cachedIcons.put(file, new Wallpaper(file));
            return AppHandler.cachedIcons.get(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
