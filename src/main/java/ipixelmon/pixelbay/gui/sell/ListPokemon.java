package ipixelmon.pixelbay.gui.sell;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import ipixelmon.pixelbay.gui.search.BasicScrollList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;

import java.util.List;

public class ListPokemon extends BasicScrollList
{

    protected List<PixelmonData> pokemon;

    public ListPokemon(final Minecraft client, final int width, final int height, final int top, final int bottom, final int left, final int entryHeight, final int screenWidth, final int screenHeight, List<PixelmonData> pokemon)
    {
        super(client, width, height, top, bottom, left, entryHeight, screenWidth, screenHeight);
        this.pokemon = pokemon;
    }

    @Override
    protected int getSize()
    {
        return pokemon.size();
    }

    @Override
    protected void elementClicked(final int index, final boolean doubleClick)
    {

    }

    @Override
    protected boolean isSelected(final int index)
    {
        return index == selectedIndex;
    }

    @Override
    protected void drawBackground()
    {

    }

    @Override
    protected void drawSlot(final int slotIdx, final int entryRight, final int slotTop, final int slotBuffer, final Tessellator tess)
    {
        if(slotIdx >= pokemon.size())
        {
            return;
        }


        PixelmonData pData = pokemon.get(slotIdx);

        if(pData == null)
        {
            return;
        }

        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GuiHelper.bindPokemonSprite(pData, this.client);
        GuiHelper.drawImageQuad(this.left, slotTop - 3, 26.0D, 26.0F, 0.0D, 0.0D, 1.0D, 1.0D, 0.0F);

        this.client.fontRendererObj.drawString("Level: " + pData.lvl, left + 26, slotTop + 4, 0xFFFFFF);
        this.client.fontRendererObj.drawString("XP: " + pData.xp, left + 26, slotTop + 15, 0xFFFFFF);
    }
}
