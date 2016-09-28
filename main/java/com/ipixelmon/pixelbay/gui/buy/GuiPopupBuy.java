package com.ipixelmon.pixelbay.gui.buy;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GuiPopupBuy extends GuiYesNo
{

    public GuiPopupBuy(final GuiYesNoCallback parentScreen, final String prompt1, final String prompt2, final int delay)
    {
        super(parentScreen, prompt1, prompt2, delay);
    }

    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException
    {
        if(keyCode == Keyboard.KEY_ESCAPE)
        {
            Minecraft.getMinecraft().displayGuiScreen((GuiScreen) parentScreen);
        }
    }

}
