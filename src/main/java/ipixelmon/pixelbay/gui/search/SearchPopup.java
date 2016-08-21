package ipixelmon.pixelbay.gui.search;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import ipixelmon.iPixelmon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.*;

public final class SearchPopup extends Gui {

    private static final ResourceLocation searchIcon = new ResourceLocation(iPixelmon.id + ":pixelbay/textures/gui/SearchIcon.png");
    private static final ResourceLocation bgTexture = new ResourceLocation(iPixelmon.id + ":pixelbay/textures/gui/SearchPopup.png");
    public static final int width = 176, height = 88;

    private final int xPosition, yPosition;
    protected GuiTextField textField;
    protected GuiButton searchBtn;
    protected boolean visible;

    public SearchPopup(FontRenderer fontRenderer, int xPosition, int yPosition) {
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        int section = this.height / 2;
        this.textField = new GuiTextField(0, fontRenderer, this.xPosition + ((this.width - (this.width - 50)) / 2), this.yPosition + ((section * 0) + ((section - 20) / 2)), this.width - 50, 20);
        this.searchBtn = new GuiButton(1, this.xPosition + ((this.width - (this.width - 100)) /2), this.yPosition + ((section * 1) + ((section - 20) / 2)), this.width - 100, 20, "Search");
        this.visible = false;
    }


    public void draw(Minecraft mc, int mouseX, int mouseY) {
        GlStateManager.color(1, 1, 1, 1);
        mc.getTextureManager().bindTexture(bgTexture);
        drawTexturedModalRect(xPosition, yPosition, 0, 0, width, height);
        this.textField.drawTextBox();
        this.searchBtn.drawButton(mc, mouseX, mouseY);
    }

    public void drawSearchButton(Minecraft mc, GuiButton searchBtn) {
        mc.getTextureManager().bindTexture(searchIcon);
        GlStateManager.enableBlend();
        int iconWidth = 16, iconHeight = 16;
        GlStateManager.color(0f, 0f, 0f, 128f / 255f);
        GuiHelper.drawImageQuad(xPosition + ((searchBtn.width - iconWidth) / 2), searchBtn.yPosition + ((searchBtn.height - iconHeight) / 2) + 1, iconWidth, iconHeight, 0.0D, 0.0D, 1.0D, 1.0D, 0.0F);

        Color color = new Color(14737632);

        if (searchBtn.isMouseOver()) color = new Color(16777120);

        GlStateManager.color(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, 1f);
        GuiHelper.drawImageQuad(searchBtn.xPosition + ((searchBtn.width - iconWidth) / 2), searchBtn.yPosition + ((searchBtn.height - iconHeight) / 2), iconWidth, iconHeight, 0.0D, 0.0D, 1.0D, 1.0D, 0.0F);
    }

}
