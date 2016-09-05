package ipixelmon.pixelbay.gui.sell;

import com.pixelmonmod.pixelmon.client.ServerStorageDisplay;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.storage.PCClientStorage;
import ipixelmon.PixelmonUtility;
import ipixelmon.iPixelmon;
import ipixelmon.pixelbay.gui.InfoMessage;
import ipixelmon.pixelbay.gui.InputWindow;
import ipixelmon.pixelbay.gui.BasicScrollList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

public class GuiSellPopup extends InputWindow
{

    protected BasicScrollList scrollList;
    protected InfoMessage.Info infoMessage;

    public GuiSellPopup(final FontRenderer fontRenderer, final int xPosition, final int yPosition, final BasicScrollList scrollList)
    {
        super(fontRenderer, xPosition, yPosition, "Sell");
        this.scrollList = scrollList;
    }

    @Override
    public void draw(Minecraft mc, int mouseX, int mouseY)
    {
        super.draw(mc, mouseX, mouseY);
        if(infoMessage != null && this.isVisible())
        {
            infoMessage.draw(xPosition + (width - mc.fontRendererObj.getStringWidth(infoMessage.getText())) / 2, yPosition + 10, 0xFFFFFF);
        }
    }

    @Override
    public void actionPerformed()
    {
        if(scrollList.selectedIndex < 0)
        {
            return;
        }

        if(textField.getText().isEmpty())
        {
            return;
        }

        try
        {
            Integer.parseInt(textField.getText());
        }catch (NumberFormatException e)
        {
            return;
        }

        if(scrollList instanceof ListItem)
        {
            ItemStack stack = ((ListItem) scrollList).items.get(scrollList.selectedIndex);
            iPixelmon.network.sendToServer(new PacketSellItem(stack, Integer.parseInt(textField.getText())));
            ((ListItem) scrollList).items.remove(scrollList.selectedIndex);
            this.setVisible(false);
        } else if (scrollList instanceof ListPokemon)
        {
            PixelmonData pData = ((ListPokemon) scrollList).pokemon.get(scrollList.selectedIndex);

            if(PixelmonUtility.getPokemonCountClient() == 1) {
                infoMessage = InfoMessage.newMessage(EnumChatFormatting.RED + "You cannot sell your only pokémon.", 3);
                return;
            }

            iPixelmon.network.sendToServer(new PacketSellPokemon(pData, Integer.parseInt(textField.getText())));
            ((ListPokemon) scrollList).pokemon.remove(scrollList.selectedIndex);

            ServerStorageDisplay.changePokemon(pData.order, (PixelmonData) null);
            PCClientStorage.refreshStore();
            this.setVisible(false);
        }

        scrollList.selectedIndex = -1;
    }

    @Override
    public void keyTyped(final char typedChar, final int keyCode)
    {
        super.keyTyped(typedChar, keyCode);
        if(keyCode < 0 || keyCode > 9 && !textField.getText().isEmpty() && keyCode != 11)
        {
            textField.setText(textField.getText().substring(0, textField.getText().length() - 1));
        }
    }
}