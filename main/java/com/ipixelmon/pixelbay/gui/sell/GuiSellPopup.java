package com.ipixelmon.pixelbay.gui.sell;

import com.ipixelmon.TimedMessage;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.pixelbay.gui.buy.GuiSearch;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.text.DecimalFormat;

public class GuiSellPopup extends GuiYesNo
{

    private GuiTextField textField;
    private TimedMessage message;

    public GuiSellPopup(final GuiYesNoCallback parentScreen, final String prompt1, final String prompt2, final int delay)
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

        if (message != null)
        {
            messageLine1 = message.getMessage();
        }
    }

    @Override
    public void initGui()
    {
        super.initGui();
        textField = new GuiTextField(1, mc.fontRendererObj, (width - 100) / 2, buttonList.get(0).yPosition - 25, 100, 20);
        textField.setMaxStringLength(11);
    }

    @Override
    protected void actionPerformed(final GuiButton button) throws IOException
    {
        if (button.id == 0 && !textField.getText().isEmpty())
        {
            GuiSell guiSell = (GuiSell) parentScreen;
            long price = Long.parseLong(textField.getText().replaceAll(",", ""));
            if(price > Integer.MAX_VALUE)
            {
                new Thread(message = new TimedMessage("Price is too large. Max Value: " + Integer.MAX_VALUE, 5)).start();
                return;
            }
            if (guiSell.scrollList instanceof ListItem)
            {
                ListItem listItem = (ListItem) guiSell.scrollList;
                iPixelmon.network.sendToServer(new PacketSellItem(listItem.items.get(listItem.selectedIndex), Integer.parseInt(textField.getText().replaceAll(",", ""))));
            } else
            {
                ListPokemon listPokemon = (ListPokemon) guiSell.scrollList;
                iPixelmon.network.sendToServer(new PacketSellPokemon(listPokemon.pokemon.get(listPokemon.selectedIndex), Integer.parseInt(textField.getText().replaceAll(",", ""))));
            }
        }
        Minecraft.getMinecraft().displayGuiScreen((GuiScreen) parentScreen);
        super.actionPerformed(button);
    }

    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException
    {
        if (("" + typedChar).matches("[0-9]+") || keyCode == Keyboard.KEY_DELETE || keyCode == Keyboard.KEY_BACK)
        {
            textField.textboxKeyTyped(typedChar, keyCode);
        }

        DecimalFormat formatter = new DecimalFormat("#,###");
        if(!textField.getText().isEmpty())
        {
            textField.setText(formatter.format(Long.parseLong(textField.getText().replaceAll(",", ""))));
        }

        if (keyCode == Keyboard.KEY_ESCAPE)
        {
            Minecraft.getMinecraft().displayGuiScreen((GuiScreen) parentScreen);
        }
    }


}
