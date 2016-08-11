package ipixelmon.minebay.gui;

import com.pixelmonmod.pixelmon.config.PixelmonItems;
import com.pixelmonmod.pixelmon.entities.pixelmon.Entity3HasStats;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.stats.BaseStats;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;
import ipixelmon.GuiList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class SearchGui extends GuiScreen {

    public static final int ID = 9745;

    private GuiTextField searchField;
    private GuiButton searchBtn;
    private SearchList searchList;
    private List<GuiList.GuiObject> searchListObjects;

    public SearchGui() {
        this.searchListObjects = new ArrayList<>();
    }

    @Override
    public final void drawScreen(final int mouseX, final int mouseY, final float partialTicks) {
        this.drawDefaultBackground();

        this.searchList.draw(mc, mouseX, mouseY);
        this.searchField.drawTextBox();

        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    protected final void keyTyped(final char typedChar, final int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        this.searchField.textboxKeyTyped(typedChar, keyCode);
        this.searchList.keyTyped(typedChar, keyCode);
    }

    @Override
    protected final void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        this.searchField.mouseClicked(mouseX, mouseY, mouseButton);
        this.searchList.mouseClicked(mc, mouseX, mouseY);
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

        this.searchListObjects.add(new SearchListObject(listWidth, 20, new ItemStack(Items.apple), Minecraft.getMinecraft().thePlayer.getUniqueID(), 20, this.itemRender));
        this.searchListObjects.add(new SearchListObject(listWidth, 20, new ItemStack(Items.iron_axe), Minecraft.getMinecraft().thePlayer.getUniqueID(), 80, this.itemRender));
        this.searchListObjects.add(new SearchListObject(listWidth, 20, new ItemStack(Items.gold_ingot), Minecraft.getMinecraft().thePlayer.getUniqueID(), 100, this.itemRender));
        this.searchListObjects.add(new SearchListObject(listWidth, 20, new ItemStack(Items.sign), Minecraft.getMinecraft().thePlayer.getUniqueID(), 25, this.itemRender));
        this.searchListObjects.add(new SearchListObject(listWidth, 20, new ItemStack(Items.magma_cream), Minecraft.getMinecraft().thePlayer.getUniqueID(), 436, this.itemRender));
        this.searchListObjects.add(new SearchListObject(listWidth, 20, new ItemStack(Items.bucket), Minecraft.getMinecraft().thePlayer.getUniqueID(), 45, this.itemRender));
        this.searchListObjects.add(new SearchListObject(listWidth, 20, new ItemStack(Items.boat), Minecraft.getMinecraft().thePlayer.getUniqueID(), 57, this.itemRender));
        this.searchListObjects.add(new SearchListObject(listWidth, 20, new ItemStack(Items.diamond_helmet), Minecraft.getMinecraft().thePlayer.getUniqueID(), 23, this.itemRender));
        this.searchListObjects.add(new SearchListObject(listWidth, 20, new ItemStack(Items.golden_apple), Minecraft.getMinecraft().thePlayer.getUniqueID(), 75, this.itemRender));
        this.searchListObjects.add(new SearchListObject(listWidth, 20, new ItemStack(Items.cooked_chicken), Minecraft.getMinecraft().thePlayer.getUniqueID(), 34, this.itemRender));
        this.searchListObjects.add(new SearchListObject(listWidth, 20, new ItemStack(Items.coal), Minecraft.getMinecraft().thePlayer.getUniqueID(), 12, this.itemRender));

        this.searchListObjects.add(new SearchListObject(listWidth, 20, makePokemonItem(EnumPokemon.Aggron), Minecraft.getMinecraft().thePlayer.getUniqueID(), 12, this.itemRender));
        this.searchListObjects.add(new SearchListObject(listWidth, 20, makePokemonItem(EnumPokemon.Rayquaza), Minecraft.getMinecraft().thePlayer.getUniqueID(), 12, this.itemRender));
        this.searchListObjects.add(new SearchListObject(listWidth, 20, makePokemonItem(EnumPokemon.Aerodactyl), Minecraft.getMinecraft().thePlayer.getUniqueID(), 12, this.itemRender));

        this.searchList = new SearchList((this.width - listWidth) / 2, (this.height - listHeight) / 2, listWidth, listHeight, this.searchListObjects);
    }

    @Override
    public final void updateScreen() {
        super.updateScreen();

        if(this.searchField != null) this.searchField.updateCursorCounter();
    }

    private final ItemStack makePokemonItem(EnumPokemon pokemon) {
        ItemStack stack = new ItemStack(PixelmonItems.itemPixelmonSprite);
        NBTTagCompound tagCompound = new NBTTagCompound();
        Optional stats = Entity3HasStats.getBaseStats(pokemon.name);
        tagCompound.setString("SpriteName", "pixelmon:sprites/pokemon/" + String.format("%03d", new Object[]{Integer.valueOf(((BaseStats)stats.get()).nationalPokedexNumber)}));
        NBTTagCompound display = new NBTTagCompound();
        display.setString("Name", EntityPixelmon.getLocalizedName(pokemon.name) + " " + StatCollector.translateToLocal("item.PixelmonSprite.name"));
        tagCompound.setTag("display", display);
        stack.setTagCompound(tagCompound);
        return stack;
    }

}
