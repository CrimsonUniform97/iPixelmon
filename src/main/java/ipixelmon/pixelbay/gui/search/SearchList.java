package ipixelmon.pixelbay.gui.search;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import ipixelmon.guiList.IGuiList;
import ipixelmon.iPixelmon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.util.Rectangle;

public final class SearchList extends IGuiList{

    private int maxPages;

    public SearchList(GuiScreen screen) {
        super(screen);
    }

    @Override
    public void drawList(int mouseX, int mouseY, Minecraft mc) {
        mc.getTextureManager().bindTexture(new ResourceLocation(iPixelmon.id + ":textures/gui/PixelbayLogo.png"));
        int logoWidth = 398 / 2;
        int logoHeight = 108 / 2;
        GuiHelper.drawImageQuad(this.getBounds().getX() + ((this.getBounds().getWidth() - logoWidth) / 2), this.getBounds().getY() - logoHeight, logoWidth, logoHeight, 0.0D, 0.0D, 1.0D, 1.0D, 0.0F);

        String pageString = "Page (" + this.getPage() + "/" + this.maxPages + ")";
        int pageStringWidth = mc.fontRendererObj.getStringWidth(pageString);
        // Draw pages and current page
        mc.fontRendererObj.drawString(pageString, this.getBounds().getX() + (this.getBounds().getWidth() - pageStringWidth), this.getBounds().getY() - 10, 0xFFFFFF);

        super.drawList(mouseX, mouseY, mc);
    }

    @Override
    public void initGui() {
        super.initGui();
        maxPages = this.getMaxPages();
    }

    @Override
    public Rectangle getBounds() {
        int listWidth = this.getParentScreen().width - 50, listHeight = this.getParentScreen().height - 50;
        return new Rectangle((this.getParentScreen().width - listWidth) / 2, ((this.getParentScreen().height - listHeight) / 2) + 40, listWidth, listHeight - 40);
    }
}
