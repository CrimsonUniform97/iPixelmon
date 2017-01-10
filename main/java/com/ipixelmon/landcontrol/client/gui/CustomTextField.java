package com.ipixelmon.landcontrol.client.gui;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.ipixelmon.*;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ChatAllowedCharacters;
import net.minecraft.util.MathHelper;

/**
 * Created by colby on 1/9/2017.
 */
public class CustomTextField extends com.ipixelmon.GuiTextField {

    public CustomTextField(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void drawString(FontRenderer fontRendererIn, String text, int x, int y, int color) {
        super.drawString(fontRendererIn, text, x, y, color);
    }
}