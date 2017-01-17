package com.ipixelmon.gym.client;

import com.google.common.collect.Lists;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.util.PixelmonAPI;
import com.pixelmonmod.pixelmon.client.render.RenderPixelmon;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public abstract class GuiPickPixelmon extends Gui {

    private static final ResourceLocation BG_TEXTURE = new ResourceLocation(iPixelmon.id, "textures/gui/landcontrol/RegionBG.png");
    private static final int BG_HEIGHT = 234, BG_WIDTH = 256;
    private int POS_X, POS_Y;

    private List<EntityPixelmon> pixelmonList = Lists.newArrayList();
    private int page = 0;
    private GuiButton left, right, select;
    private GuiScreen parent;
    public boolean enabled = true;

    private RenderPixelmon renderPixelmon;
    private float pixelmonDisplayRotY = 0.0F;

    public GuiPickPixelmon(GuiScreen parent) {
        this.parent = parent;
        pixelmonList = PixelmonAPI.Client.getPixelmon(true);
        renderPixelmon = new RenderPixelmon(parent.mc.getRenderManager());
    }

    public void drawScreen(Minecraft mc, int mouseX, int mouseY) {
        if (!enabled) return;

        GlStateManager.color(1, 1, 1, 1);

        /**
         * Draw background
         */
        mc.getTextureManager().bindTexture(BG_TEXTURE);
        drawTexturedModalRect(POS_X, POS_Y, 0, 0, BG_WIDTH, BG_HEIGHT);

        /**
         * Draw pokemon
         */
        if (!pixelmonList.isEmpty()) {
            PixelmonAPI.Client.renderPixelmon3D(pixelmonList.get(page), parent.width / 2, POS_Y + BG_HEIGHT - 40, 400, 40.0F,
                    pixelmonDisplayRotY += 0.66F);
            PixelmonAPI.Client.renderPixelmonTip(pixelmonList.get(page), POS_X, POS_Y + 24, parent.width, parent.height);
        }

        left.drawButton(mc, mouseX, mouseY);
        right.drawButton(mc, mouseX, mouseY);
        select.drawButton(mc, mouseX, mouseY);
    }

    public void mouseClicked(Minecraft mc, int mouseX, int mouseY) {
        if (!enabled) return;

        if (left.mousePressed(mc, mouseX, mouseY)) {
            page = page - 1 < 0 ? 0 : page - 1;
        } else if (right.mousePressed(mc, mouseX, mouseY)) {
            page = page + 1 >= pixelmonList.size() ? pixelmonList.size() - 1 : page + 1;
        } else if (select.mousePressed(mc, mouseX, mouseY)) {
            onSelect(pixelmonList.get(page));
        }
    }

    public void initGui() {
        POS_X = (parent.width - BG_WIDTH) / 2;
        POS_Y = (parent.height - BG_HEIGHT) / 2;

        left = new GuiButton(0, POS_X + 5, POS_Y + ((parent.height - 20) / 2),
                20, 20, "<");
        right = new GuiButton(1, POS_X + BG_WIDTH - 25, POS_Y + ((parent.height - 20) / 2),
                20, 20, ">");

        select = new GuiButton(2, POS_X + ((BG_WIDTH - 50) / 2), POS_Y + BG_HEIGHT - 25,
                50, 20, "Select");

        left.enabled = !pixelmonList.isEmpty();
        right.enabled = !pixelmonList.isEmpty();
        select.enabled = !pixelmonList.isEmpty();
    }

    public void updateScreen() {
        left.enabled = !(page <= 0);
        right.enabled = !(page >= pixelmonList.size() - 1);
        select.enabled = !pixelmonList.get(page).isFainted;
    }

    public abstract void onSelect(EntityPixelmon pixelmon);
}
