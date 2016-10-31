package com.ipixelmon.tablet.client.apps.camera;

import com.ipixelmon.tablet.client.App;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

/**
 * Created by colby on 10/29/2016.
 */
public class Gallery extends App {

    public static Wallpaper currentWallpaper;

    public Gallery(String name, String icon) {
        super(name, icon);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
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
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
    }

    @Override
    public void initGui() {
        super.initGui();
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    //    public static void refreshWallpapers()
//    {
//        MLRPTabletClientProxy.wallpaperArray.clear();
//        File folder = new File(MLRPTablet.directory.getAbsolutePath());
//        File[] listOfFiles = folder.listFiles();
//
//        for (int i = 0; i < listOfFiles.length; i++)
//        {
//            if (listOfFiles[i].isFile())
//            {
//                if (listOfFiles[i].getName().endsWith(".png") || listOfFiles[i].getName().endsWith(".jpg"))
//                {
//                    MLRPTabletClientProxy.wallpaperArray.add(new Wallpaper(listOfFiles[i]));
//                }
//            } else if (listOfFiles[i].isDirectory())
//            {
//                System.out.println("Directory " + listOfFiles[i].getName());
//            }
//        }
//    }
}
