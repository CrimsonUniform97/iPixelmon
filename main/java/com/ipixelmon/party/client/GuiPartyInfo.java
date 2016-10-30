package com.ipixelmon.party.client;

import com.ipixelmon.iPixelmon;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.util.Rectangle;

import java.io.IOException;

/**
 * Created by colby on 10/27/2016.
 */
public class GuiPartyInfo extends GuiScreen {

    private int posX, posY;
    private GuiTextField textField;
    private static final Rectangle bgBounds = new Rectangle(0, 0, 176, 166);
    private static final ResourceLocation bgTexture = new ResourceLocation(iPixelmon.id, "textures/gui/party/party.png");

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        mc.getTextureManager().bindTexture(bgTexture);
        drawTexturedModalRect(posX, posY, bgBounds.getX(), bgBounds.getY(), bgBounds.getWidth(), bgBounds.getHeight());
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
    public void updateScreen() {
        super.updateScreen();
    }

    @Override
    public void initGui() {
        super.initGui();
        buttonList.clear();
        posX = (width - bgBounds.getWidth()) / 2;
        posY = (height - bgBounds.getHeight()) / 2;
    }
}
