package com.ipixelmon.landcontrol.client;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.PacketEditMemberRequest;
import com.ipixelmon.landcontrol.PacketEditMemberResponse;
import com.ipixelmon.landcontrol.Region;
import com.ipixelmon.TimedMessage;
import com.ipixelmon.uuidmanager.UUIDManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumChatFormatting;

import java.io.IOException;
import java.util.UUID;

public class GuiRegionInfo extends GuiScreen
{

    int bgWidth = 200, bgHeight = 100, posX, posY;
    public GuiMemberScrollList scrollList;
    public GuiTextField textField;
    private Region region;
    public TimedMessage message;

    public GuiRegionInfo(Region region)
    {
        this.region = region;
    }

    @Override
    public void drawScreen(int mouseX,  int mouseY, final float partialTicks)
    {
        super.drawScreen(mouseX, mouseY, partialTicks);
        scrollList.drawScreen(mouseX, mouseY, partialTicks);
        textField.drawTextBox();
        mc.fontRendererObj.drawStringWithShadow("Double click player for more options.", posX, posY - 20, 0xffff00);
        mc.fontRendererObj.drawStringWithShadow("Members:", posX, posY - 10, 0xFFFFFF);
        if(message != null)
        {
            mc.fontRendererObj.drawStringWithShadow(message.getMessage(), posX, textField.yPosition + textField.height + 2, 0xFFFFFF);
        }
    }

    @Override
    public void confirmClicked(final boolean result, final int id)
    {
        if (result)
        {
            iPixelmon.network.sendToServer(new PacketEditMemberRequest(scrollList.playerNames.get(scrollList.getSelectedIndex()), region, false));
        }

        Minecraft.getMinecraft().displayGuiScreen(this);
        super.confirmClicked(result, id);
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
                for(String s : scrollList.playerNames)
                {
                    if(s.equalsIgnoreCase(textField.getText()))
                    {
                        message.setMessage(EnumChatFormatting.RED + "Player is already a member.", 3);
                        return;
                    }
                }

                UUID playerUUID = UUIDManager.getUUID(textField.getText());

                if(playerUUID == null)
                {
                    message.setMessage(EnumChatFormatting.RED + "Player not found.", 3);
                    return;
                }

                iPixelmon.network.sendToServer(new PacketEditMemberRequest(textField.getText(), region, true));
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
        scrollList = new GuiMemberScrollList(mc, bgWidth, bgHeight, posY, posY + bgHeight, posX, 12, width, height, region, this);
        textField = new GuiTextField(0, mc.fontRendererObj, posX, posY + bgHeight + 2, bgWidth - 50, 20);
        buttonList.add(new GuiButton(1, textField.xPosition + textField.width + 2, textField.yPosition, 50, 20, "Add"));
        buttonList.add(new GuiButton(2, textField.xPosition + textField.width + 2, textField.yPosition, 50, 20, "Add"));
    }
}
