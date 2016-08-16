package ipixelmon.pixelbay.gui.sell;

import ipixelmon.guiList.IGuiList;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.util.Rectangle;

public final class SellList extends IGuiList {

    public SellList(GuiScreen parentScreen) {
        super(parentScreen);
    }

    @Override
    public Rectangle getBounds() {
        int listWidth = this.getParentScreen().width - 50, listHeight = this.getParentScreen().height - 50;
        return new Rectangle((this.getParentScreen().width - listWidth) / 2, ((this.getParentScreen().height - listHeight) / 2) + 40, listWidth, listHeight - 40);
    }
}
