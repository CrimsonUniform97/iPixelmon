package ipixelmon.pixelbay.gui.sell;

import ipixelmon.iPixelmon;
import ipixelmon.pixelbay.gui.buy.GuiSearch;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class GuiSellPopup extends GuiYesNo
{

    private GuiTextField textField;

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
    }

    @Override
    public void initGui()
    {
        super.initGui();
        textField = new GuiTextField(1, mc.fontRendererObj, (width - 100) / 2, buttonList.get(0).yPosition - 25, 100, 20);
    }

    @Override
    protected void actionPerformed(final GuiButton button) throws IOException
    {
        if(button.id == 0)
        {
            GuiSell guiSell = (GuiSell) parentScreen;
            if(guiSell.scrollList instanceof ListItem)
            {
                ListItem listItem = (ListItem) guiSell.scrollList;
                iPixelmon.network.sendToServer(new PacketSellItem(listItem.items.get(listItem.selectedIndex), Integer.parseInt(textField.getText())));
            } else
            {
                ListPokemon listPokemon = (ListPokemon) guiSell.scrollList;
                iPixelmon.network.sendToServer(new PacketSellPokemon(listPokemon.pokemon.get(listPokemon.selectedIndex), Integer.parseInt(textField.getText())));
            }
        }
        Minecraft.getMinecraft().displayGuiScreen((GuiScreen) parentScreen);
        super.actionPerformed(button);
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

}
