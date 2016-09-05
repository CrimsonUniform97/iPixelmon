package ipixelmon.pixelbay.gui.sell;

import com.pixelmonmod.pixelmon.client.ServerStorageDisplay;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import ipixelmon.pixelbay.gui.BasicScrollList;
import ipixelmon.pixelbay.gui.buy.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class SellGui extends GuiScreen {

    public static final int ID = 987;

    private BasicScrollList scrollList;
    protected GuiSellPopup sellPopup;
    private int pokemonBtnId, itemBtnId;
    private int scrollListWidth = 300, scrollListHeight = 150;

    @Override
    public final void drawScreen(int mouseX, int mouseY, final float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        scrollList.drawScreen(mouseX, mouseY, partialTicks);
        this.drawItemBtnIcon();
        this.drawPokemonBtnIcon();
        sellPopup.draw(mc, mouseX, mouseY);
    }

    @Override
    protected final void keyTyped(final char typedChar, final int keyCode) throws IOException {
        if(!sellPopup.isVisible())
        {
            super.keyTyped(typedChar, keyCode);
        }
        sellPopup.keyTyped(typedChar, keyCode);
    }

    @Override
    protected final void actionPerformed(final GuiButton button) throws IOException {
        super.actionPerformed(button);

        int posX = (this.width - scrollListWidth) / 2;
        int posY = (this.height - scrollListHeight) / 2;

        if (button == this.buttonList.get(pokemonBtnId) && !(this.scrollList instanceof ListPokemon))
        {
            this.scrollList = new ListPokemon(this.mc, scrollListWidth, scrollListHeight, posY + 20, posY + 170, posX, 30, getPokemon(), this);
        } else if (button == this.buttonList.get(itemBtnId) && !(this.scrollList instanceof ListItem))
        {
            this.scrollList = new ListItem(this.mc, scrollListWidth, scrollListHeight, posY + 20, posY + 170, posX, 30, getItems(), this);
        }

        sellPopup.scrollList = scrollList;

        scrollList.actionPerformed(button);
    }

    @Override
    public final void initGui() {
        super.initGui();
        this.buttonList.clear();

        int posX = (this.width - scrollListWidth) / 2;
        int posY = (this.height - scrollListHeight) / 2;
        scrollList = new ListItem(this.mc, scrollListWidth, scrollListHeight, posY + 20, posY + 170, posX, 30, getItems(), this);

        this.sellPopup = new GuiSellPopup(this.fontRendererObj, (this.width - GuiPopupSearch.width) / 2, (this.height - GuiPopupSearch.height) / 2, scrollList);

        this.buttonList.add(new GuiButton(pokemonBtnId = 0, posX + scrollList.listWidth, posY + 20 + 20, 20, 20, ""));
        this.buttonList.add(new GuiButton(itemBtnId = 1, posX + scrollList.listWidth, posY + 20 + 40, 20, 20, ""));
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
        sellPopup.update();
        scrollList.enabled = !sellPopup.isVisible();
    }

    private void drawPokemonBtnIcon()
    {
        GlStateManager.enableBlend();
        GlStateManager.color(1, 1, 1, 1);
        PixelmonData pData = new PixelmonData();
        pData.name = EnumPokemon.Pikachu.name;
        GuiHelper.bindPokemonSprite(pData, mc);
        GuiButton pokeBtn = this.buttonList.get(pokemonBtnId);
        GuiHelper.drawImageQuad(pokeBtn.xPosition + ((pokeBtn.width - 24) / 2), pokeBtn.yPosition + ((pokeBtn.height - 24) / 2) - 3, 24, 24, 0.0D, 0.0D, 1.0D, 1.0D, 0.0F);
    }

    private void drawItemBtnIcon()
    {
        ItemStack itemStack = new ItemStack(Items.iron_pickaxe);

        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        RenderHelper.enableGUIStandardItemLighting();

        GuiButton itemBtn = this.buttonList.get(this.itemBtnId);
        if (mc.getRenderItem() != null)
            mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, itemBtn.xPosition + ((itemBtn.width - 16) / 2), itemBtn.yPosition + ((itemBtn.height - 16) / 2));

        RenderHelper.disableStandardItemLighting();
    }

    public List<ItemStack> getItems()
    {
        List<ItemStack> items = new ArrayList<>();

        // Add Items
        for(ItemStack stack : Minecraft.getMinecraft().thePlayer.inventory.mainInventory) if(stack != null) items.add(stack);

        // Add Armor
        for(ItemStack stack : Minecraft.getMinecraft().thePlayer.inventory.armorInventory) if(stack != null) items.add(stack);

        return items;
    }

    public List<PixelmonData> getPokemon()
    {
        List<PixelmonData> pDataList = new ArrayList<>();

        final PixelmonData[] pixelList = ServerStorageDisplay.pokemon;

        for(PixelmonData pData : pixelList) if(pData != null) pDataList.add(pData);

        return pDataList;
    }

}
