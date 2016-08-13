package ipixelmon.minebay.gui.sell;

import ipixelmon.GuiList;
import net.minecraft.client.gui.GuiButton;

import java.util.List;

public final class SellList extends GuiList {

    public SellList(final int screenX, final int screenY, final int width, final int height, final List<ListObject> objects) {
        super(screenX, screenY, width, height, objects);
    }

    @Override
    public final GuiButton getLeftBtn() {
        return new GuiButton(0, this.getScreenX() + this.getWidth(), this.getScreenY() + 22, 20, 20, "<");
    }

    @Override
    public final GuiButton getRightBtn() {
        return new GuiButton(1, this.getScreenX() + this.getWidth(), this.getScreenY(), 20, 20, ">");
    }

    @Override
    public final boolean drawSelectionBox() {
        return true;
    }
}
