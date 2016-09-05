package ipixelmon.pixelbay.gui.buy;

import ipixelmon.pixelbay.gui.InputWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;

public class GuiPopupBuy extends InputWindow
{

    // TODO: Implement this into ListItem and ListPokemon and GuiSearch
    public GuiPopupBuy(final FontRenderer fontRenderer, final int xPosition, final int yPosition)
    {
        super(fontRenderer, xPosition, yPosition, "Confirm Purchase");
        this.textField.setVisible(false);
        this.actionBtn.width = 100;
        this.actionBtn.xPosition = this.xPosition + (this.width - 100) / 2;
        this.actionBtn.yPosition = this.yPosition + (this.height - 20) / 2;
    }

    @Override
    public void actionPerformed()
    {
        // TODO: Popup done, just need to send packet, check balance, etc.
        this.actionBtn.playPressSound(Minecraft.getMinecraft().getSoundHandler());
        this.visible = false;
    }

}
