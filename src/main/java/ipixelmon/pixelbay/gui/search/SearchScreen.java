package ipixelmon.pixelbay.gui.search;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraftforge.fml.client.GuiScrollingList;

import java.io.IOException;

public class SearchScreen extends GuiScreen {

    private GuiScrollingList scrollList;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        GlStateManager.color(1, 1, 1, 1);

        scrollList.drawScreen(mouseX, mouseY, partialTicks);

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        scrollList.actionPerformed(button);
    }

    @Override
    public void initGui() {
        int posX = (this.width - 100) / 2;
        int posY = (this.height - 150) / 2;
        scrollList = new PokemonList(this.mc, 100, 150, posY, posY + 150, posX, 20, this.width, this.height);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
    }
}
