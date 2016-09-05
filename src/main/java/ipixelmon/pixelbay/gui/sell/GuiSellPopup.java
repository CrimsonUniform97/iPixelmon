package ipixelmon.pixelbay.gui.sell;

import com.pixelmonmod.pixelmon.client.ServerStorageDisplay;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.storage.PCClientStorage;
import ipixelmon.PixelmonUtility;
import ipixelmon.iPixelmon;
import ipixelmon.pixelbay.gui.InputWindow;
import ipixelmon.pixelbay.gui.BasicScrollList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;

public class GuiSellPopup extends InputWindow
{

    private BasicScrollList scrollList;

    public GuiSellPopup(final FontRenderer fontRenderer, final int xPosition, final int yPosition, final BasicScrollList scrollList)
    {
        super(fontRenderer, xPosition, yPosition, "Sell");
        this.scrollList = scrollList;
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
        } else if (scrollList instanceof ListPokemon)
        {
            PixelmonData pData = ((ListPokemon) scrollList).pokemon.get(scrollList.selectedIndex);

            if(PixelmonUtility.getPokemonCountClient() == 1) {
                Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText("You cannot sell your only items."));
                return;
            }

            iPixelmon.network.sendToServer(new PacketSellPokemon(pData, Integer.parseInt(textField.getText())));
            ((ListPokemon) scrollList).pokemon.remove(scrollList.selectedIndex);

            ServerStorageDisplay.changePokemon(pData.order, (PixelmonData) null);
            PCClientStorage.refreshStore();
        }

        scrollList.selectedIndex = -1;
    }

    @Override
    public void keyTyped(final char typedChar, final int keyCode)
    {
        super.keyTyped(typedChar, keyCode);
        if(keyCode < 0 || keyCode > 9 && !textField.getText().isEmpty())
        {
            textField.setText(textField.getText().substring(0, textField.getText().length() - 1));
        }
    }
}