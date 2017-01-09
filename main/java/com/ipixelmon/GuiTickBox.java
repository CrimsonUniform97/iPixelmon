package com.ipixelmon;

import com.ipixelmon.util.GuiUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

/**
 * Created by colby on 1/8/2017.
 */
public class GuiTickBox extends Gui {

    private static final ResourceLocation switchOn = new ResourceLocation(iPixelmon.id, "textures/gui/switchOn.png");
    private static final ResourceLocation switchOff = new ResourceLocation(iPixelmon.id, "textures/gui/switchOff.png");

    private static final int WIDTH = 36, HEIGHT = 18;

    private int xPosition, yPosition;
    private String key;
    private boolean value;

    public boolean enabled = true;

    public GuiTickBox(int xPosition, int yPosition, String key, boolean value) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.key = key;
        this.value = value;
    }

    public void draw(Minecraft mc, int mouseX, int mouseY) {
        GlStateManager.enableTexture2D();
        GlStateManager.color(1f, 1f, 1f, !enabled ? 155f/255f : 1f);
        GlStateManager.enableBlend();

        mc.getTextureManager().bindTexture(value ? switchOn : switchOff);
        GuiUtil.drawImage(xPosition, yPosition, WIDTH, HEIGHT);

        int stringWidth = mc.fontRendererObj.getStringWidth(key + ":");
        mc.fontRendererObj.drawStringWithShadow(key + ":", xPosition - stringWidth - 2,
                yPosition + ((HEIGHT - mc.fontRendererObj.FONT_HEIGHT) / 2), 0xFFFFFF);
    }

    public boolean mouseClicked(int mouseX, int mouseY) {
        if (enabled && mouseX >= xPosition && mouseX <= xPosition + WIDTH && mouseY >= yPosition && mouseY <= yPosition + HEIGHT) {
            value = !value;
            return true;
        }

        return false;
    }

    public boolean getValue() {
        return value;
    }

    public String getKey() {
        return key;
    }
}
