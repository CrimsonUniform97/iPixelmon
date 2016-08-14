package ipixelmon.minebay.gui.sell;

import com.pixelmonmod.pixelmon.client.ServerStorageDisplay;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.storage.PCClientStorage;
import ipixelmon.GuiList;
import ipixelmon.PixelmonUtility;
import ipixelmon.iPixelmon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ChatComponentText;
import org.lwjgl.input.Keyboard;

public final class PokemonListObject extends GuiList.ListObject {

    private final PixelmonData pokemon;
    private final SellBtn sellBtn;
    private final GuiTextField priceField;

    public PokemonListObject(final int width, final int height, final PixelmonData pixelmonData) {
        super(width, height);
        this.pokemon = pixelmonData;
        this.sellBtn = new SellBtn(0, this.xPos + 300, this.yPos, "Sell");
        this.priceField = new GuiTextField(0, this.mc.fontRendererObj, this.xPos, this.yPos, 100, 13);
        this.priceField.setText("$");
    }

    @Override
    public final void draw(final int x, final int y) {
        this.sellBtn.xPosition = this.xPos + 300;
        this.sellBtn.yPosition = this.yPos + ((this.height - this.sellBtn.height) / 2);
        this.priceField.xPosition =  this.sellBtn.xPosition + this.sellBtn.width + 10;
        this.priceField.yPosition = this.yPos + ((this.height - this.priceField.height) / 2);
        if(this.isSelected) {
            this.sellBtn.drawButton(this.mc, x, y);
            this.priceField.drawTextBox();
        }
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GuiHelper.bindPokemonSprite(pokemon, this.mc);
        GuiHelper.drawImageQuad(this.xPos, this.yPos, 26.0D, 26.0F, 0.0D, 0.0D, 1.0D, 1.0D, 0.0F);

        this.mc.fontRendererObj.drawString("Pokemon: " + this.pokemon.name, this.xPos + 30, this.yPos + 1, 0xFFFFFF);
        this.mc.fontRendererObj.drawString("Level: " + this.pokemon.lvl, this.xPos + 30, this.yPos + 10, 0xFFFFFF);
        this.mc.fontRendererObj.drawString("XP: " + this.pokemon.xp, this.xPos + 30, this.yPos + 20, 0xFFFFFF);
    }

    @Override
    public void mouseClicked(final int mouseX, final int mouseY) {
        this.priceField.mouseClicked(mouseX, mouseY, 0);

        if(this.sellBtn.isMouseOver() && this.sellBtn.enabled) {
            // DON'T REMOVE IF IT IS THE ONLY POKEMON THEY HAVE IN THEIR HANDS

            if(PixelmonUtility.getPokemonCountClient() == 1) {
                Minecraft.getMinecraft().thePlayer.addChatComponentMessage(new ChatComponentText("You cannot sell your only pokemon."));
                return;
            }

            iPixelmon.network.sendToServer(new PacketSellPokemon(pokemon, Integer.parseInt(this.priceField.getText().replaceAll("\\$", ""))));

            ServerStorageDisplay.changePokemon(pokemon.order, (PixelmonData) null);
            PCClientStorage.refreshStore();
        }
    }

    @Override
    public void keyTyped(final char typedChar, final int keyCode) {
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
    public void updateScreen() {
        this.priceField.updateCursorCounter();

        if(this.priceField.getCursorPosition() == 0) {
            this.priceField.setCursorPosition(1);
        }

        if(this.priceField.getSelectionEnd() == 0) {
            this.priceField.setSelectionPos(1);
        }

        this.sellBtn.enabled = !this.priceField.getText().replaceAll("\\$", "").isEmpty();
    }

}
