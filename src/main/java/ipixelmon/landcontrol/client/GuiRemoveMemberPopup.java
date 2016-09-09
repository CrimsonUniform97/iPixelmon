package ipixelmon.landcontrol.client;

import ipixelmon.pixelbay.gui.InputWindow;
import net.minecraft.client.gui.FontRenderer;

public class GuiRemoveMemberPopup extends InputWindow
{

    public GuiRemoveMemberPopup(final FontRenderer fontRenderer, final int xPosition, final int yPosition, final String btnTxt)
    {
        super(fontRenderer, xPosition, yPosition, btnTxt);
        textField.setVisible(false);
        actionBtn.xPosition = xPosition + (width - actionBtn.width) / 2;
        actionBtn.yPosition = yPosition + (height - actionBtn.height) / 2;
    }

    @Override
    public void actionPerformed()
    {
        // TODO: Send packet to remove member
    }
}
