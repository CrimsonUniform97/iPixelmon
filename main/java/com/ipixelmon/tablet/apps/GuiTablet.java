package com.ipixelmon.tablet.apps;

import com.google.common.collect.Maps;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.util.GuiUtil;
import com.ipixelmon.pixelegg.client.Animation;
import com.ipixelmon.tablet.client.TileIterator;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.Rectangle;

import java.io.IOException;
import java.util.Map;

/**
 * Created by colby on 10/28/2016.
 */
public class GuiTablet extends App {

    private Map<App, Animation> animations = Maps.newHashMap();

    private static final int columns = 4, rows = 2, iconWidth = 64, iconHeight = 64;

    public GuiTablet() {
        super("Tablet", false);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        drawApps(mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        setActiveApp(getAppUnderMouse(mouseX, mouseY));
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if(keyCode == Keyboard.KEY_ESCAPE) setActiveApp(null);
    }

    private void drawApps(int mouseX, int mouseY) {
        GlStateManager.enableTexture2D();

        App app;
        Rectangle rec;

        TileIterator tileIterator = new TileIterator(getScreenBounds(), columns, rows, App.apps.toArray());

        while (tileIterator.hasNext()) {
            app = (App) tileIterator.next();
            rec = tileIterator.getTileBounds();

            mc.getTextureManager().bindTexture(new ResourceLocation("minecraft:textures/font/ascii.png"));

            int xOffset = (rec.getWidth() - iconWidth) / 2;
            int yOffset = (rec.getHeight() - iconHeight) / 2;


            // animation and drawing for icon
            GlStateManager.pushMatrix();
            GlStateManager.translate(rec.getX() + xOffset, rec.getY() + yOffset, 1);

            // translate to center
            GlStateManager.translate((float) iconWidth / 2, (float) iconHeight / 2, 1 / 2);

            if (!animations.containsKey(app)) {
                animations.put(app, new Animation(rec.getX() + xOffset, rec.getY() + yOffset, 1).scale(1f));
            }

            float scaleTo = 1.2f;

            Rectangle boundsToRec = rec;
            boundsToRec.setBounds(rec.getX() + xOffset, rec.getY() + yOffset, iconWidth, iconHeight);

            if (boundsToRec.contains(mouseX, mouseY)) {
                if (animations.get(app).getActions().isEmpty() && animations.get(app).scalar() == 1f) {
                    animations.put(app, new Animation(rec.getX() + xOffset, rec.getY() + yOffset, 1).scale(1f).scaleTo(scaleTo, 0.02f));
                }
            } else {
                if (animations.get(app).getActions().isEmpty() && animations.get(app).scalar() == scaleTo) {
                    animations.put(app, new Animation(rec.getX() + xOffset, rec.getY() + yOffset, 1).scale(scaleTo).scaleTo(1f, 0.02f));
                }
            }

            // increment animation
            animations.get(app).animate();

            // scale
            GlStateManager.scale(animations.get(app).scalar(), animations.get(app).scalar(), animations.get(app).scalar());

            // translate back from center
            GlStateManager.translate((float) -iconWidth / 2, (float) -iconHeight / 2, -1 / 2);

            // draw font
            mc.fontRendererObj.setUnicodeFlag(true);
            int fontOffset = (iconWidth - mc.fontRendererObj.getStringWidth(app.name)) / 2;
            mc.fontRendererObj.drawString(app.name, fontOffset, iconHeight + 1, 0xFFFFFF, true);
            mc.fontRendererObj.setUnicodeFlag(false);

            // draw icon
            mc.getTextureManager().bindTexture(app.getIcon());
            iPixelmon.util.gui.drawImage(0, 0, iconWidth, iconHeight);
            GlStateManager.popMatrix();
        }

    }

    private App getAppUnderMouse(int mouseX, int mouseY) {
        App app;
        Rectangle rec;

        TileIterator tileIterator = new TileIterator(getScreenBounds(), columns, rows, App.apps.toArray());

        while (tileIterator.hasNext()) {
            app = (App) tileIterator.next();
            rec = tileIterator.getTileBounds();
            Rectangle boundsToRec = rec;
            int xOffset = (rec.getWidth() - iconWidth) / 2;
            int yOffset = (rec.getHeight() - iconHeight) / 2;
            boundsToRec.setBounds(rec.getX() + xOffset, rec.getY() + yOffset, iconWidth, iconHeight);

            if (boundsToRec.contains(mouseX, mouseY)) return app;
        }

        return null;
    }

}
