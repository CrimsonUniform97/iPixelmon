package com.ipixelmon.team.client;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.pixelbay.gui.ColorPicker;
import com.ipixelmon.team.EnumTeam;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.util.Rectangle;

import java.awt.*;
import java.io.IOException;

public class GuiTeamMenu extends GuiScreen {

    private static final int bgWidth = 300, bgHeight = 200, xDiff = (bgWidth / 3);
    private int posX, posY;

    private static final ResourceLocation teamColossus = new ResourceLocation(iPixelmon.id, "textures/gui/team/colossus.png");
    private static final ResourceLocation teamManta = new ResourceLocation(iPixelmon.id, "textures/gui/team/manta.png");
    private static final ResourceLocation teamOmicron = new ResourceLocation(iPixelmon.id, "textures/gui/team/omicron.png");

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawDefaultBackground();
        super.drawDefaultBackground();
        this.drawRect(new Rectangle(posX, posY, bgWidth, bgHeight), ColorPicker.color(70, 0, 80, 250), ColorPicker.color(90, 0, 100, 250));

        mc.fontRendererObj.setUnicodeFlag(true);
        drawTeam(EnumTeam.Colossus, 0, "Extremely tactical in combat. Team Colossus trainers carry few Pokemon, but are extremely talented with each one.");
        drawTeam(EnumTeam.Manta, 1, "The trainer that goes in guns blazing and doesn't back down. Manta trainers are less about tactics and more about absolute carnage.");
        drawTeam(EnumTeam.Omicron, 2, "Interested in the science of Pokemon. Team Omicron trainers study all Pokemon. They know of each Pokemon's weaknesses and strengths.");

        super.drawScreen(mouseX, mouseY, partialTicks);
        mc.fontRendererObj.setUnicodeFlag(false);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        iPixelmon.network.sendToServer(new PacketChooseTeam(button.id));
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();

        posX = (this.width - bgWidth) / 2;
        posY = (this.height - bgHeight) / 2;

        this.buttonList.add(new GuiButton(0, posX + (xDiff * 0) + ((xDiff - 75) / 2), posY + bgHeight - 25, 75, 20, "Join Colossus"));
        this.buttonList.add(new GuiButton(1, posX + (xDiff * 1) + ((xDiff - 75) / 2), posY + bgHeight - 25, 75, 20, "Join Manta"));
        this.buttonList.add(new GuiButton(2, posX + (xDiff * 2) + ((xDiff - 75) / 2), posY + bgHeight - 25, 75, 20, "Join Omicron"));
    }

    private void drawTeam(EnumTeam team, int index, String description) {
        GlStateManager.color(1, 1, 1, 1);

        mc.getTextureManager().bindTexture(team == EnumTeam.Colossus ? teamColossus : team == EnumTeam.Omicron ? teamOmicron : teamManta);
        GuiHelper.drawImageQuad(posX + (xDiff * index) + ((xDiff - 65) / 2), posY + 2, 65, 65, 0.0D, 0.0D, 1.0D, 1.0D, 0.0F);
        mc.fontRendererObj.drawString(team.colorChat() + "Team " + team.name(), posX + (xDiff * index) + ((xDiff - mc.fontRendererObj.getStringWidth("Team " + team.name())) / 2), posY + 70, 16777215);
        mc.fontRendererObj.drawSplitString("", posX + (xDiff * index), posY + 80, xDiff, 0xFFFFFF);
        mc.fontRendererObj.drawSplitString(description, posX + (xDiff * index) + 10, posY + 80, xDiff - 10, 16777215);
    }

    public void drawRect(Rectangle rect, Color bgColor, Color trimColor) {
        int x = rect.getX(), y = rect.getY(), w = rect.getWidth(), h = rect.getHeight();
        x += 4;
        y += 4;
        w -= 8;
        h -= 8;
        int l = bgColor.getRGB();
        this.drawGradientRect(x - 3, y - 4, x + w + 3, y - 3, l, l);
        this.drawGradientRect(x - 3, y + h + 3, x + w + 3, y + h + 4, l, l);
        this.drawGradientRect(x - 3, y - 3, x + w + 3, y + h + 3, l, l);
        this.drawGradientRect(x - 4, y - 3, x - 3, y + h + 3, l, l);
        this.drawGradientRect(x + w + 3, y - 3, x + w + 4, y + h + 3, l, l);
        int i1 = trimColor.getRGB();
        int j1 = (i1 & 16711422 /* white */) >> 1 | i1 & -16777216 /* black */;
        this.drawGradientRect(x - 3, y - 3 + 1, x - 3 + 1, y + h + 3 - 1, i1, j1);
        this.drawGradientRect(x + w + 2, y - 3 + 1, x + w + 3, y + h + 3 - 1, i1, j1);
        this.drawGradientRect(x - 3, y - 3, x + w + 3, y - 3 + 1, i1, i1);
        this.drawGradientRect(x - 3, y + h + 2, x + w + 3, y + h + 3, j1, j1);
    }

}
