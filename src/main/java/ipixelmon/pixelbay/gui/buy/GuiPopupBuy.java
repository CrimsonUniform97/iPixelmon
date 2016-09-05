package ipixelmon.pixelbay.gui.buy;

import ipixelmon.PixelmonUtility;
import ipixelmon.iPixelmon;
import ipixelmon.pixelbay.gui.InfoMessage;
import ipixelmon.pixelbay.gui.InputWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.EnumChatFormatting;

public class GuiPopupBuy extends InputWindow
{

    // TODO: Implement this into ListItem and ListPokemon and GuiSearch
    private ISearchList searchList;
    private InfoMessage.Info infoMessage;

    public GuiPopupBuy(final FontRenderer fontRenderer, final int xPosition, final int yPosition, ISearchList searchList)
    {
        super(fontRenderer, xPosition, yPosition, "Confirm Purchase");
        this.textField.setVisible(false);
        this.actionBtn.width = 100;
        this.actionBtn.xPosition = this.xPosition + (this.width - 100) / 2;
        this.actionBtn.yPosition = this.yPosition + (this.height - 20) / 2;
        this.searchList = searchList;
    }
    @Override
    public void draw(Minecraft mc, int mouseX, int mouseY)
    {
        super.draw(mc, mouseX, mouseY);
        if(infoMessage != null && this.isVisible())
        {
            infoMessage.draw(xPosition + (width - mc.fontRendererObj.getStringWidth(infoMessage.getText())), yPosition + 10, 0xFFFFFF);
        }
    }

    @Override
    public void actionPerformed()
    {

        if (searchList instanceof ListItem)
        {
            ListItem listItem = (ListItem) searchList;
            if (PixelmonUtility.getClientBalance() < listItem.getSelected().price)
            {
                infoMessage = InfoMessage.newMessage(EnumChatFormatting.RED + "Insufficient PokéDollars", 3);
                return;
            }
            iPixelmon.network.sendToServer(new PacketBuyItem(listItem.getSelected().itemStack, listItem.getSelected().seller, listItem.getSelected().price));
            listItem.entries.remove(listItem.selectedIndex);
            this.setVisible(false);
        } else if (searchList instanceof ListPokemon)
        {
            ListPokemon listPokemon = (ListPokemon) searchList;

            if (PixelmonUtility.getClientBalance() < listPokemon.getSelected().price)
            {
                infoMessage = InfoMessage.newMessage(EnumChatFormatting.RED + "Insufficient PokéDollars", 3);
                return;
            }

            iPixelmon.network.sendToServer(new PacketBuyPokemon(listPokemon.getSelected().pixelmonData, listPokemon.getSelected().seller, listPokemon.getSelected().price));
            listPokemon.entries.remove(listPokemon.selectedIndex);
            this.setVisible(false);
        }

        this.setVisible(false);
    }

}
