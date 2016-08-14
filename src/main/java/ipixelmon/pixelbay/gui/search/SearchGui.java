package ipixelmon.pixelbay.gui.search;

import ipixelmon.iPixelmon;
import ipixelmon.mysql.SelectionForm;
import ipixelmon.pixelbay.Pixelbay;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import org.lwjgl.util.Rectangle;

import java.io.IOException;

public final class SearchGui extends GuiScreen {

    public static final int ID = 9745;

    private SearchList searchList;

    public SearchGui() {
        this.searchList = new SearchList(this);

        new Thread(new SearchListPopulator(iPixelmon.mysql.selectAllFrom(Pixelbay.class, new SelectionForm("Pokemon")),
                iPixelmon.mysql.selectAllFrom(Pixelbay.class, new SelectionForm("Item")), this.searchList)).start();
    }

    @Override
    public final void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.searchList.drawList(mouseX, mouseY, this.mc);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected final void keyTyped(final char typedChar, final int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        this.searchList.keyTyped(typedChar, keyCode);
    }

    @Override
    protected final void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.searchList.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected final void actionPerformed(final GuiButton button) throws IOException {
        super.actionPerformed(button);
    }

    @Override
    public final void initGui() {
        this.buttonList.clear();
        this.searchList.initGui();
    }

    @Override
    public final void updateScreen() {
        this.searchList.update();
    }

}
