package ipixelmon.pixelbay.gui.buy;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import ipixelmon.iPixelmon;
import net.minecraft.client.Minecraft;
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

    protected ISearchList searchList;
    private int searchBtnId, pokemonBtnId, itemBtnId;
    private int scrollListWidth = 300, scrollListHeight = 150;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        searchList.drawScreen(mouseX, mouseY, partialTicks);
        GuiPopupSearch.drawSearchButton(mc, this.buttonList.get(searchBtnId));
        this.drawItemBtnIcon();
        this.drawPokemonBtnIcon();
    }

    @Override
    public void confirmClicked(final boolean result, final int id)
    {
        if(result)
        {
            if(searchList instanceof ListItem)
            {
                ListItem listItem = (ListItem) searchList;
                ListItem.ItemListInfo info = listItem.entries.get(listItem.selectedIndex);
                iPixelmon.network.sendToServer(new PacketBuyItem(info.itemStack, info.seller, info.price));
            } else
            {
                ListPokemon listPokemon = (ListPokemon) searchList;
                ListPokemon.PokeListInfo info = listPokemon.entries.get(listPokemon.selectedIndex);
                if (info != null)
                {
                    iPixelmon.network.sendToServer(new PacketBuyPokemon(info.pixelmonData, info.seller, info.price));
                }
            }
        }

        Minecraft.getMinecraft().displayGuiScreen(this);
        super.confirmClicked(result, id);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        super.actionPerformed(button);

        int posX = (width - scrollListWidth) / 2;
        int posY = (height - scrollListHeight) / 2;

        if (button == buttonList.get(pokemonBtnId) && !(searchList instanceof ListPokemon))
        {
            searchList = new ListPokemon(mc, scrollListWidth, scrollListHeight, posY + 20, posY + 170, posX, this);
            searchList.search(null);
        } else if (button == buttonList.get(itemBtnId) && !(searchList instanceof ListItem))
        {
            searchList = new ListItem(mc, scrollListWidth, scrollListHeight, posY + 20, posY + 170, posX, this);
            searchList.search(null);
        }

        if (button == buttonList.get(searchBtnId))
        {
            Minecraft.getMinecraft().displayGuiScreen(new GuiPopupSearch(this, "", "", 3));
            return;
        }

        searchList.actionPerformed(button);
    }

    @Override
    public void initGui()
    {
        buttonList.clear();

        int posX = (width - scrollListWidth) / 2;
        int posY = (height - scrollListHeight) / 2;
        searchList = new ListItem(mc, scrollListWidth, scrollListHeight, posY + 20, posY + 170, posX, this);
        searchList.search(null);

        buttonList.add(new GuiButton(searchBtnId = 0, posX + searchList.listWidth, posY + 20 + 00, 20, 20, ""));
        buttonList.add(new GuiButton(pokemonBtnId = 1, posX + searchList.listWidth, posY + 20 + 20, 20, 20, ""));
        buttonList.add(new GuiButton(itemBtnId = 2, posX + searchList.listWidth, posY + 20 + 40, 20, 20, ""));
    }

    private void drawPokemonBtnIcon()
    {
        GlStateManager.enableBlend();
        GlStateManager.color(1, 1, 1, 1);
        PixelmonData pData = new PixelmonData();
        pData.name = EnumPokemon.Pikachu.name;
        GuiHelper.bindPokemonSprite(pData, mc);
        GuiButton pokeBtn = buttonList.get(pokemonBtnId);
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

        GuiButton itemBtn = buttonList.get(itemBtnId);
        if (mc.getRenderItem() != null)
            mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, itemBtn.xPosition + ((itemBtn.width - 16) / 2), itemBtn.yPosition + ((itemBtn.height - 16) / 2));

        RenderHelper.disableStandardItemLighting();
    }

}
