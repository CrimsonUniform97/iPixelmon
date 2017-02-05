package com.ipixelmon.poketournament.client;

import com.ipixelmon.TimedMessage;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.poketournament.Match;
import com.ipixelmon.poketournament.SingleEliminationTournament;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.UUID;

public class TournamentGui extends GuiScreen {

    private UUID arena;

    private SingleEliminationTournament tournament;

    private static final ResourceLocation bgResource = new ResourceLocation(iPixelmon.id,
            "textures/gui/landcontrol/RegionBG.png");

    private static final int BG_WIDTH = 256, BG_HEIGHT = 234;

    private int xPosition, yPosition;

    private int bracketWidth = 90;

    private int xOffset = 0, yOffset = 0;

    private float zoom = 1;

    private boolean drawBrackets = false;

    private GuiTextField teamNameField;

    private TimedMessage timedMessage = new TimedMessage("", 0);

    public TournamentGui(UUID arena, SingleEliminationTournament tournament, boolean drawBrackets) {
        this.arena = arena;
        this.tournament = tournament;
        this.drawBrackets = drawBrackets;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        handleMouseOffset();
        drawBg();
        enableScissorTest();

        if (drawBrackets) {
            drawBrackets();
        } else {
            teamNameField.drawTextBox();
        }

        if (timedMessage.hasMessage()) {
            int msgX = xPosition + ((BG_WIDTH - fontRendererObj.getStringWidth(timedMessage.getMessage())) / 2);
            fontRendererObj.drawString(TextFormatting.RED + timedMessage.getMessage(), msgX, yPosition - 12,
                    0xFFFFFF, true);
        }


        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        if (teamNameField != null) {
            if (fontRendererObj.getStringWidth(teamNameField.getText() + typedChar) < bracketWidth || keyCode == Keyboard.KEY_BACK)
                teamNameField.textboxKeyTyped(typedChar, keyCode);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (teamNameField != null)
            teamNameField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        iPixelmon.network.sendToServer(new PacketSubmitTeam(arena, teamNameField.getText()));
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();

        xPosition = (this.width - BG_WIDTH) / 2;
        yPosition = (this.height - BG_HEIGHT) / 2;

        if (!drawBrackets) {
            teamNameField = new GuiTextField(0, fontRendererObj, xPosition + ((BG_WIDTH - 100) / 2),
                    yPosition + ((BG_HEIGHT - 20) / 2), 100, 20);
            buttonList.add(new GuiButton(1, xPosition + ((BG_WIDTH - 50) / 2), teamNameField.yPosition + 22,
                    50, 20, "Submit"));

            buttonList.get(0).enabled = false;
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();

        if (teamNameField != null) {
            teamNameField.updateCursorCounter();
            buttonList.get(0).enabled = !teamNameField.getText().isEmpty();
        }
    }

    private void drawBrackets() {
        GlStateManager.pushMatrix();
        GlStateManager.translate(BG_WIDTH / 2, BG_HEIGHT / 2, 400f);

        int dWheel = Mouse.getDWheel();

        if (dWheel != 0) {
            zoom += (dWheel / 480.0F);
            zoom = zoom <= 0.5F ? 0.5F : zoom;
            zoom = zoom >= 1.0F ? 1.0F : zoom;
        }

        GlStateManager.scale(zoom, zoom, zoom);

        GlStateManager.translate(-(BG_WIDTH / 2), -(BG_HEIGHT / 2), -400f);
        GlStateManager.translate(xOffset, yOffset, 400f);

        int w = bracketWidth;
        int h = 6;

        int x = xPosition + 4 - w;
        int y = yPosition + 12;

        for (int round = 1; round < tournament.getTotalNumberOfRounds(); round++) {
            y -= h / 2;

            h *= 2;

            drawMatch(x += w, y += (h / 2), w, h, h * 2, round);
        }

        GlStateManager.popMatrix();
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

    // TODO: Draw Round 1 correctly.
    private void drawMatch(int x, int y, int w, int bracketHeight, int bracketYOffsets, int round) {
        for (Match match : tournament.getMatchesForRound(round)) {
            /* Draw first 2 rounds */

//            if(match.prevMatch1 != null && match.prevMatch1.round == 1) {
//                drawLine(round1x, round1Y - bracketHeight, w, 1);
//                drawLine(round1x, round1Y + round1h - bracketHeight, w, 1);
//                drawLine(round1x + w, round1Y - bracketHeight, 1, round1h + 1);
//
//                if (match.prevMatch1.team1 != null)
//                    fontRendererObj.drawString(match.prevMatch1.team1.name, round1x, round1Y - 8 - bracketHeight, 0xFFFFFF);
//                if (match.prevMatch1.team2 != null)
//                    fontRendererObj.drawString(match.prevMatch1.team2.name, round1x, round1Y + round1h - 8 - bracketHeight, 0xFFFFFF);
//            }
//
//
//            if (match.prevMatch2 != null && match.prevMatch2.round == 1) {
//                drawLine(round1x, round1Y, w, 1);
//                drawLine(round1x, round1Y + round1h, w, 1);
//                drawLine(round1x + w, round1Y, 1, round1h + 1);
//
//                if (match.prevMatch2.team1 != null)
//                    fontRendererObj.drawString(match.prevMatch2.team1.name, round1x, round1Y - 8, 0xFFFFFF);
//                if (match.prevMatch2.team2 != null)
//                    fontRendererObj.drawString(match.prevMatch2.team2.name, round1x, round1Y + round1h - 8, 0xFFFFFF);
//            }



            drawLine(x, y, w, 1);
            drawLine(x, y + bracketHeight, w, 1);
            drawLine(x + w, y, 1, bracketHeight + 1);

            /* Draw winner */
            if (match.round == tournament.getTotalNumberOfRounds() - 1) {
                drawLine(x + w, y + (bracketHeight / 2), w, 1);
                if (match.winner != null)
                    fontRendererObj.drawString(match.winner.name, x + w + 2, y + (bracketHeight / 2) - 8, 0xFFFFFF);
            }

            if (match.team1 != null)
                fontRendererObj.drawString(match.team1.name, x + 2, y - 8, 0xFFFFFF);
            if (match.team2 != null)
                fontRendererObj.drawString(match.team2.name, x + 2, y + bracketHeight - 8, 0xFFFFFF);

            y += bracketYOffsets;
        }
    }

    public TimedMessage getTimedMessage() {
        return timedMessage;
    }
}
