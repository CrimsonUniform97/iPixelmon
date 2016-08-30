package ipixelmon.pixelbay.gui.sell;

import com.pixelmonmod.pixelmon.client.ServerStorageDisplay;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.storage.PCClientStorage;
import ipixelmon.PixelmonUtility;
import ipixelmon.guiList.IListObject;
import ipixelmon.iPixelmon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.input.Keyboard;

public final class PokemonSellObject extends IListObject {

    private final PixelmonData pokemon;
    private SellBtn sellBtn;
    private final int sections = 3;
    private GuiTextField priceField;

    public PokemonSellObject(PixelmonData pData) {
        this.pokemon = pData;
    }

    @Override
    public void drawObject(int mouseX, int mouseY, Minecraft mc) {
        if (this.isSelected()) {
            this.priceField.drawTextBox();
            this.sellBtn.drawButton(mc, mouseX, mouseY);
        }

        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GuiHelper.bindPokemonSprite(pokemon, mc);
        GuiHelper.drawImageQuad(0, 0, 26.0D, 26.0F, 0.0D, 0.0D, 1.0D, 1.0D, 0.0F);

        int x = this.getList().getBounds().getWidth() / sections;
        mc.fontRendererObj.drawString("Pokemon: " + this.pokemon.name, getX(30, x * 1 - 100), 2, 0xFFFFFF);
        mc.fontRendererObj.drawString("Level: " + this.pokemon.lvl, getX(30, x * 1 - 100), 11, 0xFFFFFF);
        mc.fontRendererObj.drawString("XP: " + this.pokemon.xp, getX(30, x * 1 - 100), 20, 0xFFFFFF);
    }

    @Override
    public void initGui() {
        int x = this.getList().getBounds().getWidth() / sections;
        this.sellBtn = new SellBtn(0, getX(300, x * 3 - 100), 2, "Sell");
        String priceFieldText = this.priceField != null ? this.priceField.getText() : "$";
        this.priceField = new GuiTextField(0, Minecraft.getMinecraft().fontRendererObj, getX(200, x * 2 - 100), 2, 75, 20);
        this.priceField.setText(priceFieldText);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int btn) {
        this.priceField.mouseClicked(mouseX, mouseY, 0);

        if(this.sellBtn.isMouseOver() && this.sellBtn.enabled) {
            // DON'T REMOVE IF IT IS THE ONLY POKEMON THEY HAVE IN THEIR HANDS

            if(PixelmonUtility.getPokemonCountClient() == 1) {
                Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText("You cannot sell your only pokemon."));
                return;
            }

            iPixelmon.network.sendToServer(new PacketSellPokemon(pokemon, Integer.parseInt(this.priceField.getText().replaceAll("\\$", ""))));
            this.getList().removeObject(this);

            ServerStorageDisplay.changePokemon(pokemon.order, (PixelmonData) null);
            PCClientStorage.refreshStore();
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if(keyCode == Keyboard.KEY_BACK) {
            if(this.priceField.getText().charAt(this.priceField.getCursorPosition() - 1) == '$') {
                return;
            }

            this.priceField.textboxKeyTyped(typedChar, keyCode);
            return;
        }

        if(String.valueOf(typedChar).matches("[0-9]+") || keyCode == Keyboard.KEY_LEFT || keyCode == Keyboard.KEY_RIGHT) {
            if(!this.priceField.getSelectedText().contains("$")) {
                this.priceField.textboxKeyTyped(typedChar, keyCode);
            }
        }
    }

    @Override
    public void update() {
        this.priceField.updateCursorCounter();

        if(this.priceField.getCursorPosition() == 0) {
            this.priceField.setCursorPosition(1);
        }

        if(this.priceField.getSelectionEnd() == 0) {
            this.priceField.setSelectionPos(1);
        }

        this.sellBtn.enabled = !this.priceField.getText().replaceAll("\\$", "").isEmpty();
    }

    @Override
    public int getHeight() {
        return 28;
    }

    private int getX(int min, int x) {
        return x < min ? min : x;
    }
}
