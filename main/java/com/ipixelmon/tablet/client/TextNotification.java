package com.ipixelmon.tablet.client;

import com.ipixelmon.tablet.notification.Notification;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;

import java.util.Random;

/**
 * Created by colby on 10/6/2016.
 */
public class TextNotification extends Notification {

    private String text;
    private int duration;

    public TextNotification(String text) {
        super();
        this.text = text;
        this.duration = 1000 * (MathHelper.getRandomIntegerInRange(new Random(), 5, 30));
    }

    @Override
    public void draw() {
        GlStateManager.disableTexture2D();
        GlStateManager.color(0, 0, 0, 1);
        drawTexturedModalRect(0, 0, 0, 0, maxWidth, getHeight());
        GlStateManager.enableTexture2D();
        GlStateManager.color(1, 1, 1, 1);
        Minecraft.getMinecraft().fontRendererObj.drawString(text, 0, 0, 0xFFFFFF);
    }

    @Override
    public int getHeight() {
        return 30;
    }

    @Override
    public int getWidth() {
        return maxWidth;
    }

    @Override
    public long getDuration() {
        return duration;
    }

    @Override
    public void actionPerformed() {
        System.out.println(text);
    }
}
