package ipixelmon.minebay.gui.sell;

import com.pixelmonmod.pixelmon.Pixelmon;
import com.pixelmonmod.pixelmon.client.ServerStorageDisplay;
import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import com.pixelmonmod.pixelmon.comm.packetHandlers.clientStorage.Add;
import com.pixelmonmod.pixelmon.comm.packetHandlers.pcServer.TrashPokemon;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.storage.PCClientStorage;
import com.pixelmonmod.pixelmon.storage.PCPos;
import ipixelmon.GuiList;
import ipixelmon.PixelmonUtility;
import ipixelmon.iPixelmon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ChatComponentText;
import org.apache.logging.log4j.core.jmx.Server;
import org.lwjgl.input.Keyboard;

public final class PokemonListObject extends GuiList.ListObject {

    private final PixelmonData pokemon;
    private SellBtn sellBtn;
    private GuiTextField priceField;

    public PokemonListObject(final int width, final int height, final PixelmonData pixelmonData) {
        super(width, height);
        this.pokemon = pixelmonData;
        this.sellBtn = new SellBtn(0, this.xPos + 200, this.yPos, "Sell");
        this.priceField = new GuiTextField(0, this.mc.fontRendererObj, this.xPos, this.yPos, 100, 13);
        this.priceField.setText("$");
    }

    // TODO: Add Price Box next to sell Btn
    @Override
    public final void draw(final int x, final int y) {
        this.sellBtn.xPosition = this.xPos + 200;
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

        this.mc.fontRendererObj.drawString("Name: " + this.pokemon.name, this.xPos + 30, this.yPos + 4, 0xFFFFFF);
        this.mc.fontRendererObj.drawString("Level: " + this.pokemon.lvl, this.xPos + 30, this.yPos + 14, 0xFFFFFF);
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

            this.deletePokemon(-1, pokemon.order);
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

    public int getPos(PixelmonData pixelmon) {
        for(int i = 0; i < 6; i++) {
            if(ServerStorageDisplay.pokemon[i] == pixelmon) {
                return i;
            }
        }

        return -1;
    }

    public void storePokemonAtPos(PixelmonData pkt, int box, int pos) {
        if(pkt != null) {
            pkt.order = pos;
            if(box != -1) {
                pkt.boxNumber = box;
            }
        }

        if(box == -1) {
            ServerStorageDisplay.changePokemon(pos, pkt);
        } else {
            PCClientStorage.changePokemon(box, pos, pkt);
        }

    }

    public void deletePokemon(int box, int pos) {
        this.storePokemonAtPos((PixelmonData)null, box, pos);
    }
}
