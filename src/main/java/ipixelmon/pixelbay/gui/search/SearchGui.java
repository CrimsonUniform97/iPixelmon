package ipixelmon.pixelbay.gui.search;

import com.google.common.base.Splitter;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import ipixelmon.ItemSerializer;
import ipixelmon.guiList.IListObject;
import ipixelmon.iPixelmon;
import ipixelmon.mysql.SelectionForm;
import ipixelmon.pixelbay.Pixelbay;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.lwjgl.util.Rectangle;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public final class SearchGui extends GuiScreen {

    public static final int ID = 9745;

    private SearchList searchList;
    private GuiTextField searchField;
    private GuiButton searchBtn;

    @Override
    public final void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();
        this.searchList.drawList(mouseX, mouseY, this.mc);
        this.searchField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected final void keyTyped(final char typedChar, final int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        this.searchList.keyTyped(typedChar, keyCode);
        this.searchField.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    protected final void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        // TODO: Draw page as new increment after changing pages. So go to like page 32 and not just 16
        if (this.searchList.getPage() == this.searchList.getMaxPages() && this.searchList.getPageUpBtn().mousePressed(this.mc, mouseX, mouseY)) {
            int prevPokeIndex = this.searchList.getPokeIndex();
            int prevItemIndex = this.searchList.getItemIndex();

            this.searchList.setPokeIndex(this.searchList.getPokeIndex() + this.searchList.getPokeEntries());
            this.searchList.setItemIndex((this.searchList.getItemIndex() + this.searchList.getItemEntries()));
            this.searchList.search(this.searchField.getText());

            // Set the index back to the previous since this page returned no results. Player has reached the end.
            if (this.searchList.getPokeEntries() == 0 && this.searchList.getItemEntries() == 0) {
                this.searchList.setPokeIndex(prevPokeIndex);
                this.searchList.setItemIndex(prevItemIndex);
                this.searchList.search(this.searchField.getText());
            } else {
                this.searchList.setPage(0);
            }

            this.searchList.getPageUpBtn().playPressSound(this.mc.getSoundHandler());
            return;
        }

        if (this.searchList.getPage() == 0 && this.searchList.getPageDownBtn().mousePressed(this.mc, mouseX, mouseY)) {
            // Don't do anything if on the first page.
            if (this.searchList.getItemIndex() == 0 && this.searchList.getPokeIndex() == 0) return;

            int newPokeIndex = this.searchList.getPokeIndex() - this.searchList.getSearchLimit();
            if (newPokeIndex < 0) newPokeIndex = 0;

            int newItemIndex = this.searchList.getItemIndex() - this.searchList.getSearchLimit();
            if (newItemIndex < 0) newItemIndex = 0;

            this.searchList.setPokeIndex(newPokeIndex);
            this.searchList.setItemIndex(newItemIndex);
            this.searchList.search(this.searchField.getText());
            this.searchList.getPageDownBtn().playPressSound(this.mc.getSoundHandler());
            return;
        }

        this.searchList.mouseClicked(mouseX, mouseY, mouseButton);
        this.searchField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected final void actionPerformed(final GuiButton button) throws IOException {
        super.actionPerformed(button);
        this.searchList.search(this.searchField.getText());
        this.searchList.setPage(0);
    }

    @Override
    public final void initGui() {
        this.buttonList.clear();

        if(this.searchList == null) {
            this.searchList = new SearchList(this);
            this.searchList.search(null);
            this.searchList.setPage(0);
        }

        this.searchList.initGui();
        this.searchField = new GuiTextField(0, this.fontRendererObj, 120, 50, 50, 20);
        this.buttonList.add(searchBtn = new GuiButton(0, 50, 50, 50, 20, "Search"));
    }

    @Override
    public final void updateScreen() {
        this.searchField.updateCursorCounter();
        this.searchList.update();
    }

}
