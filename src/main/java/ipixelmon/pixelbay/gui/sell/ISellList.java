package ipixelmon.pixelbay.gui.sell;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import ipixelmon.iPixelmon;
import ipixelmon.pixelbay.gui.BasicScrollList;
import ipixelmon.pixelbay.gui.ColorPicker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.util.Rectangle;

import java.awt.*;

public abstract class ISellList extends BasicScrollList
{

    private static final ResourceLocation logo = new ResourceLocation(iPixelmon.id + ":pixelbay/textures/gui/PixelbayLogo.png");

    public ISellList(final Minecraft client, final int width, final int height, final int top, final int bottom, final int left, final int entryHeight, final int screenWidth, final int screenHeight)
    {
        super(client, width, height, top, bottom, left, entryHeight, screenWidth, screenHeight);
    }

    @Override
    protected void drawBackground()
    {
        GlStateManager.color(1, 1, 1, 1);
        this.drawRect(new Rectangle(left, top, listWidth, listHeight), ColorPicker.color(16, 0, 16, 250), ColorPicker.color(29, 0, 102, 250));

        this.client.getTextureManager().bindTexture(logo);
        int logoWidth = 398 / 2;
        int logoHeight = 108 / 2;
        GuiHelper.drawImageQuad(this.left + ((this.listWidth - logoWidth) / 2), this.top - logoHeight, logoWidth, logoHeight, 0.0D, 0.0D, 1.0D, 1.0D, 0.0F);
    }

    @Override
    protected boolean isSelected(int index)
    {
        return index == this.selectedIndex;
    }

    @Override
    public void drawSelectionBox(final int slotLeft, final int slotTop, final int slotRight, final int slotBuffer)
    {
        this.drawRect(new Rectangle(slotLeft, slotTop, listWidth - 6, slotBuffer), ColorPicker.color(70, 0, 80, 250), ColorPicker.color(90, 0, 100, 250));
    }

    @Override
    public void drawScrollBar(final int scrollBarLeft, final int scrollBarRight, final int thumbTop, final int thumbHeight)
    {
        this.drawRect(new Rectangle(scrollBarLeft, this.top, scrollBarRight - scrollBarLeft, this.bottom - this.top), ColorPicker.color(16, 0, 16, 250), ColorPicker.color(29, 0, 102, 250));
        this.drawRect(new Rectangle(scrollBarLeft, thumbTop, scrollBarRight - scrollBarLeft, thumbHeight),  ColorPicker.color(70, 0, 80, 250), ColorPicker.color(90, 0, 100, 250));
    }

    public void drawRect(Rectangle rect, Color bgColor, Color trimColor)
    {
        int x = rect.getX(), y = rect.getY(), w = rect.getWidth(), h = rect.getHeight();
        x += 4;
        y += 4;
        w -= 8;
        h -= 8;
        int l = bgColor.getRGB();
        this.drawGradientRect(x - 3, y - 4, x + w + 3, y - 3, l, l);
        this.drawGradientRect(x - 3, y + h + 3, x + w + 3, y + h + 4, l, l);
        this.drawGradientRect(x - 3, y - 3, x + w + 3, y + h + 3, l, l);
        this.drawGradientRect(x - 4, y - 3, x - 3, y + h + 3, l, l);
        this.drawGradientRect(x + w + 3, y - 3, x + w + 4, y + h + 3, l, l);
        int i1 = trimColor.getRGB();
        int j1 = (i1 & 16711422 /* white */) >> 1 | i1 & -16777216 /* black */;
        this.drawGradientRect(x - 3, y - 3 + 1, x - 3 + 1, y + h + 3 - 1, i1, j1);
        this.drawGradientRect(x + w + 2, y - 3 + 1, x + w + 3, y + h + 3 - 1, i1, j1);
        this.drawGradientRect(x - 3, y - 3, x + w + 3, y - 3 + 1, i1, i1);
        this.drawGradientRect(x - 3, y + h + 2, x + w + 3, y + h + 3, j1, j1);
    }


}
