package ipixelmon.pixelbay.gui.buy;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import ipixelmon.iPixelmon;
import ipixelmon.pixelbay.gui.InputWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public class GuiPopupSearch extends InputWindow
{

    private static final ResourceLocation searchIcon = new ResourceLocation(iPixelmon.id + ":pixelbay/textures/gui/SearchIcon.png");

    private ISearchList searchList;
    private GuiButton searchBtn;

    public GuiPopupSearch(final FontRenderer fontRenderer, final int xPosition, final int yPosition, ISearchList searchList, GuiButton searchBtn)
    {
        super(fontRenderer, xPosition, yPosition, "Search");
        this.searchList = searchList;
        this.searchBtn = searchBtn;
    }

    @Override
    public void draw(final Minecraft mc, final int mouseX, final int mouseY)
    {
        super.draw(mc, mouseX, mouseY);
        drawSearchButton(mc);
    }

    @Override
    public void actionPerformed()
    {
        searchList.search(textField.getText().trim());
        this.actionBtn.playPressSound(Minecraft.getMinecraft().getSoundHandler());
        this.visible = false;
    }

    private void drawSearchButton(Minecraft mc)
    {
        // TODO: Fix placement of searchIcon, should not be where actionBtn is, easy fix.
        mc.getTextureManager().bindTexture(searchIcon);
        GlStateManager.enableBlend();
        int iconWidth = 16, iconHeight = 16;
        GlStateManager.color(0f, 0f, 0f, 128f / 255f);
        GuiHelper.drawImageQuad(searchBtn.xPosition + ((searchBtn.width - iconWidth) / 2), searchBtn.yPosition + ((searchBtn.height - iconHeight) / 2) + 1, iconWidth, iconHeight, 0.0D, 0.0D, 1.0D, 1.0D, 0.0F);

        Color color = new Color(14737632);

        if (actionBtn.isMouseOver()) color = new Color(16777120);

        GlStateManager.color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 1f);
        GuiHelper.drawImageQuad(searchBtn.xPosition + ((searchBtn.width - iconWidth) / 2), searchBtn.yPosition + ((searchBtn.height - iconHeight) / 2), iconWidth, iconHeight, 0.0D, 0.0D, 1.0D, 1.0D, 0.0F);
    }


}
