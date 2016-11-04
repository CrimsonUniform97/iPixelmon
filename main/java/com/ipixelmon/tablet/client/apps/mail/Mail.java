package com.ipixelmon.tablet.client.apps.mail;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.client.App;
import com.ipixelmon.tablet.client.apps.camera.Wallpaper;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.compress.utils.IOUtils;

import java.io.*;

/**
 * Created by colby on 10/28/2016.
 */
public class Mail extends App {

    private static Wallpaper icon, icon_new;

    public Mail(String name) {
        super(name);
        icon = getIcon(false);
        icon_new = getIcon(true);
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
    public Wallpaper getIcon() {
        return icon;
    }

    private Wallpaper getIcon(boolean isNew) {
        File file = new File(name.toLowerCase());

        try {
            OutputStream outputStream = new FileOutputStream(file);
            InputStream inputStream = Minecraft.getMinecraft().getResourceManager().getResource(
                    new ResourceLocation(iPixelmon.id, "textures/apps/" + name.toLowerCase() + "/icon" + (isNew ? "_new" : "") + ".png")).getInputStream();
            IOUtils.copy(inputStream, outputStream);
            outputStream.close();
            return new Wallpaper(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
