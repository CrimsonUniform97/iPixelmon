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

        public static void renderItem(ItemStack stack, int x, int y, int screenWidth, int screenHeight, int mouseX, int mouseY) {
            Minecraft mc = Minecraft.getMinecraft();
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.enableBlend();
            mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
            mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, stack, x, y,
                    stack.stackSize > 1 ? String.valueOf(stack.stackSize) : "");

            if (mouseX >= x && mouseX <= x + 16 && mouseY >= y && mouseY <= y + 16) {
                renderToolTip(stack, mouseX, mouseY, screenWidth, screenHeight);
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
            GuiUtil.drawHoveringText(list, x, y, width, height);
        }
    }

}
