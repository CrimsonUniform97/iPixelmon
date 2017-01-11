package com.ipixelmon.gym.client;

import com.ipixelmon.gym.Gym;
import com.ipixelmon.iPixelmon;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;

public class GuiGymInfo extends GuiScreen {

    private static final ResourceLocation BG_TEXTURE = new ResourceLocation(iPixelmon.id, "textures/gui/landcontrol/RegionBG.png");
    private static final int BG_HEIGHT = 234, BG_WIDTH = 256;
    private static int POS_X, POS_Y;

    private Gym gym;

    public GuiGymInfo(Gym gym) {
        this.gym = gym;
    }

    // TODO: Work on gui

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.color(1, 1, 1, 1);
        mc.getTextureManager().bindTexture(BG_TEXTURE);
        drawTexturedModalRect(POS_X, POS_Y, 0, 0, BG_WIDTH, BG_HEIGHT);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();

        POS_X = (this.width - BG_WIDTH) / 2;
        POS_Y = (this.height - BG_HEIGHT) / 2;

        this.buttonList.add(new GuiButton(0, POS_X + 5, POS_Y + ((this.height - 20) / 2),
                20, 20, "<"));
        this.buttonList.add(new GuiButton(1, POS_X + BG_WIDTH - 25, POS_Y + ((this.height - 20) / 2),
                20, 20, ">"));
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }
}
