package ipixelmon.pixelbay.gui.search;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import ipixelmon.guiList.QueryType;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.GuiScrollingList;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public final class SearchGui extends GuiScreen {

    public static final int ID = 9745;

    private GuiButton searchBtn, pokemonBtn, itemBtn;
    private GuiSearchList searchList;
    protected SearchPopup popupWindow;

    @Override
    public final void drawScreen(int mouseX, int mouseY, final float partialTicks) {

        if(this.popupWindow.visible) {
            mouseX = 0;
            mouseY = 0;
        }

        this.drawDefaultBackground();
        this.searchList.drawList(mouseX, mouseY, this.mc);
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.popupWindow.drawSearchButton(mc, this.searchBtn);
        this.drawItemBtnIcon();
        this.drawPokemonBtnIcon();

        if (this.popupWindow.visible) {
            this.drawDefaultBackground();
            this.popupWindow.draw(mc, mouseX, mouseY);
        }
    }

    @Override
    protected final void keyTyped(final char typedChar, final int keyCode) throws IOException {

        if (this.popupWindow.visible) {

            if (keyCode == Keyboard.KEY_ESCAPE)
                this.popupWindow.visible = false;
            else if (keyCode == Keyboard.KEY_RETURN) {
                this.searchList.search(this.popupWindow.textField.getText(), true, true);
                this.popupWindow.visible = false;
            }
            else
                this.popupWindow.textField.textboxKeyTyped(typedChar, keyCode);
            return;
        }

        this.searchList.keyTyped(typedChar, keyCode);
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected final void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        if (this.popupWindow.visible) {

            if (this.popupWindow.searchBtn.mousePressed(mc, mouseX, mouseY)) {
                this.searchList.search(this.popupWindow.textField.getText(), true, true);
                this.popupWindow.visible = false;
            } else
                this.popupWindow.textField.mouseClicked(mouseX, mouseY, mouseButton);

            return;
        }

        this.searchList.mouseClicked(mouseX, mouseY, mouseButton);
    }

    // TODO: Make the buttons not hover blue while in pop menu, add shifting between pokemon and items
    @Override
    protected final void actionPerformed(final GuiButton button) throws IOException {
        super.actionPerformed(button);

        if (this.popupWindow.visible) {
            return;
        }

        if(button == this.pokemonBtn && !(this.searchList instanceof SearchListPokemon)) {
            this.searchList = new SearchListPokemon(this);
            this.searchList.search(null, true, true);
        } else if (button == this.itemBtn && !(this.searchList instanceof SearchListItem)) {
            this.searchList = new SearchListItem(this);
            this.searchList.search(null, true, true);
            // TODO: While having searched for Ruby, when clicking item it does not reset the list back to default... fix.
            System.out.println("CALLED");
        }

        if (button == this.searchBtn) {
            this.popupWindow.visible = true;
            this.popupWindow.textField.setFocused(true);
            this.popupWindow.textField.setCanLoseFocus(false);
        }
    }

    @Override
    public final void initGui() {
        this.buttonList.clear();

        this.popupWindow = new SearchPopup(this.fontRendererObj, (this.width - SearchPopup.width) / 2, (this.height - SearchPopup.height) / 2);

        if (this.searchList == null) {
            this.searchList = new SearchListItem(this);
            this.searchList.search(null, true, true);
        }

        this.searchList.initGui();
        this.buttonList.add(searchBtn = new GuiButton(0, this.searchList.getBounds().getX() + this.searchList.getBounds().getWidth(), this.searchList.getPageDownBtn().yPosition + this.searchList.getPageDownBtn().height, 20, 20, ""));
        this.buttonList.add(pokemonBtn = new GuiButton(1, this.searchList.getBounds().getX() + this.searchList.getBounds().getWidth(), this.searchList.getPageDownBtn().yPosition + this.searchList.getPageDownBtn().height + 20, 20, 20, ""));
        this.buttonList.add(itemBtn = new GuiButton(2, this.searchList.getBounds().getX() + this.searchList.getBounds().getWidth(), this.searchList.getPageDownBtn().yPosition + this.searchList.getPageDownBtn().height + 40, 20, 20, ""));
    }

    @Override
    public final void updateScreen() {
        this.searchList.update();

        if (this.popupWindow.visible)
            this.popupWindow.textField.updateCursorCounter();
    }

    private void drawPokemonBtnIcon() {
        GlStateManager.enableBlend();
        GlStateManager.color(1, 1, 1, 1);
        PixelmonData pData = new PixelmonData();
        pData.name = EnumPokemon.Pikachu.name;
        GuiHelper.bindPokemonSprite(pData, mc);
        GuiHelper.drawImageQuad(this.pokemonBtn.xPosition + ((this.pokemonBtn.width - 24) / 2), this.pokemonBtn.yPosition + ((this.pokemonBtn.height - 24) / 2) - 3, 24, 24, 0.0D, 0.0D, 1.0D, 1.0D, 0.0F);
    }

    private void drawItemBtnIcon() {
        ItemStack itemStack = new ItemStack(Items.iron_pickaxe);

        GlStateManager.color(1, 1, 1, 1);
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.disableDepth();
        RenderHelper.enableGUIStandardItemLighting();

        if (mc.getRenderItem() != null)
            mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, this.itemBtn.xPosition + ((this.itemBtn.width - 16) / 2), this.itemBtn.yPosition + ((this.itemBtn.height - 16) / 2));

        RenderHelper.disableStandardItemLighting();
    }

}
