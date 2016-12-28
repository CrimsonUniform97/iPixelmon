package com.ipixelmon.tablet.apps.mail;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.util.GuiUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by colby on 12/16/2016.
 */
public class IconBtn extends GuiButton {

    private ResourceLocation icon;
    private RenderItem itemRender = Minecraft.getMinecraft().getRenderItem();
    private List<String> text;
    public IconBtn(int buttonId, int x, int y, String buttonText, ResourceLocation icon) {
        super(buttonId, x, y, buttonText);
        this.icon = icon;
        text = new ArrayList<>();
        text.add(buttonText);
        this.width = 20;
        this.height = 20;
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY) {
        GlStateManager.pushAttrib();
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
        GlStateManager.color(1, 1, 1, 1);

        mc.getTextureManager().bindTexture(icon);
        if(enabled) {
            GlStateManager.color(1, 1, 1, 1);
        } else {
            GlStateManager.color(128f/255f,128f/255f,128f/255f,255f);
        }

        iPixelmon.util.gui.drawImage(xPosition, yPosition, 16, 16);
        this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;

        if (this.hovered) {
            drawHoveringText(text, mouseX + 5, mouseY, mc.fontRendererObj);
        }
        GlStateManager.popAttrib();
    }

    private void drawHoveringText(List<String> textLines, int x, int y, FontRenderer font) {
        if (!textLines.isEmpty()) {
            GlStateManager.disableRescaleNormal();
            RenderHelper.disableStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.disableDepth();
            int i = 0;

            for (String s : textLines) {
                int j = font.getStringWidth(s);

                if (j > i) {
                    i = j;
                }
            }

            int l1 = x + 12;
            int i2 = y - 12;
            int k = 8;

            if (textLines.size() > 1) {
                k += 2 + (textLines.size() - 1) * 10;
            }

            if (l1 + i > this.width) {
                l1 -= 28 + i;
            }

            this.zLevel = 300.0F;
            this.itemRender.zLevel = 300.0F;
            int l = -267386864;
            this.drawGradientRect(l1 - 3, i2 - 4, l1 + i + 3, i2 - 3, l, l);
            this.drawGradientRect(l1 - 3, i2 + k + 3, l1 + i + 3, i2 + k + 4, l, l);
            this.drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 + k + 3, l, l);
            this.drawGradientRect(l1 - 4, i2 - 3, l1 - 3, i2 + k + 3, l, l);
            this.drawGradientRect(l1 + i + 3, i2 - 3, l1 + i + 4, i2 + k + 3, l, l);
            int i1 = 1347420415;
            int j1 = (i1 & 16711422) >> 1 | i1 & -16777216;
            this.drawGradientRect(l1 - 3, i2 - 3 + 1, l1 - 3 + 1, i2 + k + 3 - 1, i1, j1);
            this.drawGradientRect(l1 + i + 2, i2 - 3 + 1, l1 + i + 3, i2 + k + 3 - 1, i1, j1);
            this.drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 - 3 + 1, i1, i1);
            this.drawGradientRect(l1 - 3, i2 + k + 2, l1 + i + 3, i2 + k + 3, j1, j1);

            for (int k1 = 0; k1 < textLines.size(); ++k1) {
                String s1 = (String) textLines.get(k1);
                font.drawStringWithShadow(s1, (float) l1, (float) i2, -1);

                if (k1 == 0) {
                    i2 += 2;
                }

                i2 += 10;
            }

            this.zLevel = 0.0F;
            this.itemRender.zLevel = 0.0F;
            GlStateManager.enableLighting();
            GlStateManager.enableDepth();
            RenderHelper.enableStandardItemLighting();
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableBlend();
        }
    }

}
