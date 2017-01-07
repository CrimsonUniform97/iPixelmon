package com.ipixelmon.landcontrol.gui;

import com.ipixelmon.landcontrol.ToolCupboardTileEntity;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Mouse;

import java.io.IOException;

/**
 * Created by colby on 1/7/2017.
 */
public class ToolCupboardGui extends GuiScreen {

    private ToolCupboardTileEntity tileEntity;
    private PlayerList playerList;
    private GuiTextField playerField;

    public ToolCupboardGui(ToolCupboardTileEntity tileEntity) {
        this.tileEntity = tileEntity;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        playerList.draw(mouseX, mouseY, Mouse.getDWheel());
        playerField.drawTextBox();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        playerField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        playerList.keyTyped(typedChar, keyCode);
        playerField.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        playerField.updateCursorCounter();
        this.buttonList.get(0).enabled = playerList.getSelected() > -1;
        this.buttonList.get(1).enabled = !playerField.getText().isEmpty();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        // TODO:
    }

    @Override
    public void initGui() {
        super.initGui();

        int listWidth = 85;
        int listHeight = 100;
        int xPos = (this.width - listWidth) / 2;
        int yPos = (this.height - listHeight) / 2;
        xPos -= 20;

        playerList = new PlayerList(xPos, yPos, listWidth, listHeight, tileEntity);

        this.buttonList.add(new GuiButton(0, xPos, yPos + listHeight + 2, listWidth, 20, "Remove"));

        playerField = new GuiTextField(0, mc.fontRendererObj, xPos + listWidth + 10, yPos + 10, 65, 20);

        this.buttonList.add(new GuiButton(1, playerField.xPosition + playerField.width + 5, playerField.yPosition,
                40, 20, "Add"));
    }
}
