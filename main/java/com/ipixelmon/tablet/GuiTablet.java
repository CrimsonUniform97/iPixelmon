package com.ipixelmon.tablet;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ipixelmon.pixelegg.client.Animation;
import com.ipixelmon.util.GuiUtil;
import javafx.scene.control.Tab;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.util.Rectangle;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by colby on 12/31/2016.
 */
public class GuiTablet extends AppGui {

    private Map<AppBase, Animation> animations = Maps.newHashMap();

    private static final int columns = 4, rows = 2, iconWidth = 64, iconHeight = 64;

    public GuiTablet(Object[] objects) {
        super(objects);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, int dWheel) {
        drawApps(mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (getAppUnderMouse(mouseX, mouseY) != null) {
            Tablet.displayApp(getAppUnderMouse(mouseX, mouseY).getClass(), null);
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == Keyboard.KEY_ESCAPE) Minecraft.getMinecraft().displayGuiScreen(null);
    }

    private void drawApps(int mouseX, int mouseY) {
        GlStateManager.enableTexture2D();

        AppBase app;
        Rectangle rec;

        List<AppBase> apps = Lists.newArrayList();

        for(AppBase appBase : Tablet.apps) {
            try {
                if(appBase.clientProxyClass().newInstance().getIcon() != null &&
                        appBase.clientProxyClass().newInstance().getIcon() instanceof ResourceLocation) {
                    apps.add(appBase);
                }
            } catch (Exception e) {}
        }

        TileIterator tileIterator = new TileIterator(screenBounds, columns, rows, apps.toArray());

        while (tileIterator.hasNext()) {
            app = (AppBase) tileIterator.next();
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
            int fontOffset = (iconWidth - mc.fontRendererObj.getStringWidth(app.getName())) / 2;
            mc.fontRendererObj.drawString(app.getName(), fontOffset, iconHeight + 1, 0xFFFFFF, true);
            mc.fontRendererObj.setUnicodeFlag(false);

            // draw icon
            try {
                mc.getTextureManager().bindTexture((ResourceLocation) app.clientProxyClass().newInstance().getIcon());
            } catch (Exception e) {}
            GuiUtil.drawImage(0, 0, iconWidth, iconHeight);
            GlStateManager.popMatrix();
        }

    }

    private AppBase getAppUnderMouse(int mouseX, int mouseY) {
        AppBase app;
        Rectangle rec;

        TileIterator tileIterator = new TileIterator(screenBounds, columns, rows, Tablet.apps.toArray());

        while (tileIterator.hasNext()) {
            app = (AppBase) tileIterator.next();
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
