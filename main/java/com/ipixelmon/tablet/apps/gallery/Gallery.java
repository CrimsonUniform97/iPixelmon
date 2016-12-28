package com.ipixelmon.tablet.apps.gallery;

import com.google.common.collect.Lists;
import com.ipixelmon.util.GuiUtil;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.apps.App;
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

    public static final Set<Wallpaper> wallpapers = new TreeSet<>();
    public static final File wallpapersDir = new File(iPixelmon.path.getParentFile(), "screenshots");
    private static final ResourceLocation folderIcon = new ResourceLocation(iPixelmon.id, "textures/apps/gallery/folder.png");
    private static final ResourceLocation reloadIcon = new ResourceLocation(iPixelmon.id, "textures/apps/gallery/reload.png");
    private static final ResourceLocation arrowIcon = new ResourceLocation(iPixelmon.id, "textures/apps/gallery/arrow.png");
    private static final ResourceLocation trashIcon = new ResourceLocation(iPixelmon.id, "textures/apps/gallery/trash.png");
    private static final ResourceLocation setWallpaperIcon = new ResourceLocation(iPixelmon.id, "textures/apps/gallery/set_as_wallpaper_icon.png");
    private Rectangle folderBounds, reloadBounds, leftBounds, rightBounds, trashBounds, setWallpaperBounds, goBackBounds;
    private int page = 0, columns = 2, rows = 2;
    private Wallpaper viewingWallpaper;

    public Gallery(String name) {
        super(name, true);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        GlStateManager.enableBlend();

        if (viewingWallpaper != null) {
            drawViewingWallpaperScreen(mouseX, mouseY);
            return;
        }

        drawWallpapers(mouseX, mouseY);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        TileIterator tileIterator = new TileIterator(getScreenBounds(), columns, rows, wallpapers.toArray());

        if(viewingWallpaper != null) {

            if(setWallpaperBounds.contains(mouseX, mouseY)) {
                iPixelmon.config.setString("tablet.wallpaper", viewingWallpaper.getLocation().getAbsolutePath());
                viewingWallpaper = null;
            } else if (trashBounds.contains(mouseX, mouseY)) {
                viewingWallpaper.getLocation().delete();
                wallpapers.remove(viewingWallpaper);
                viewingWallpaper = null;
            } else if (goBackBounds.contains(mouseX, mouseY)) {
                viewingWallpaper = null;
            }

            tileIterator = new TileIterator(getScreenBounds(), columns, rows, wallpapers.toArray());

            if(page > tileIterator.getMaxPages()) page = tileIterator.getMaxPages();

            return;
        }

        Wallpaper wallpaper;
        Rectangle rec;
        while (tileIterator.hasNext()) {
            wallpaper = (Wallpaper) tileIterator.next();
            rec = tileIterator.getTileBounds();
            if (tileIterator.getPage() == page) {
                Dimension boundary = iPixelmon.util.gui.getScaledDimension(new Dimension(wallpaper.getImage().getWidth(), wallpaper.getImage().getHeight()), new Dimension(rec.getWidth() - 24, rec.getHeight()));

                rec.setBounds(rec.getX() + 2, rec.getY() + ((rec.getHeight() - boundary.getHeight()) / 2), boundary.getWidth(), boundary.getHeight());

                if (rec.contains(mouseX, mouseY)) {
                    viewingWallpaper = wallpaper;
                    return;
                }
            }
        }

        if (folderBounds.contains(mouseX, mouseY)) openWallpapersFolder();
        else if (reloadBounds.contains(mouseX, mouseY)) populateWallpapers();
        else if (rightBounds.contains(mouseX, mouseY)) {
            page = page + 1 > tileIterator.getMaxPages() ? tileIterator.getMaxPages() : page + 1;
        } else if (leftBounds.contains(mouseX, mouseY)) page = page - 1 < 0 ? 0 : page - 1;
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
    }

    @Override
    public void initGui() {
        super.initGui();
        folderBounds = new Rectangle(getScreenBounds().getX() + (getScreenBounds().getWidth() - 18), getScreenBounds().getY() + (18 * 0), 16, 16);
        reloadBounds = new Rectangle(getScreenBounds().getX() + (getScreenBounds().getWidth() - 18), getScreenBounds().getY() + (18 * 1), 16, 16);
        rightBounds = new Rectangle(getScreenBounds().getX() + (getScreenBounds().getWidth() - 18), getScreenBounds().getY() + (18 * 2), 16, 16);
        leftBounds = new Rectangle(getScreenBounds().getX() + (getScreenBounds().getWidth() - 18) - 1, getScreenBounds().getY() + (18 * 3), 16, 16);
        setWallpaperBounds = new Rectangle(getScreenBounds().getX() + (getScreenBounds().getWidth() - 18), getScreenBounds().getY() + (18 * 0), 16, 16);
        trashBounds = new Rectangle(getScreenBounds().getX() + (getScreenBounds().getWidth() - 17), getScreenBounds().getY() + (18 * 1), 16, 16);
        goBackBounds = new Rectangle(getScreenBounds().getX() + (getScreenBounds().getWidth() - 18), getScreenBounds().getY() + (18 * 2), 16, 16);
        viewingWallpaper = null;
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    private void drawViewingWallpaperScreen(int mouseX, int mouseY) {
        Dimension boundary = iPixelmon.util.gui.getScaledDimension(new Dimension(viewingWallpaper.getImage().getWidth(),
                viewingWallpaper.getImage().getHeight()), new Dimension(getScreenBounds().getWidth() - 20, getScreenBounds().getHeight() - 20));
        viewingWallpaper.drawWallpaper(getScreenBounds().getX() + 1,
                getScreenBounds().getY() + ((getScreenBounds().getHeight() - boundary.getHeight()) / 2), boundary.getWidth(), boundary.getHeight());

        mc.getTextureManager().bindTexture(trashIcon);
        iPixelmon.util.gui.drawImage(trashBounds.getX(), trashBounds.getY(), trashBounds.getWidth(), trashBounds.getHeight());
        mc.getTextureManager().bindTexture(setWallpaperIcon);
        iPixelmon.util.gui.drawImage(setWallpaperBounds.getX(), setWallpaperBounds.getY(), setWallpaperBounds.getWidth(), setWallpaperBounds.getHeight());
        mc.getTextureManager().bindTexture(arrowIcon);

        GlStateManager.pushMatrix();
        {
            GlStateManager.translate(goBackBounds.getX(), goBackBounds.getY(), 0f);
            GlStateManager.translate(goBackBounds.getWidth() / 2, goBackBounds.getHeight() / 2, 0f);
            GlStateManager.rotate(180f, 0f, 0f, 0f);
            GlStateManager.scale(1.4f, 1.4f, 1.4f);
            GlStateManager.translate(-goBackBounds.getWidth() / 2, -goBackBounds.getHeight() / 2, 0f);
            iPixelmon.util.gui.drawImage(0, 0, goBackBounds.getWidth(), goBackBounds.getHeight());
        }
        GlStateManager.popMatrix();

        List<String> hoveringText = Lists.newArrayList();
        if(setWallpaperBounds.contains(mouseX, mouseY)) {
            hoveringText.add("Set as Wallpaper");
        } else if (trashBounds.contains(mouseX, mouseY)) {
            hoveringText.add("Delete");
        } else if (goBackBounds.contains(mouseX, mouseY)) {
            hoveringText.add("Go Back");
        }

        drawHoveringText(hoveringText, mouseX, mouseY, mc.fontRendererObj);
    }

    private void drawWallpapers(int mouseX, int mouseY) {
        TileIterator tileIterator = new TileIterator(getScreenBounds(), columns, rows, wallpapers.toArray());
        Wallpaper wallpaper;
        Rectangle rec;
        while (tileIterator.hasNext()) {
            wallpaper = (Wallpaper) tileIterator.next();
            rec = tileIterator.getTileBounds();
            if (tileIterator.getPage() == page) {
                Dimension boundary = iPixelmon.util.gui.getScaledDimension(new Dimension(wallpaper.getImage().getWidth(), wallpaper.getImage().getHeight()), new Dimension(rec.getWidth() - 24, rec.getHeight()));

                rec.setBounds(rec.getX() + 2, rec.getY() + ((rec.getHeight() - boundary.getHeight()) / 2), boundary.getWidth(), boundary.getHeight());

                if (rec.contains(mouseX, mouseY)) {
                    GlStateManager.color(120f / 255f, 120f / 255f, 120f / 255f, 1f);
                }

                wallpaper.drawWallpaper(rec.getX(), rec.getY(), rec.getWidth(), rec.getHeight());
                GlStateManager.color(1, 1, 1, 1);
            }
        }

        mc.getTextureManager().bindTexture(folderIcon);
        iPixelmon.util.gui.drawImage(folderBounds.getX(), folderBounds.getY(), folderBounds.getWidth(), folderBounds.getHeight());
        mc.getTextureManager().bindTexture(reloadIcon);
        iPixelmon.util.gui.drawImage(reloadBounds.getX(), reloadBounds.getY(), reloadBounds.getWidth(), reloadBounds.getHeight());
        drawArrows();
        drawHoveringText(mouseX, mouseY);
    }

    private void drawArrows() {
        mc.getTextureManager().bindTexture(arrowIcon);

        GlStateManager.pushMatrix();
        {
            GlStateManager.translate(rightBounds.getX(), rightBounds.getY(), 0f);
            GlStateManager.translate(rightBounds.getWidth() / 2, rightBounds.getHeight() / 2, 0f);
            GlStateManager.scale(1.4f, 1.4f, 1.4f);
            GlStateManager.translate(-rightBounds.getWidth() / 2, -rightBounds.getHeight() / 2, 0f);
            iPixelmon.util.gui.drawImage(0, 0, rightBounds.getWidth(), rightBounds.getHeight());
        }
        GlStateManager.popMatrix();

        GlStateManager.pushMatrix();
        {
            GlStateManager.translate(leftBounds.getX(), leftBounds.getY(), 0f);
            GlStateManager.translate(leftBounds.getWidth() / 2, leftBounds.getHeight() / 2, 0f);
            GlStateManager.rotate(180f, 0f, 0f, 0f);
            GlStateManager.scale(1.4f, 1.4f, 1.4f);
            GlStateManager.translate(-leftBounds.getWidth() / 2, -leftBounds.getHeight() / 2, 0f);
            iPixelmon.util.gui.drawImage(0, 0, leftBounds.getWidth(), leftBounds.getHeight());
        }
        GlStateManager.popMatrix();
    }

    private void drawHoveringText(int mouseX, int mouseY) {
        if (folderBounds.contains(mouseX, mouseY)) {
            List<String> text = Lists.newArrayList();
            text.add("Open Folder");
            drawHoveringText(text, mouseX, mouseY, mc.fontRendererObj);
        }

        if (reloadBounds.contains(mouseX, mouseY)) {
            List<String> text = Lists.newArrayList();
            text.add("Refresh Wallpapers");
            drawHoveringText(text, mouseX, mouseY, mc.fontRendererObj);
        }

        if (rightBounds.contains(mouseX, mouseY)) {
            List<String> text = Lists.newArrayList();
            text.add("Page Up");
            drawHoveringText(text, mouseX, mouseY, mc.fontRendererObj);
        }

        if (leftBounds.contains(mouseX, mouseY)) {
            List<String> text = Lists.newArrayList();
            text.add("Page Down");
            drawHoveringText(text, mouseX, mouseY, mc.fontRendererObj);
        }
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

        Iterator iterator = wallpapers.iterator();
        Wallpaper wallpaper;
        while (iterator.hasNext()) {
            wallpaper = (Wallpaper) iterator.next();

            if (!wallpaper.isValid()) iterator.remove();
        }

        iterator = Arrays.asList(wallpapersDir.listFiles()).listIterator();

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

    public static final Wallpaper getWallpaper() {
        Iterator iterator = wallpapers.iterator();
        Wallpaper wallpaper;
        while(iterator.hasNext()) {
            wallpaper = (Wallpaper) iterator.next();
            if(wallpaper.getLocation().getAbsolutePath().equalsIgnoreCase(iPixelmon.config.getString("tablet.wallpaper"))) return wallpaper;
        }

        return null;
    }
}
