package ipixelmon.pixelbay.gui.sell;

import com.pixelmonmod.pixelmon.client.ServerStorageDisplay;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import ipixelmon.GuiList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class SellGui extends GuiScreen {

    public static final int ID = 987;

    private SellList guiList;

    public SellGui() {
        this.guiList = new SellList(this);
    }

    @Override
    public final void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        this.drawDefaultBackground();
        this.guiList.drawList(mouseX, mouseY, this.mc);
    }

    @Override
    protected final void keyTyped(final char typedChar, final int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        this.guiList.keyTyped(typedChar, keyCode);
    }

    @Override
    protected final void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        this.guiList.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected final void actionPerformed(final GuiButton button) throws IOException {
        super.actionPerformed(button);
    }

    @Override
    public final void initGui() {
        super.initGui();
        this.buttonList.clear();

        final PixelmonData[] pixelList = ServerStorageDisplay.pokemon;
        // Add Pokemon
        for(PixelmonData pData : pixelList) if(pData != null) this.guiList.addObject(new PokemonSellObject(pData));

        // Add Items
        for(ItemStack stack : Minecraft.getMinecraft().thePlayer.inventory.mainInventory) if(stack != null) this.guiList.addObject(new ItemSellObject(stack));

        // Add Armor
        for(ItemStack stack : Minecraft.getMinecraft().thePlayer.inventory.armorInventory) if(stack != null) this.guiList.addObject(new ItemSellObject(stack));

        this.guiList.initGui();
    }

    @Override
    public final void updateScreen() {
        super.updateScreen();
        this.guiList.update();
    }
}
