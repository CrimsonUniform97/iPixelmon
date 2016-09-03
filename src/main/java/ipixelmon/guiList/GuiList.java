package ipixelmon.guiList;

import net.minecraft.client.gui.GuiTextField;
import net.minecraftforge.fml.client.GuiScrollingList;

public abstract class GuiList {

    private int x, y, width, height;
    private GuiTextField textField;

    public GuiList(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public void drawList(int mouseX, int mouseY) {

    }

    public abstract void drawEntry(int x, int y);

}
