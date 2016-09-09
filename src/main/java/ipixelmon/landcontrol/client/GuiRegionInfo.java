package ipixelmon.landcontrol.client;

import ipixelmon.iPixelmon;
import ipixelmon.landcontrol.PacketAddMember;
import ipixelmon.landcontrol.Region;
import ipixelmon.TimedMessage;
import ipixelmon.uuidmanager.UUIDManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumChatFormatting;

import java.io.IOException;
import java.util.UUID;

public class GuiRegionInfo extends GuiScreen
{

    int bgWidth = 200, bgHeight = 100, posX, posY;
    private GuiMemberScrollList scrollList;
    private GuiTextField textField;
    private Region region;
    private TimedMessage message;
    protected GuiRemoveMemberPopup popup;

    public GuiRegionInfo(Region region)
    {
        this.region = region;
    }
        // TODO: work on the popup window and remove member prompt
    @Override
    public void drawScreen( int mouseX,  int mouseY, final float partialTicks)
    {
        super.drawScreen(mouseX = popup.isVisible() ? 0 : mouseX, mouseY = popup.isVisible() ? 0 : mouseY, partialTicks);
        scrollList.drawScreen(mouseX, mouseY, partialTicks);
        textField.drawTextBox();
        mc.fontRendererObj.drawStringWithShadow("Double click player for more options.", posX, posY - 20, 0xffff00);
        mc.fontRendererObj.drawStringWithShadow("Members:", posX, posY - 10, 0xFFFFFF);
        if(message != null)
        {
            mc.fontRendererObj.drawStringWithShadow(message.getMessage(), posX, textField.yPosition + textField.height + 2, 0xFFFFFF);
        }

        popup.draw(mc, mouseX, mouseY);
    }

    @Override
    protected void actionPerformed(final GuiButton button) throws IOException
    {
        super.actionPerformed(button);
        scrollList.actionPerformed(button);

        if(button.id == 1)
        {
            if(!textField.getText().isEmpty())
            {
                UUID playerUUID = UUIDManager.getUUID(textField.getText());

                if(playerUUID == null)
                {
                    new Thread(message = new TimedMessage(EnumChatFormatting.RED + "Player not found.", 3)).start();
                    return;
                }

                iPixelmon.network.sendToServer(new PacketAddMember(textField.getText(), region));
            }
        }
    }

    @Override
    protected void mouseClicked(final int mouseX, final int mouseY, final int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        textField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(final char typedChar, final int keyCode) throws IOException
    {
        super.keyTyped(typedChar, keyCode);
        textField.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
        textField.updateCursorCounter();
    }

    @Override
    public void initGui()
    {
        buttonList.clear();
        posX = (width - bgWidth) / 2;
        posY = (height - bgHeight) / 2;
        scrollList = new GuiMemberScrollList(mc, bgWidth, bgHeight, posY, posY + bgHeight, posX, 12, width, height, region);
        textField = new GuiTextField(0, mc.fontRendererObj, posX, posY + bgHeight + 2, bgWidth - 50, 20);
        buttonList.add(new GuiButton(1, textField.xPosition + textField.width + 2, textField.yPosition, 50, 20, "Add"));
        popup = new GuiRemoveMemberPopup(mc.fontRendererObj,(width - GuiRemoveMemberPopup.width) / 2,(height - GuiRemoveMemberPopup.height) / 2, "Remove");
    }
}
