package ipixelmon.pixelbay.gui.sell;

import com.pixelmonmod.pixelmon.client.ServerStorageDisplay;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.storage.PCClientStorage;
import ipixelmon.PixelmonUtility;
import ipixelmon.iPixelmon;
import ipixelmon.pixelbay.gui.InputWindow;
import ipixelmon.pixelbay.gui.search.BasicScrollList;
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

        if(scrollList instanceof ListItem)
        {
            ItemStack stack = ((ListItem) scrollList).items.get(scrollList.selectedIndex);
            iPixelmon.network.sendToServer(new PacketSellItem(stack, Integer.parseInt(textField.getText().replaceAll("\\$", ""))));
            ((ListItem) scrollList).items.remove(scrollList.selectedIndex);
        } else if (scrollList instanceof ListPokemon)
        {
            PixelmonData pData = ((ListPokemon) scrollList).pokemon.get(scrollList.selectedIndex);

            if(PixelmonUtility.getPokemonCountClient() == 1) {
                Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText("You cannot sell your only items."));
                return;
            }

            iPixelmon.network.sendToServer(new PacketSellPokemon(pData, Integer.parseInt(textField.getText().replaceAll("\\$", ""))));
            ((ListPokemon) scrollList).pokemon.remove(scrollList.selectedIndex);

            ServerStorageDisplay.changePokemon(pData.order, (PixelmonData) null);
            PCClientStorage.refreshStore();
        }

        scrollList.selectedIndex = -1;
    }
}