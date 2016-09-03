package ipixelmon.pixelbay.gui.search;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import ipixelmon.guiList.QueryType;
import ipixelmon.iPixelmon;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public abstract class BasicSearchList extends SearchList {

    public BasicSearchList(final SearchGui parentScreen) {
        super(parentScreen);
    }

    private static final ResourceLocation logo = new ResourceLocation(iPixelmon.id + ":pixelbay/textures/gui/PixelbayLogo.png");

    @Override
    public void drawList(final int mouseX, final int mouseY, final Minecraft mc) {
        mc.getTextureManager().bindTexture(logo);
        int logoWidth = 398 / 2;
        int logoHeight = 108 / 2;
        GuiHelper.drawImageQuad(this.getBounds().getX() + ((this.getBounds().getWidth() - logoWidth) / 2), this.getBounds().getY() - logoHeight, logoWidth, logoHeight, 0.0D, 0.0D, 1.0D, 1.0D, 0.0F);

        super.drawList(mouseX, mouseY, mc);
    }
    @Override
    public void pageUp() {
        if (this.getPage() == this.getLastPage())
            this.search(this.getParentScreen().popupWindow.textField.getText(), QueryType.UP);
        super.pageUp();
    }

    @Override
    public void pageDown() {
        if (this.getPage() == 0)
            this.search(this.getParentScreen().popupWindow.textField.getText(), QueryType.DOWN);
        super.pageDown();
    }

}
