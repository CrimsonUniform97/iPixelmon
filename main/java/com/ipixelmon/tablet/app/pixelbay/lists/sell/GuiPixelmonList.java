package com.ipixelmon.tablet.app.pixelbay.lists.sell;

import com.ipixelmon.tablet.app.pixelbay.gui.sell.SellGuiPixelmon;
import com.ipixelmon.tablet.app.pixelbay.lists.IScrollListWithDesign;
import com.ipixelmon.util.PixelmonAPI;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import net.minecraft.client.Minecraft;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * Created by colby on 1/1/2017.
 */
public class GuiPixelmonList extends IScrollListWithDesign {

    private List<PixelmonData> pixelmon;

    public GuiPixelmonList(int xPosition, int yPosition, int width, int height) {
        super(xPosition, yPosition, width, height);
        pixelmon = PixelmonAPI.Client.getPixelmon(true);
        drawScrollbarOnTop = false;
    }

    @Override
    public int getObjectHeight(int index) {
        return 30;
    }

    @Override
    public void drawObject(int index, int mouseX, int mouseY, boolean isHovering) {
        int x = 2;
        int y = 2;
        int width = 24;
        int height = 24;

        GL11.glEnable(GL11.GL_SCISSOR_TEST);

        PixelmonAPI.Client.renderPixelmon2D(pixelmon.get(index), x - 5, y - 12, width + 16, height + 16);

        if (mouseX >= x && mouseX <= x + width + 5 && mouseY >= y && mouseY <= y + height) {
            PixelmonAPI.Client.renderPixelmonTip(pixelmon.get(index), mouseX, mouseY, this.width, this.height);
        }
    }

    @Override
    public int getSize() {
        return pixelmon.size();
    }

    @Override
    public void elementClicked(int index, int mouseX, int mouseY, boolean doubleClick) {
        if(doubleClick) {
            Minecraft.getMinecraft().displayGuiScreen(new SellGuiPixelmon(new Object[]{pixelmon.get(index)}));
        }
    }

}
