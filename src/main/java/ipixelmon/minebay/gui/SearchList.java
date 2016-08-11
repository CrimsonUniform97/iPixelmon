package ipixelmon.minebay.gui;

import ipixelmon.GuiList;
import net.minecraft.client.gui.GuiButton;

import java.util.List;

public final class SearchList extends GuiList {

    public SearchList(final int screenX, final int screenY, final int width, final int height, final List<GuiObject> objects) {
        super(screenX, screenY, width, height, objects);
    }

    @Override
    public final GuiButton getLeftBtn() {
        return new GuiButton(0, this.getScreenX(), this.getScreenY(), 20, 20, "<");
    }

    @Override
    public final GuiButton getRightBtn() {
        return new GuiButton(0, this.getScreenX() + this.getWidth() - 20, this.getScreenY(), 20, 20, ">");
    }

    @Override
    public final boolean drawSelectionBox() {
        return true;
    }

//    @Override
//    public final void renderSelectionBox() {
//        if (this.getSelectedObject() == null) return;
//
//        if (!this.getCurrentPage().getObjects().contains(this.getSelectedObject())) return;
//
//        glDisable(GL_TEXTURE_2D);
//        glEnable(GL_BLEND);
//        glColor4f(1, 1, 1, 50f / 255f);
//        this.drawTexturedModalRect(this.getScreenX() - 1, this.getScreenY() + this.getSelectedObject().getY() - 1, 0, 0, this.getWidth() - 2, this.getSelectedObject().getHeight());
//        glEnable(GL_TEXTURE_2D);
//    }
}
