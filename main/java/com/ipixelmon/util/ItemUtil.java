package com.ipixelmon.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * Created by colby on 12/31/2016.
 */
public class ItemUtil {

    @SideOnly(Side.CLIENT)
    public static class Client {

        public static void renderItem(ItemStack stack, int x, int y, int width, int height, int mouseX, int mouseY) {
            Minecraft mc = Minecraft.getMinecraft();
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.enableBlend();
            mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
            mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, stack, x, y, String.valueOf(stack.stackSize));

            if (mouseX >= x && mouseX <= x + 16 && mouseY >= y && mouseY <= y + 16) {
                GL11.glDisable(GL11.GL_SCISSOR_TEST);
                renderToolTip(stack, mouseX, mouseY, width, height);
                GL11.glEnable(GL11.GL_SCISSOR_TEST);
            }

            RenderHelper.disableStandardItemLighting();
        }


        private static void renderToolTip(ItemStack stack, int x, int y, int width, int height) {
            Minecraft mc = Minecraft.getMinecraft();
            List<String> list = stack.getTooltip(mc.thePlayer, mc.gameSettings.advancedItemTooltips);

            for (int i = 0; i < list.size(); ++i) {
                if (i == 0) {
                    list.set(i, stack.getRarity().rarityColor + (String) list.get(i));
                } else {
                    list.set(i, EnumChatFormatting.GRAY + (String) list.get(i));
                }
            }

            FontRenderer font = stack.getItem().getFontRenderer(stack);
            drawHoveringText(list, x, y, width, height);
        }

        private static void drawHoveringText(List<String> textLines, int x, int y, int width, int height) {
            Minecraft mc = Minecraft.getMinecraft();
            RenderItem itemRender = mc.getRenderItem();

            if (!textLines.isEmpty()) {
                GlStateManager.disableRescaleNormal();
                RenderHelper.disableStandardItemLighting();
                GlStateManager.disableLighting();
                GlStateManager.disableDepth();
                int i = 0;

                for (String s : textLines) {
                    int j = mc.fontRendererObj.getStringWidth(s);

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

                if (l1 + i > width) {
                    l1 -= 28 + i;
                }

                if (i2 + k + 6 > height) {
                    i2 = height - k - 6;
                }

//                this.zLevel = 300.0F;
                itemRender.zLevel = 300.0F;
                int l = -267386864;
                drawGradientRect(l1 - 3, i2 - 4, l1 + i + 3, i2 - 3, l, l);
                drawGradientRect(l1 - 3, i2 + k + 3, l1 + i + 3, i2 + k + 4, l, l);
                drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 + k + 3, l, l);
                drawGradientRect(l1 - 4, i2 - 3, l1 - 3, i2 + k + 3, l, l);
                drawGradientRect(l1 + i + 3, i2 - 3, l1 + i + 4, i2 + k + 3, l, l);
                int i1 = 1347420415;
                int j1 = (i1 & 16711422) >> 1 | i1 & -16777216;
                drawGradientRect(l1 - 3, i2 - 3 + 1, l1 - 3 + 1, i2 + k + 3 - 1, i1, j1);
                drawGradientRect(l1 + i + 2, i2 - 3 + 1, l1 + i + 3, i2 + k + 3 - 1, i1, j1);
                drawGradientRect(l1 - 3, i2 - 3, l1 + i + 3, i2 - 3 + 1, i1, i1);
                drawGradientRect(l1 - 3, i2 + k + 2, l1 + i + 3, i2 + k + 3, j1, j1);

                for (int k1 = 0; k1 < textLines.size(); ++k1) {
                    String s1 = (String) textLines.get(k1);
                    mc.fontRendererObj.drawStringWithShadow(s1, (float) l1, (float) i2, -1);

                    if (k1 == 0) {
                        i2 += 2;
                    }

                    i2 += 10;
                }

//                this.zLevel = 0.0F;
                itemRender.zLevel = 0.0F;
                GlStateManager.enableLighting();
                GlStateManager.enableDepth();
                RenderHelper.enableStandardItemLighting();
                GlStateManager.enableRescaleNormal();
            }
        }

        private static void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor) {
            float f = (float) (startColor >> 24 & 255) / 255.0F;
            float f1 = (float) (startColor >> 16 & 255) / 255.0F;
            float f2 = (float) (startColor >> 8 & 255) / 255.0F;
            float f3 = (float) (startColor & 255) / 255.0F;
            float f4 = (float) (endColor >> 24 & 255) / 255.0F;
            float f5 = (float) (endColor >> 16 & 255) / 255.0F;
            float f6 = (float) (endColor >> 8 & 255) / 255.0F;
            float f7 = (float) (endColor & 255) / 255.0F;
            GlStateManager.disableTexture2D();
            GlStateManager.enableBlend();
            GlStateManager.disableAlpha();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            GlStateManager.shadeModel(7425);
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer worldrenderer = tessellator.getWorldRenderer();
            worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
            worldrenderer.pos((double) right, (double) top, (double) 300).color(f1, f2, f3, f).endVertex();
            worldrenderer.pos((double) left, (double) top, (double) 300).color(f1, f2, f3, f).endVertex();
            worldrenderer.pos((double) left, (double) bottom, (double) 300).color(f5, f6, f7, f4).endVertex();
            worldrenderer.pos((double) right, (double) bottom, (double) 300).color(f5, f6, f7, f4).endVertex();
            tessellator.draw();
            GlStateManager.shadeModel(7424);
            GlStateManager.disableBlend();
            GlStateManager.enableAlpha();
            GlStateManager.enableTexture2D();
        }
    }

}
