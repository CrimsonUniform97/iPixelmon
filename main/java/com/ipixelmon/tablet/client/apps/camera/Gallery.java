package com.ipixelmon.tablet.client.apps.camera;

import com.google.common.collect.Lists;
import com.ipixelmon.GuiUtil;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.client.App;
import com.ipixelmon.tablet.client.TileIterator;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.Sys;
import org.lwjgl.util.Dimension;
import org.lwjgl.util.Rectangle;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.*;

/**
 * Created by colby on 10/29/2016.
 */
public class Gallery extends App {

    public static Wallpaper currentWallpaper;
    public static final Set<Wallpaper> wallpapers = new TreeSet<>();
    public static final File wallpapersDir = new File(iPixelmon.path, "wallpapers");
    private static final ResourceLocation folderIcon = new ResourceLocation(iPixelmon.id, "textures/gui/tablet/folder.png");
    private static final ResourceLocation reloadIcon = new ResourceLocation(iPixelmon.id, "textures/gui/tablet/reload.png");
    private Rectangle folderBounds, reloadBounds;

    // TODO: Add pages

    public Gallery(String name, String icon) {
        super(name, icon);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        GlStateManager.enableBlend();

        TileIterator tileIterator = new TileIterator(screenBounds, 2, 2, wallpapers.toArray());
        Wallpaper wallpaper;
        Rectangle rec;
        while(tileIterator.hasNext()) {
            wallpaper = (Wallpaper) tileIterator.next();
            rec = tileIterator.getTileBounds();
            Dimension boundary = GuiUtil.instance.getScaledDimension(new Dimension(wallpaper.getImage().getWidth(), wallpaper.getImage().getHeight()), new Dimension(rec.getWidth() - 12, rec.getHeight() - 12));
            wallpaper.drawWallpaper(rec.getX() + (((rec.getWidth() - 18) - boundary.getWidth()) / 2), rec.getY() + ((rec.getHeight() - boundary.getHeight()) / 2), boundary.getWidth(), boundary.getHeight());
        }

        mc.getTextureManager().bindTexture(folderIcon);
        GuiUtil.instance.drawImage(folderBounds.getX(), folderBounds.getY(), folderBounds.getWidth(), folderBounds.getHeight());
        mc.getTextureManager().bindTexture(reloadIcon);
        GuiUtil.instance.drawImage(reloadBounds.getX(), reloadBounds.getY(), reloadBounds.getWidth(), reloadBounds.getHeight());

        if (folderBounds.contains(mouseX, mouseY)) {
            List<String> text = Lists.newArrayList();
            text.add("Open Folder");
            drawHoveringText(text, mouseX, mouseY, mc.fontRendererObj);
        }

        if (reloadBounds.contains(mouseX, mouseY)) {
            List<String> text = Lists.newArrayList();
            text.add("Reload Wallpapers");
            drawHoveringText(text, mouseX, mouseY, mc.fontRendererObj);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (folderBounds.contains(mouseX, mouseY)) openWallpapersFolder();
        else if (reloadBounds.contains(mouseX, mouseY)) populateWallpapers();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
    }

    @Override
    public void initGui() {
        super.initGui();
        folderBounds = new Rectangle(screenBounds.getX() + (screenBounds.getWidth() - 18), screenBounds.getY(), 16, 16);
        reloadBounds = new Rectangle(screenBounds.getX() + (screenBounds.getWidth() - 18), screenBounds.getY() + 18, 16, 16);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    private void openWallpapersFolder() {
        Logger logger = LogManager.getLogger();
        String s = wallpapersDir.getAbsolutePath();

        if (Util.getOSType() == Util.EnumOS.OSX) {
            try {
                logger.info(s);
                Runtime.getRuntime().exec(new String[]{"/usr/bin/open", s});
                return;
            } catch (IOException ioexception1) {
                logger.error((String) "Couldn\'t open file", (Throwable) ioexception1);
            }
        } else if (Util.getOSType() == Util.EnumOS.WINDOWS) {
            String s1 = String.format("cmd.exe /C start \"Open file\" \"%s\"", new Object[]{s});

            try {
                Runtime.getRuntime().exec(s1);
                return;
            } catch (IOException ioexception) {
                logger.error((String) "Couldn\'t open file", (Throwable) ioexception);
            }
        }

        boolean flag = false;

        try {
            Class<?> oclass = Class.forName("java.awt.Desktop");
            Object object = oclass.getMethod("getDesktop", new Class[0]).invoke((Object) null, new Object[0]);
            oclass.getMethod("browse", new Class[]{URI.class}).invoke(object, new Object[]{wallpapersDir.toURI()});
        } catch (Throwable throwable) {
            logger.error("Couldn\'t open link", throwable);
            flag = true;
        }

        if (flag) {
            logger.info("Opening via system class!");
            Sys.openURL("file://" + s);
        }
    }

    public static final void populateWallpapers() {
        wallpapersDir.mkdir();

        Iterator iterator = Arrays.asList(wallpapersDir.listFiles()).listIterator();

        File file;
        while (iterator.hasNext()) {
            file = (File) iterator.next();

            if (file.isFile()) {
                if (file.getName().endsWith(".png") || file.getName().endsWith(".jpg")) {
                    wallpapers.add(new Wallpaper(file));
                }
            }
        }
    }
}
