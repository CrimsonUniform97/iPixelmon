package com.ipixelmon.itemdisplay;

import com.ipixelmon.iPixelmon;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.math.BlockPos;
import org.lwjgl.input.Keyboard;

import java.io.IOException;

public class ItemDisplayBlockGui extends GuiScreen {

    private ItemDisplayBlockTileEntity tileEntity;
    private GuiTextField scale, xOffset, yOffset, zOffset;

    public ItemDisplayBlockGui(ItemDisplayBlockTileEntity tileEntity) {
        this.tileEntity = tileEntity;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.scale.drawTextBox();
        this.xOffset.drawTextBox();
        this.yOffset.drawTextBox();
        this.zOffset.drawTextBox();

        fontRendererObj.drawStringWithShadow("Scale", scale.xPosition, scale.yPosition - 10, 0xFFFFFF);
        fontRendererObj.drawStringWithShadow("X", xOffset.xPosition, xOffset.yPosition - 10, 0xFFFFFF);
        fontRendererObj.drawStringWithShadow("Y", yOffset.xPosition, yOffset.yPosition - 10, 0xFFFFFF);
        fontRendererObj.drawStringWithShadow("Z", zOffset.xPosition, zOffset.yPosition - 10, 0xFFFFFF);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        iPixelmon.network.sendToServer(new PacketUpdateTileEntity(tileEntity.getPos(), getScale(), getxOffset(), getyOffset(), getzOffset()));
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);

        String text = "" + typedChar;

        if(!(text).matches("[0-9]+") &&
                keyCode != Keyboard.KEY_BACK &&
                keyCode != Keyboard.KEY_PERIOD &&
                keyCode != Keyboard.KEY_LEFT &&
                keyCode != Keyboard.KEY_RIGHT) return;

        this.scale.textboxKeyTyped(typedChar, keyCode);
        this.xOffset.textboxKeyTyped(typedChar, keyCode);
        this.yOffset.textboxKeyTyped(typedChar, keyCode);
        this.zOffset.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.scale.mouseClicked(mouseX, mouseY, mouseButton);
        this.xOffset.mouseClicked(mouseX, mouseY, mouseButton);
        this.yOffset.mouseClicked(mouseX, mouseY, mouseButton);
        this.zOffset.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();

        int yOffset = 60;

        this.buttonList.add(new GuiButton(0, (this.width - 30) / 2, this.height - yOffset, 30, 20, "Set"));

        int totalWidth = this.width / 2;
        int section = totalWidth / 4;
        int x = (this.width - totalWidth) / 2;

        int boxWidth = 30;
        this.scale = new GuiTextField(0, fontRendererObj, x + (section * 0), this.height - (yOffset + 30), boxWidth, 20);
        this.xOffset = new GuiTextField(1, fontRendererObj, x + (section * 1), this.height - (yOffset + 30), boxWidth, 20);
        this.yOffset = new GuiTextField(2, fontRendererObj, x + (section * 2), this.height - (yOffset + 30), boxWidth, 20);
        this.zOffset = new GuiTextField(3, fontRendererObj, x + (section * 3), this.height - (yOffset + 30), boxWidth, 20);

        this.scale.setText(String.valueOf(tileEntity.getScale()));
        this.xOffset.setText(String.valueOf(tileEntity.getxOffset()));
        this.yOffset.setText(String.valueOf(tileEntity.getyOffset()));
        this.zOffset.setText(String.valueOf(tileEntity.getzOffset()));
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        this.scale.updateCursorCounter();
        this.xOffset.updateCursorCounter();
        this.yOffset.updateCursorCounter();
        this.zOffset.updateCursorCounter();

        this.buttonList.get(0).enabled = getScale() != -999 && getxOffset() != Double.NaN && getyOffset() != Double.NaN &&
                getzOffset() != Double.NaN;
        this.buttonList.get(0).enabled = !scale.getText().isEmpty() && !xOffset.getText().isEmpty() &&
                !yOffset.getText().isEmpty() && !zOffset.getText().isEmpty();
    }

    private int getScale() {
        try {
            return Integer.parseInt(scale.getText());
        }catch (NumberFormatException e) {
          return -999;
        }
    }

    private double getxOffset() {
        try {
            return Double.parseDouble(xOffset.getText());
        }catch (NumberFormatException e) {
            return Double.NaN;
        }
    }

    private double getyOffset() {
        try {
            return Double.parseDouble(yOffset.getText());
        }catch (NumberFormatException e) {
            return Double.NaN;
        }
    }

    private double getzOffset() {
        try {
            return Double.parseDouble(zOffset.getText());
        }catch (NumberFormatException e) {
            return Double.NaN;
        }
    }
}
