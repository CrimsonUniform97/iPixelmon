package com.ipixelmon.pixelbay.gui.buy;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.ipixelmon.iPixelmon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.io.IOException;

public class GuiPopupSearch extends GuiYesNo
{

    private static final ResourceLocation searchIcon = new ResourceLocation(iPixelmon.id + ":pixelbay/textures/gui/SearchIcon.png");

    private GuiTextField textField;

    public GuiPopupSearch(final GuiYesNoCallback parentScreen, final String prompt1, final String prompt2, final int delay)
    {
        super(parentScreen, prompt1, prompt2, delay);
    }

    @Override
    public void drawScreen(final int mouseX, final int mouseY, final float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        textField.drawTextBox();
    }

    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        textField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
        textField.updateCursorCounter();
    }

    @Override
    public void initGui()
    {
        super.initGui();
        buttonList.remove(1);
        buttonList.get(0).displayString = "Search";
        buttonList.get(0).xPosition = (width - this.buttonList.get(0).width) / 2;
        buttonList.get(0).yPosition = 20 + (height - this.buttonList.get(0).height) / 2;
        textField = new GuiTextField(1, mc.fontRendererObj, (width - 100) / 2, buttonList.get(0).yPosition - 25, 100, 20);
    }

    @Override
    protected void actionPerformed(final GuiButton button) throws IOException
    {
        Minecraft.getMinecraft().displayGuiScreen((GuiScreen) parentScreen);
        ((GuiSearch) parentScreen).searchList.search(textField.getText());
    }

    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException
    {
        textField.textboxKeyTyped(typedChar, keyCode);

        if (keyCode == Keyboard.KEY_ESCAPE)
        {
            Minecraft.getMinecraft().displayGuiScreen((GuiScreen) parentScreen);
        }
    }

    public static void drawSearchButton(Minecraft mc, GuiButton searchBtn)
    {
        mc.getTextureManager().bindTexture(searchIcon);
        GlStateManager.enableBlend();
        int iconWidth = 16, iconHeight = 16;
        GlStateManager.color(0f, 0f, 0f, 128f / 255f);
        GuiHelper.drawImageQuad(searchBtn.xPosition + ((searchBtn.width - iconWidth) / 2), searchBtn.yPosition + ((searchBtn.height - iconHeight) / 2) + 1, iconWidth, iconHeight, 0.0D, 0.0D, 1.0D, 1.0D, 0.0F);

        Color color = new Color(14737632);

        if (searchBtn.isMouseOver()) color = new Color(16777120);

        GlStateManager.color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 1f);
        GuiHelper.drawImageQuad(searchBtn.xPosition + ((searchBtn.width - iconWidth) / 2), searchBtn.yPosition + ((searchBtn.height - iconHeight) / 2), iconWidth, iconHeight, 0.0D, 0.0D, 1.0D, 1.0D, 0.0F);
    }

}
