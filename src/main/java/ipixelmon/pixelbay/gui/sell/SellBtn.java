package ipixelmon.pixelbay.gui.sell;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

public final class SellBtn extends GuiButton{

    @Override
    public void drawButton(final Minecraft mc, final int mouseX, final int mouseY) {
        this.hovered = mouseX >= this.xPosition && mouseY >= this.yPosition && mouseX < this.xPosition + this.width && mouseY < this.yPosition + this.height;
        GlStateManager.color(1f, 1f, 1f, 1f);
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        float opacity = this.isMouseOver() && enabled ? 150f/255f : 128f / 255f;

        GlStateManager.color(1f, 1f, 1f, enabled ? opacity - .20f : opacity - .38f);
        this.drawTexturedModalRect(this.xPosition, this.yPosition, 0, 0, this.width, this.height);

        GlStateManager.color(1f, 1f, 1f, enabled ? opacity : opacity - .30f);
        this.drawTexturedModalRect(this.xPosition + 1, this.yPosition + 1, 0, 0, this.width - 2, this.height - 2);

        GlStateManager.enableTexture2D();
        GlStateManager.color(1f, 1f, 1f, 1f);
        int stringX = this.xPosition + (this.width - fontRenderer.getStringWidth(this.displayString)) / 2;
        int stringY = this.yPosition + (this.height - fontRenderer.FONT_HEIGHT) / 2;
        mc.fontRendererObj.drawString(this.displayString, stringX, stringY + 1, 0xFFFFFF);
    }

    public SellBtn(final int buttonId, final int x, final int y, final String buttonText) {
        super(buttonId, x, y, 50, 13, buttonText);
        fontRenderer = Minecraft.getMinecraft().fontRendererObj;
    }

    private final FontRenderer fontRenderer;
}
