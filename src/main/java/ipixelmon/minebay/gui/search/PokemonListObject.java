package ipixelmon.minebay.gui.search;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import ipixelmon.GuiList;
import ipixelmon.minebay.gui.sell.SellBtn;
import ipixelmon.uuidmanager.UUIDManager;
import net.minecraft.client.renderer.GlStateManager;

import java.util.UUID;

public final class PokemonListObject extends GuiList.ListObject {

    private final PixelmonData pokemon;
    private final SellBtn buyBtn;
    private final long price;
    private final String playerName;

    public PokemonListObject(final int width, final int height, final PixelmonData pixelmonData, final UUID seller, final long price) {
        super(width, height);
        this.pokemon = pixelmonData;
        this.buyBtn = new SellBtn(0, this.xPos + 300, this.yPos, "Buy");
        this.price = price;
        this.playerName = UUIDManager.getPlayerName(seller);
    }

    // TODO: Add Price Box next to sell Btn
    @Override
    public final void draw(final int x, final int y) {
        this.buyBtn.xPosition = this.xPos + 300;
        this.buyBtn.yPosition = this.yPos + ((this.height - this.buyBtn.height) / 2);
        if(this.isSelected) this.buyBtn.drawButton(this.mc, x, y);

        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GuiHelper.bindPokemonSprite(pokemon, this.mc);
        GuiHelper.drawImageQuad(this.xPos, this.yPos, 26.0D, 26.0F, 0.0D, 0.0D, 1.0D, 1.0D, 0.0F);

        this.mc.fontRendererObj.drawString("Pokemon: " + this.pokemon.name, this.xPos + 30, this.yPos + 1, 0xFFFFFF);
        this.mc.fontRendererObj.drawString("Level: " + this.pokemon.lvl, this.xPos + 30, this.yPos + 10, 0xFFFFFF);
        this.mc.fontRendererObj.drawString("XP: " + this.pokemon.xp, this.xPos + 30, this.yPos + 20, 0xFFFFFF);
        this.mc.fontRendererObj.drawString("Seller: " + playerName, this.xPos + 150, this.yPos + 1, 0xFFFFFF);
        this.mc.fontRendererObj.drawString("Price: $" + price, this.xPos + 150, this.yPos + 10, 0xFFFFFF);
    }

    @Override
    public void mouseClicked(final int mouseX, final int mouseY) {
        if(this.buyBtn.isMouseOver() && this.buyBtn.enabled) {
            // TODO: Buy the pokemon, send the packet
        }
    }

}
