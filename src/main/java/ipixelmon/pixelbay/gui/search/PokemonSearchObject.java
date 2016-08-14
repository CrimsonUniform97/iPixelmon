package ipixelmon.pixelbay.gui.search;

import com.pixelmonmod.pixelmon.client.gui.GuiHelper;
import com.pixelmonmod.pixelmon.comm.PixelmonData;
import ipixelmon.guiList.IListObject;
import ipixelmon.pixelbay.gui.sell.SellBtn;
import ipixelmon.uuidmanager.UUIDManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;

import java.util.UUID;

public final class PokemonSearchObject extends IListObject {

    private final PixelmonData pokemon;
    private SellBtn buyBtn;
    private final long price;
    private final String playerName;

    public PokemonSearchObject(final PixelmonData pixelmonData, final UUID seller, final long price) {
        this.pokemon = pixelmonData;
        this.price = price;
        this.playerName = UUIDManager.getPlayerName(seller);
    }

    @Override
    public void drawObject(int mouseX, int mouseY, Minecraft mc) {
        if (this.isSelected()) this.buyBtn.drawButton(mc, mouseX, mouseY);

        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GuiHelper.bindPokemonSprite(pokemon, mc);
        GuiHelper.drawImageQuad(0, 0, 26.0D, 26.0F, 0.0D, 0.0D, 1.0D, 1.0D, 0.0F);

        mc.fontRendererObj.drawString("Pokemon: " + this.pokemon.name, 30, 1, 0xFFFFFF);
        mc.fontRendererObj.drawString("Level: " + this.pokemon.lvl, 30, 10, 0xFFFFFF);
        mc.fontRendererObj.drawString("XP: " + this.pokemon.xp, 30, 20, 0xFFFFFF);
        mc.fontRendererObj.drawString("Seller: " + playerName, 150, 1, 0xFFFFFF);
        mc.fontRendererObj.drawString("Price: $" + price, 150, 10, 0xFFFFFF);
    }

    @Override
    public void initGui() {
        this.buyBtn = new SellBtn(0, this.getList().getBounds().getWidth() - 200, 5, "Buy");
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int btn) {
        if (this.buyBtn.isMouseOver()) {
            System.out.println("BOOM");
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {

    }

    @Override
    public void update() {

    }

    @Override
    public int getHeight() {
        return 20;
    }

}
