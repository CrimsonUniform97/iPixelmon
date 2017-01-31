package com.ipixelmon.poketournament.client;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.poketournament.Match;
import com.ipixelmon.poketournament.SingleElimationTournament;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class TournamentGui extends GuiScreen {

    private SingleElimationTournament tournament;

    private static final ResourceLocation bgResource = new ResourceLocation(iPixelmon.id,
            "textures/gui/landcontrol/RegionBG.png");

    private static final int BG_WIDTH = 256, BG_HEIGHT = 234;

    private int xPosition, yPosition;

    public TournamentGui(SingleElimationTournament tournament) {
        this.tournament = tournament;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        mc.getTextureManager().bindTexture(bgResource);
        drawTexturedModalRect(xPosition, yPosition, 0, 0, BG_WIDTH, BG_HEIGHT);

        int x = xPosition;
        int y = yPosition;
        for(int round = 0; round < tournament.getRound(); round++) {
            for(Match match : tournament.getMatchesForRound(round)) {
                drawLine(x, y, 50, 1);
                drawLine(x, y + 20, 50, 1);
                y += 60;
            }
        }
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

        xPosition = (this.width - BG_WIDTH) / 2;
        yPosition = (this.height - BG_HEIGHT) / 2;
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }

    private void drawLine(int x, int y, int width, int height) {
        GlStateManager.disableTexture2D();
        GlStateManager.color(0, 0, 0, 1);
        drawTexturedModalRect(x, y, 0, 0, width, height);
        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.enableTexture2D();
    }
}
