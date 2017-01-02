package com.ipixelmon.tablet.app.pixelbay.lists;

import com.ipixelmon.tablet.app.pixelbay.gui.SellGuiPixelmon;
import com.ipixelmon.util.GuiUtil;
import com.ipixelmon.util.PixelmonAPI;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import net.minecraft.client.Minecraft;
import net.minecraft.util.EnumChatFormatting;
import java.util.ArrayList;
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

        PixelmonAPI.Client.drawPixelmon(pixelmon.get(index), x - 5, y - 12, width + 16, height + 16);

        if (mouseX >= x && mouseX <= x + width + 5 && mouseY >= y && mouseY <= y + height) {
            List<String> pixelmonInfo = new ArrayList<>();
            PixelmonData pixelmonData = pixelmon.get(index);
            pixelmonInfo.add(pixelmonData.name);
            pixelmonInfo.add("");
            pixelmonInfo.add(EnumChatFormatting.YELLOW + "Lvl: " + pixelmonData.lvl);
            pixelmonInfo.add(EnumChatFormatting.LIGHT_PURPLE + "XP: " + pixelmonData.xp + "/" + pixelmonData.nextLvlXP);
            pixelmonInfo.add(EnumChatFormatting.RED + "HP: " + pixelmonData.health + "/" + pixelmonData.HP);
            pixelmonInfo.add(EnumChatFormatting.BLUE + "BP: " + PixelmonAPI.getBP(pixelmonData));
            GuiUtil.drawHoveringText(pixelmonInfo, mouseX, mouseY, this.width, this.height);
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
