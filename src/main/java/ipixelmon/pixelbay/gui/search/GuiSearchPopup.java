package ipixelmon.pixelbay.gui.search;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import ipixelmon.iPixelmon;
import ipixelmon.pixelbay.gui.InputWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;

import java.awt.*;

public class GuiSearchPopup extends InputWindow
{

    private static final ResourceLocation searchIcon = new ResourceLocation(iPixelmon.id + ":pixelbay/textures/gui/SearchIcon.png");

    private ISearchList searchList;

    public GuiSearchPopup(final FontRenderer fontRenderer, final int xPosition, final int yPosition, ISearchList searchList)
    {
        super(fontRenderer, xPosition, yPosition, "Search");
        this.searchList = searchList;
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
    }

    private void drawSearchButton(Minecraft mc)
    {
        if(this.visible)
        {
            return;
        }

        // TODO: Fix placement of searchIcon, should not be where actionBtn is, easy fix.
            mc.getTextureManager().bindTexture(searchIcon);
            GlStateManager.enableBlend();
            int iconWidth = 16, iconHeight = 16;
            GlStateManager.color(0f, 0f, 0f, 128f / 255f);
            GuiHelper.drawImageQuad(xPosition + ((actionBtn.width - iconWidth) / 2), actionBtn.yPosition + ((actionBtn.height - iconHeight) / 2) + 1, iconWidth, iconHeight, 0.0D, 0.0D, 1.0D, 1.0D, 0.0F);

            Color color = new Color(14737632);

            if (actionBtn.isMouseOver()) color = new Color(16777120);

            GlStateManager.color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 1f);
            GuiHelper.drawImageQuad(actionBtn.xPosition + ((actionBtn.width - iconWidth) / 2), actionBtn.yPosition + ((actionBtn.height - iconHeight) / 2), iconWidth, iconHeight, 0.0D, 0.0D, 1.0D, 1.0D, 0.0F);
    }


}
