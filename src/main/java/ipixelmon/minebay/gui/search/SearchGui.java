package ipixelmon.minebay.gui.search;

import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity3HasStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStats;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import ipixelmon.GuiList;
import ipixelmon.iPixelmon;
import ipixelmon.minebay.Minebay;
import ipixelmon.mysql.SelectionForm;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class SearchGui extends GuiScreen {

    public static final int ID = 9745;

    private GuiTextField searchField;
    private SearchList searchList;
    private List<GuiList.ListObject> searchListObjects;
    private final ResultSet resultPokemon, resultItem;
    private final SearchListPopulator populator;

    public SearchGui() {
        this.searchListObjects = new ArrayList<>();

        resultPokemon = iPixelmon.mysql.selectAllFrom(Minebay.class, new SelectionForm("Pokemon"));
        resultItem = iPixelmon.mysql.selectAllFrom(Minebay.class, new SelectionForm("Item"));

        new Thread(populator = new SearchListPopulator(resultPokemon, resultItem, searchListObjects)).start();
    }

    @Override
    public final void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();

        if(populator.done) this.searchList.draw(mc, mouseX, mouseY);

        this.searchField.drawTextBox();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected final void keyTyped(final char typedChar, final int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        this.searchField.textboxKeyTyped(typedChar, keyCode);

        if(populator.done) this.searchList.keyTyped(typedChar, keyCode);
    }

    @Override
    protected final void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        this.searchField.mouseClicked(mouseX, mouseY, mouseButton);

        if(populator.done) this.searchList.mouseClicked(mc, mouseX, mouseY);
    }

    @Override
    protected final void actionPerformed(final GuiButton button) throws IOException {
        super.actionPerformed(button);


    }

    @Override
    public final void initGui() {
        this.buttonList.clear();

        final int fieldWidth = 75, fieldHeight = 20;
        this.searchField = new GuiTextField(0, this.fontRendererObj, (this.width - fieldWidth) / 2, (this.height - fieldHeight) / 2, fieldWidth, fieldHeight);

        final int listWidth = this.width - 50, listHeight = this.height - 50;

        this.searchList = new SearchList((this.width - listWidth) / 2, (this.height - listHeight) / 2, listWidth, listHeight, this.searchListObjects);
    }

    @Override
    public final void updateScreen() {
        super.updateScreen();
        System.out.println(searchListObjects.size());
        if(this.searchField != null) this.searchField.updateCursorCounter();
    }

}
