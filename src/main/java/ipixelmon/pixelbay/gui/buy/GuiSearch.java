package ipixelmon.pixelbay.gui.buy;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import java.io.IOException;

public class GuiSearch extends GuiScreen
{

    public static final int ID = 9745;

    private ISearchList searchList;
    protected GuiPopupSearch popupSearch;
    protected GuiPopupBuy popupBuy;
    private int searchBtnId, pokemonBtnId, itemBtnId;
    private int scrollListWidth = 300, scrollListHeight = 150;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(popupBuy.visible || popupSearch.visible ? 0 : mouseX, popupBuy.visible || popupSearch.visible ? 0 : mouseY, partialTicks);
        searchList.drawScreen(mouseX, mouseY, partialTicks);
        this.drawItemBtnIcon();
        this.drawPokemonBtnIcon();

        popupSearch.draw(mc, mouseX, mouseY);
        popupBuy.draw(mc, mouseX, mouseY);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (!popupSearch.visible && !popupBuy.visible)
        {
            super.keyTyped(typedChar, keyCode);
        }

        popupSearch.keyTyped(typedChar, keyCode);
        popupBuy.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        super.actionPerformed(button);

        int posX = (this.width - scrollListWidth) / 2;
        int posY = (this.height - scrollListHeight) / 2;

        if (button == this.buttonList.get(pokemonBtnId) && !(this.searchList instanceof ListPokemon))
        {
            this.searchList = new ListPokemon(this.mc, scrollListWidth, scrollListHeight, posY + 20, posY + 170, posX, this);
            this.searchList.search(null);
        } else if (button == this.buttonList.get(itemBtnId) && !(this.searchList instanceof ListItem))
        {
            this.searchList = new ListItem(this.mc, scrollListWidth, scrollListHeight, posY + 20, posY + 170, posX, this);
            this.searchList.search(null);
        }

        if (button == this.buttonList.get(searchBtnId))
        {
            this.popupSearch.visible = true;
            this.popupSearch.textField.setFocused(true);
            this.popupSearch.textField.setCanLoseFocus(false);
        }

        searchList.actionPerformed(button);
    }

    @Override
    public void initGui()
    {
        this.buttonList.clear();

        int posX = (this.width - scrollListWidth) / 2;
        int posY = (this.height - scrollListHeight) / 2;
        searchList = new ListItem(this.mc, scrollListWidth, scrollListHeight, posY + 20, posY + 170, posX, this);
        searchList.search(null);

        GuiButton searchBtn = new GuiButton(searchBtnId = 0, posX + searchList.listWidth, posY + 20 + 00, 20, 20, "");

        this.popupSearch = new GuiPopupSearch(this.fontRendererObj, (this.width - GuiPopupSearch.width) / 2, (this.height - GuiPopupSearch.height) / 2, searchList, searchBtn);
        this.popupBuy = new GuiPopupBuy(this.fontRendererObj, (this.width - GuiPopupBuy.width) / 2, (this.height - GuiPopupBuy.height) / 2);
        this.buttonList.add(searchBtn);
        this.buttonList.add(new GuiButton(pokemonBtnId = 1, posX + searchList.listWidth, posY + 20 + 20, 20, 20, ""));
        this.buttonList.add(new GuiButton(itemBtnId = 2, posX + searchList.listWidth, posY + 20 + 40, 20, 20, ""));
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
        popupSearch.update();
        searchList.enabled = !popupBuy.visible && !popupSearch.visible;
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

}
