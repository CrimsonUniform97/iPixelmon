package com.ipixelmon.landcontrol.client.gui;

import com.ipixelmon.TimedMessage;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.packet.PacketBindNetwork;
import com.ipixelmon.landcontrol.packet.PacketEditPlayer;
import com.ipixelmon.landcontrol.toolCupboard.ToolCupboardTileEntity;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * Created by colby on 1/7/2017.
 */
public class ToolCupboardGui extends GuiScreen {

    public TimedMessage timedMessage = new TimedMessage("", 0);
    private ToolCupboardTileEntity tileEntity;
    private PlayerList playerList;
    private GuiTextField playerField;
    private Map<UUID, String> players;
    private boolean hasNetwork;
    private GuiButton bindNetworkBtn;

    public ToolCupboardGui(ToolCupboardTileEntity tileEntity, boolean hasNetwork, Map<UUID, String> players) {
        this.tileEntity = tileEntity;
        this.players = players;
        this.hasNetwork = hasNetwork;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        if(hasNetwork) {
            playerList.draw(mouseX, mouseY, Mouse.getDWheel());
            playerField.drawTextBox();

            if (timedMessage.hasMessage()) {
                mc.fontRendererObj.drawString(EnumChatFormatting.RED.toString() + EnumChatFormatting.ITALIC.toString() +
                        timedMessage.getMessage(), playerField.xPosition, playerField.yPosition + playerField.height + 4, 0xFFFFFF);
            }

            int networkIDWidth = mc.fontRendererObj.getStringWidth("Network ID: " + tileEntity.getNetwork().getID().toString());

            mc.fontRendererObj.drawString("Network ID: " + tileEntity.getNetwork().getID().toString().toString(),
                    (this.width - networkIDWidth) / 2, playerList.yPosition - 15, 0xFFFFFF);
        } else {
            this.drawDefaultBackground();
            bindNetworkBtn.drawButton(mc, mouseX, mouseY);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if(hasNetwork) {
            playerField.mouseClicked(mouseX, mouseY, mouseButton);
            timedMessage.setMessage("", 0);
        } else {
            if(bindNetworkBtn.mousePressed(mc, mouseX, mouseY)) {
                iPixelmon.network.sendToServer(new PacketBindNetwork(tileEntity.getPos()));
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        if(hasNetwork) {
            playerList.keyTyped(typedChar, keyCode);
            playerField.textboxKeyTyped(typedChar, keyCode);

            if (keyCode == Keyboard.KEY_RETURN)
                actionPerformed(this.buttonList.get(1));

            timedMessage.setMessage("", 0);
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();

        if(hasNetwork) {
            playerField.updateCursorCounter();
            this.buttonList.get(0).enabled = playerList.getSelected() > -1;
            this.buttonList.get(1).enabled = !playerField.getText().isEmpty();
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        if(hasNetwork) {
            switch (button.id) {
                case 0: {
                    iPixelmon.network.sendToServer(new PacketEditPlayer(tileEntity.getPos(), playerList.getSelectedUUID().toString(), false));
                    break;
                }
                case 1: {
                    iPixelmon.network.sendToServer(new PacketEditPlayer(tileEntity.getPos(), playerField.getText(), true));
                    break;
                }
            }
        }
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();

        if(hasNetwork) {
            int listWidth = 85;
            int listHeight = 100;
            int xPos = (this.width - listWidth) / 2;
            int yPos = (this.height - listHeight) / 2;
            xPos -= 20;

            playerList = new PlayerList(xPos, yPos, listWidth, listHeight, players);

            this.buttonList.add(new GuiButton(0, xPos, yPos + listHeight + 2, listWidth, 20, "Remove"));

            playerField = new GuiTextField(0, mc.fontRendererObj, xPos + listWidth + 10, yPos + 10, 65, 20);

            this.buttonList.add(new GuiButton(1, playerField.xPosition + playerField.width + 5, playerField.yPosition,
                    40, 20, "Add"));

            this.buttonList.get(0).enabled = false;
            this.buttonList.get(1).enabled = false;

            timedMessage.setMessage("", 0);
        } else {
            bindNetworkBtn = new GuiButton(0, (this.width - 150) / 2, (this.height - 20) / 2, 150,
                    20, "Bind to your network?");
        }
    }

    public GuiTextField getPlayerField() {
        return playerField;
    }
}
