package com.ipixelmon.util;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * Created by colby on 12/31/2016.
 */
public class ItemUtil {

    public static String itemToString(ItemStack itemStack) {
        NBTTagCompound tagCompound = itemStack.writeToNBT(new NBTTagCompound());
        return tagCompound.toString();
    }

    public static ItemStack itemFromString(String s) {
        return ItemStack.loadItemStackFromNBT(NBTUtil.fromString(s));
    }

    @SideOnly(Side.CLIENT)
    public static class Client {

        public static int[] renderItem(ItemStack stack, int x, int y, int screenWidth, int screenHeight, int mouseX, int mouseY) {
            Minecraft mc = Minecraft.getMinecraft();
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.enableBlend();
            mc.getRenderItem().renderItemAndEffectIntoGUI(stack, x, y);
            mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, stack, x, y,
                    stack.stackSize > 1 ? String.valueOf(stack.stackSize) : "");

            int[] toReturn = new int[2];

            if (mouseX >= x && mouseX <= x + 16 && mouseY >= y && mouseY <= y + 16) {
                GL11.glDisable(GL11.GL_SCISSOR_TEST);
                toReturn = renderToolTip(stack, mouseX, mouseY, screenWidth, screenHeight);
            }

            RenderHelper.disableStandardItemLighting();
            return toReturn;
        }

        public static int[] renderToolTip(ItemStack stack, int x, int y, int width, int height) {
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
            return GuiUtil.drawHoveringText(list, x, y, width, height, 100.0F);
        }

        public static void renderItem3D(ItemStack itemStack, int x, int y, int scale, float rotY) {
            Minecraft mc = Minecraft.getMinecraft();

            GlStateManager.pushMatrix();
            GlStateManager.pushAttrib();
            GlStateManager.translate(x, y, 400.0F);
            GlStateManager.scale(scale, scale, scale);
            GlStateManager.rotate(180f, 1f, 0f, 0f);
            GlStateManager.rotate(rotY, 0f, 1f, 0f);
            Block block = Block.getBlockFromItem(itemStack.getItem());
            if (block != null) GlStateManager.rotate(10f, 1f, 0f, 0f);
            GlStateManager.depthMask(false);
            if (block != null) {
                GlStateManager.enableLighting();

                GuiUtil.setBrightness(1.2D, 1.9D, 1.2D, -2.2D, 1.9D, 1.2D,
                        0.6F,
                        0.3F,
                        0.8F);
            }
            mc.getItemRenderer().renderItem(mc.thePlayer, itemStack, ItemCameraTransforms.TransformType.NONE);
            GlStateManager.depthMask(true);
            GlStateManager.popAttrib();
            GlStateManager.popMatrix();
        }
    }

}
