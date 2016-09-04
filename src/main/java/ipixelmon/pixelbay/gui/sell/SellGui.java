package ipixelmon.pixelbay.gui.sell;

import com.pixelmonmod.pixelmon.client.ServerStorageDisplay;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import ipixelmon.pixelbay.gui.search.*;
import ipixelmon.pixelbay.gui.search.ListItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;

import java.io.IOException;

public final class SellGui extends GuiScreen {

    public static final int ID = 987;

    private BasicScrollList scrollList;

    @Override
    public final void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected final void keyTyped(final char typedChar, final int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected final void actionPerformed(final GuiButton button) throws IOException {
        super.actionPerformed(button);
    }

    @Override
    public final void initGui() {
        super.initGui();
        this.buttonList.clear();
        // TODO: Implement ListItem and ListPokemon, also will need to draw buttons to switch in between the two. Also need to test it out and test out the search screen after redoing everything.
        // TODO: Also need to make the ListItem and ListPokemon have the same appearance as the search screen.
        scrollList = new ListItem()

        final PixelmonData[] pixelList = ServerStorageDisplay.pokemon;
        // Add Pokemon
        for(PixelmonData pData : pixelList) if(pData != null) this.guiList.addObject(new PokemonSellObject(pData));

        // Add Items
        for(ItemStack stack : Minecraft.getMinecraft().thePlayer.inventory.mainInventory) if(stack != null) this.guiList.addObject(new ItemSellObject(stack));

        // Add Armor
        for(ItemStack stack : Minecraft.getMinecraft().thePlayer.inventory.armorInventory) if(stack != null) this.guiList.addObject(new ItemSellObject(stack));

        this.guiList.initGui();
    }

}
