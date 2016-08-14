package ipixelmon.guiList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;

public abstract class IListObject extends Gui {

    protected int page = 0;

    protected IGuiList list;

    public abstract void drawObject(final int mouseX, final int mouseY, final Minecraft mc);

    public abstract void initGui();

    public abstract void mouseClicked(final int mouseX, final int mouseY, final int btn);

    public abstract void keyTyped(final char typedChar, final int keyCode);

    public abstract void update();

    public abstract int getHeight();

    public final IGuiList getList() { return this.list; }

    public final boolean isSelected() {
        return list.getSelected() != null && list.getSelected() == this;
    }

}
