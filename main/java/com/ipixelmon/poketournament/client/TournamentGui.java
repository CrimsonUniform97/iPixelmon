package com.ipixelmon.poketournament.client;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.poketournament.Match;
import com.ipixelmon.poketournament.SingleEliminationTournament;
import com.ipixelmon.poketournament.Team;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;

public class TournamentGui extends GuiScreen {

    private SingleEliminationTournament tournament;

    private static final ResourceLocation bgResource = new ResourceLocation(iPixelmon.id,
            "textures/gui/landcontrol/RegionBG.png");

    private static final int BG_WIDTH = 256, BG_HEIGHT = 234;

    private int xPosition, yPosition;

    private int xOffset = 0, yOffset = 0;

    private int oldMouseX, oldMouseY;

    public TournamentGui(SingleEliminationTournament tournament) {
        this.tournament = tournament;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        handleMouseOffset();
        drawBg();
        enableScissorTest();

        GlStateManager.pushMatrix();
        GlStateManager.translate(xOffset, yOffset, 400f);

        int x = xPosition + 4;
        int y = yPosition + 12;
        int w = 50;
        int h = 12;

        // TODO: Work on the rendering, and putting teams to face the winner of a first round.
        for (int round = 1; round < tournament.getTotalNumberOfRounds(); round++) {
            for (Match match : tournament.getMatchesForRound(round)) {
                drawLine(x, y, w, 1);
                drawLine(x, y + h, w, 1);
                drawLine(x + w, y, 1, h + 1);

                if (match.team1 != null) fontRendererObj.drawString(match.team1.name, x, y - 8, 0xFFFFFF);
                if (match.team2 != null) fontRendererObj.drawString(match.team2.name, x, y + h - 8, 0xFFFFFF);

                y += (30 * (round - 1));
            }

            x += w;
            y = (yPosition + 12) + ((h / 2) * round);
        }

        GlStateManager.popMatrix();

        GL11.glDisable(GL11.GL_SCISSOR_TEST);
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

    private void drawBg() {
        mc.getTextureManager().bindTexture(bgResource);
        drawTexturedModalRect(xPosition, yPosition, 0, 0, BG_WIDTH, BG_HEIGHT);
    }

    private void enableScissorTest() {
        ScaledResolution res = new ScaledResolution(Minecraft.getMinecraft());
        double scaleW = Minecraft.getMinecraft().displayWidth / res.getScaledWidth_double();
        double scaleH = Minecraft.getMinecraft().displayHeight / res.getScaledHeight_double();

        int left = xPosition;
        int bottom = yPosition + BG_HEIGHT - 3;
        int listWidth = BG_WIDTH;
        int viewHeight = BG_HEIGHT - 4;

        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor((int) (left * scaleW), (int) (mc.displayHeight - (bottom * scaleH)),
                (int) (listWidth * scaleW), (int) (viewHeight * scaleH));
    }

    private void handleMouseOffset() {
        if (Mouse.isButtonDown(0)) {
            xOffset += Mouse.getDX();
            yOffset += -Mouse.getDY();
        }
    }
}
