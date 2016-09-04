package ipixelmon.pixelbay.gui.search;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.GuiScrollingList;

import java.io.IOException;

public class GuiSearch extends GuiScreen
{

    public static final int ID = 9745;

    private ISearchList searchList;
    private GuiSearchPopup popupWindow;
    private int searchBtnId, pokemonBtnId, itemBtnId;
    private int scrollListWidth = 300, scrollListHeight = 150;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);

        GlStateManager.color(1, 1, 1, 1);

        searchList.drawScreen(mouseX, mouseY, partialTicks);
        this.drawItemBtnIcon();
        this.drawPokemonBtnIcon();

        popupWindow.draw(mc, mouseX, mouseY);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        super.keyTyped(typedChar, keyCode);
        popupWindow.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        super.actionPerformed(button);

        int posX = (this.width - scrollListWidth) / 2;
        int posY = (this.height - scrollListHeight) / 2;

        if (button == this.buttonList.get(pokemonBtnId) && !(this.searchList instanceof ListPokemon))
        {
            this.searchList = new ListPokemon(this.mc, scrollListWidth, scrollListHeight, posY + 20, posY + 170, posX, 30, this.width, this.height);
            this.searchList.search(null);
        } else if (button == this.buttonList.get(itemBtnId) && !(this.searchList instanceof ListItem))
        {
            this.searchList = new ListItem(this.mc, scrollListWidth, scrollListHeight, posY + 20, posY + 170, posX, 30, this.width, this.height);
            this.searchList.search(null);
        }

        if (button == this.buttonList.get(searchBtnId))
        {
            this.popupWindow.visible = true;
            this.popupWindow.textField.setFocused(true);
            this.popupWindow.textField.setCanLoseFocus(false);
        }

        searchList.actionPerformed(button);
    }

    @Override
    public void initGui()
    {
        this.buttonList.clear();

        int posX = (this.width - scrollListWidth) / 2;
        int posY = (this.height - scrollListHeight) / 2;
        searchList = new ListItem(this.mc, scrollListWidth, scrollListHeight, posY + 20, posY + 170, posX, 30, this.width, this.height);
        searchList.search(null);

        this.popupWindow = new GuiSearchPopup(this.fontRendererObj, (this.width - GuiSearchPopup.width) / 2, (this.height - GuiSearchPopup.height) / 2, searchList);
        this.buttonList.add(new GuiButton(searchBtnId = 0, posX + searchList.listWidth, posY + 20 + 00, 20, 20, ""));
        this.buttonList.add(new GuiButton(pokemonBtnId = 1, posX + searchList.listWidth, posY + 20 + 20, 20, 20, ""));
        this.buttonList.add(new GuiButton(itemBtnId = 2, posX + searchList.listWidth, posY + 20 + 40, 20, 20, ""));
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
