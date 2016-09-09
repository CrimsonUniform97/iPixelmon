package ipixelmon.landcontrol;

import ipixelmon.pixelbay.gui.ColorPicker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import org.lwjgl.util.Rectangle;

import java.awt.*;

public class GuiPopupWindow extends Gui
{

    private Rectangle bounds;
    private Minecraft client;

    // TODO: Maybe make this more functional
    public GuiPopupWindow(int left, int top, int right, int bottom)
    {
        bounds = new Rectangle(left, top, right - left, bottom - top);
        client = Minecraft.getMinecraft();
    }

    public void drawPopup()
    {
        drawRect(bounds, ColorPicker.color(217f, 217f, 217f, 255f), ColorPicker.color(191f, 191f, 191f, 255f));

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
