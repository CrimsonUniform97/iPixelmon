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
    private List<GuiList.ListObject> listObjects;

    @Override
    public final void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        this.drawDefaultBackground();
        this.guiList.draw(this.mc, mouseX, mouseY);
    }

    @Override
    protected final void keyTyped(final char typedChar, final int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        this.guiList.keyTyped(typedChar, keyCode);
    }

    @Override
    protected final void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        this.guiList.mouseClicked(this.mc, mouseX, mouseY);
    }

    @Override
    protected final void actionPerformed(final GuiButton button) throws IOException {
        super.actionPerformed(button);
    }

    @Override
    public final void initGui() {
        super.initGui();
        this.buttonList.clear();
        this.listObjects = new ArrayList<>();

        final int listWidth = this.width - 50, listHeight = this.height - 50;
        final int listX = (this.width - listWidth) /2, listY = (this.height - listHeight) / 2;


        final PixelmonData[] pixelList = ServerStorageDisplay.pokemon;

        // Add Pokemon
        for(PixelmonData pData : pixelList) if(pData != null) this.listObjects.add(new PokemonListObject(30, 28, pData));

        // Add Items
        for(ItemStack stack : Minecraft.getMinecraft().thePlayer.inventory.mainInventory) if(stack != null) this.listObjects.add(new ItemListObject(30, 20, stack));

        // Add Armor
        for(ItemStack stack : Minecraft.getMinecraft().thePlayer.inventory.armorInventory) if(stack != null) this.listObjects.add(new ItemListObject(30, 20, stack));

        this.guiList = new SellList(listX, listY, listWidth, listHeight, this.listObjects);
    }

    @Override
    public final void updateScreen() {
        super.updateScreen();
        this.guiList.updateScreen();
    }
}
