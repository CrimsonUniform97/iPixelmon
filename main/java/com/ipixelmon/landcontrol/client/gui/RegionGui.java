package com.ipixelmon.landcontrol.client.gui;

import com.google.common.collect.Lists;
import com.ipixelmon.GuiTickBox;
import com.ipixelmon.TimedMessage;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.regions.EnumRegionProperty;
import com.ipixelmon.landcontrol.regions.Region;
import com.ipixelmon.landcontrol.regions.SubRegion;
import com.ipixelmon.landcontrol.regions.packet.PacketModifyRegion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by colby on 1/8/2017.
 */
public class RegionGui extends GuiScreen {

    private static final ResourceLocation bgTexture = new ResourceLocation(iPixelmon.id, "textures/gui/landcontrol/RegionBG.png");

    private static final int BG_HEIGHT = 234, BG_WIDTH = 256;
    private static int POS_X, POS_Y;
    private List<GuiTickBox> tickBoxList = Lists.newArrayList();
    private PlayerListX playerList;
    private GuiTextField addPlayerField, enterMsgField, leaveMsgField;
    private GuiButton addPlayerBtn, enterMsgBtn, leaveMsgBtn;
    public TimedMessage infoMessage = new TimedMessage("", 0);
    private ColorPopupWindow colorPopupWindow = new ColorPopupWindow();

    private Region region;

    public RegionGui(Region region) {
        this.region = region;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        drawDefaultBackground();
        drawBackground();

        for (GuiTickBox tickBox : tickBoxList) tickBox.draw(mc, mouseX, mouseY);

        drawRegionInfo();
        drawMembersArea(mouseX, mouseY);
        drawMsgFields(mouseX, mouseY);
        drawInfoMessage();
        colorPopupWindow.draw();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        playerList.keyTyped(typedChar, keyCode);
        addPlayerField.textboxKeyTyped(typedChar, keyCode);
        enterMsgField.textboxKeyTyped(typedChar, keyCode);
        leaveMsgField.textboxKeyTyped(typedChar, keyCode);

        if (keyCode == Keyboard.KEY_RETURN && !addPlayerField.getText().isEmpty() && addPlayerBtn.enabled)
            iPixelmon.network.sendToServer(new PacketModifyRegion(region.getID(), "addPlayer", addPlayerField.getText()));
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        for (GuiTickBox tickBox : tickBoxList)
            if (tickBox.mouseClicked(mouseX, mouseY)) {
                iPixelmon.network.sendToServer(new PacketModifyRegion(region.getID(), tickBox.getKey(), String.valueOf(tickBox.getValue())));
            }

        if (region.getOwner().equals(mc.thePlayer.getUniqueID()))
            addPlayerField.mouseClicked(mouseX, mouseY, mouseButton);

        if (addPlayerBtn.mousePressed(mc, mouseX, mouseY) && !addPlayerField.getText().isEmpty() && addPlayerBtn.enabled)
            iPixelmon.network.sendToServer(new PacketModifyRegion(region.getID(), "addPlayer", addPlayerField.getText()));

        if (enterMsgBtn.mousePressed(mc, mouseX, mouseY))
            iPixelmon.network.sendToServer(new PacketModifyRegion(region.getID(), "enterMsg", enterMsgField.getText()));

        if (leaveMsgBtn.mousePressed(mc, mouseX, mouseY))
            iPixelmon.network.sendToServer(new PacketModifyRegion(region.getID(), "leaveMsg", leaveMsgField.getText()));

        enterMsgField.mouseClicked(mouseX, mouseY, mouseButton);
        leaveMsgField.mouseClicked(mouseX, mouseY, mouseButton);

        if (enterMsgField.isFocused() || leaveMsgField.isFocused()) {
            boolean enabled = colorPopupWindow.isEnabled();
            colorPopupWindow.mousePressed(mouseX, mouseY, mouseButton);

            if(enabled) {
                if (enterMsgField.isFocused()) {
                    StringBuilder builder = new StringBuilder(enterMsgField.getText());
                    builder.insert(enterMsgField.getCursorPosition(), colorPopupWindow.getSelectedColor().toString());
                    enterMsgField.setText(builder.toString());
                } else if (leaveMsgField.isFocused()) {
                    StringBuilder builder = new StringBuilder(leaveMsgField.getText());
                    builder.insert(leaveMsgField.getCursorPosition(), colorPopupWindow.getSelectedColor().toString());
                    leaveMsgField.setText(builder.toString());
                }
            }
        }
    }

    @Override
    public void initGui() {
        super.initGui();

        POS_X = (this.width - BG_WIDTH) / 2;
        POS_Y = (this.height - BG_HEIGHT) / 2;

        initTickBoxList();
        initMembersArea();
        initMsgFields();
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        addPlayerField.updateCursorCounter();
        addPlayerBtn.enabled = region.getOwner().equals(mc.thePlayer.getUniqueID()) ? !addPlayerField.getText().isEmpty() : false;
        addPlayerField.setEnabled(region.getOwner().equals(mc.thePlayer.getUniqueID()));
    }

    private void drawRegionInfo() {
        fontRendererObj.drawStringWithShadow("Min: " + region.getMin().getX() + ", " + region.getMin().getY() + ", "
                + region.getMin().getZ(), POS_X + 10, POS_Y + BG_HEIGHT - 56, 0xFFFFFF);
        fontRendererObj.drawStringWithShadow("Max: " + region.getMax().getX() + ", " + region.getMax().getY() + ", "
                + region.getMax().getZ(), POS_X + 10, POS_Y + BG_HEIGHT - 43, 0xFFFFFF);
        fontRendererObj.drawStringWithShadow("Region Type: " + ((region instanceof SubRegion) ? "SubRegion" : "Region"),
                POS_X + 10, POS_Y + BG_HEIGHT - 30, 0xFFFFFF);
        fontRendererObj.drawStringWithShadow("Owner: " + region.ownerNameClient, POS_X + 10,
                POS_Y + BG_HEIGHT - 17, 0xFFFFFF);
    }

    private void drawBackground() {
        mc.getTextureManager().bindTexture(bgTexture);
        drawTexturedModalRect(POS_X, POS_Y, 0, 0, BG_WIDTH, BG_HEIGHT);
    }

    private void drawMembersArea(int mouseX, int mouseY) {
        fontRendererObj.drawStringWithShadow("Members:", playerList.xPosition, playerList.yPosition - 10,
                0xFFFFFF);
        playerList.draw(mouseX, mouseY, Mouse.getDWheel());
        addPlayerField.drawTextBox();
        addPlayerBtn.drawButton(mc, mouseX, mouseY);
    }

    private void drawMsgFields(int mouseX, int mouseY) {
        fontRendererObj.drawStringWithShadow("Enter Message: ", POS_X + 10, enterMsgField.yPosition + (11 / 2),
                0xFFFFFF);
        fontRendererObj.drawStringWithShadow("Leave Message: ", POS_X + 10, leaveMsgField.yPosition + (11 / 2),
                0xFFFFFF);
        enterMsgField.drawTextBox();
        leaveMsgField.drawTextBox();
        enterMsgBtn.drawButton(mc, mouseX, mouseY);
        leaveMsgBtn.drawButton(mc, mouseX, mouseY);
    }

    private void drawInfoMessage() {
        if (infoMessage.hasMessage()) {
            int xOffset = (BG_WIDTH - fontRendererObj.getStringWidth(infoMessage.getMessage())) / 2;
            fontRendererObj.drawString(EnumChatFormatting.RED.toString() + EnumChatFormatting.ITALIC.toString() +
                    infoMessage.getMessage(), POS_X + xOffset, POS_Y - fontRendererObj.FONT_HEIGHT, 0xFFFFFF);
        }
    }

    private void initTickBoxList() {
        tickBoxList.clear();

        int yTotal = -16;
        int i = 0;
        for (EnumRegionProperty property : EnumRegionProperty.values()) {
            if (yTotal + 25 >= BG_HEIGHT - 140) yTotal = -16;
            tickBoxList.add(new GuiTickBox(POS_X + 50 + (i < 4 ? 30 : 150),
                    POS_Y + (yTotal += 25), property.name(), region.getProperty(property)));
            i++;
        }

        if (!(region.getOwner().equals(mc.thePlayer.getUniqueID()))) {
            for (GuiTickBox tickBox : tickBoxList) tickBox.enabled = false;
        }
    }

    private void initMembersArea() {
        playerList = new PlayerListX(POS_X + BG_WIDTH - 125, POS_Y + BG_HEIGHT - 70, 120,
                35, region);
        addPlayerField = new GuiTextField(0, fontRendererObj, playerList.xPosition + 1,
                playerList.yPosition + playerList.height + 5, playerList.width - 37, 20);
        addPlayerBtn = new GuiButton(0, addPlayerField.xPosition + addPlayerField.width + 2,
                addPlayerField.yPosition, 35, 20, "Add");
        addPlayerBtn.enabled = false;
        addPlayerField.setEnabled(false);
    }

    private void initMsgFields() {
        enterMsgField = new GuiTextField(1, fontRendererObj, playerList.xPosition + -37,
                playerList.yPosition - 57, 156 - 35, 20);
        leaveMsgField = new GuiTextField(2, fontRendererObj, enterMsgField.xPosition,
                enterMsgField.yPosition + 23, enterMsgField.width, enterMsgField.height);

        // TODO: Enable adding color to text in message. Could use the selection from the field and add some color popup window, YEAH!
        enterMsgField.setMaxStringLength(100);
        leaveMsgField.setMaxStringLength(100);

        enterMsgField.setText(region.getEnterMsg() == null ? "" : region.getEnterMsg());
        leaveMsgField.setText(region.getLeaveMsg() == null ? "" : region.getLeaveMsg());


        enterMsgBtn = new GuiButton(1,
                enterMsgField.xPosition + enterMsgField.width + 5, enterMsgField.yPosition,
                30, 20, "Set");
        leaveMsgBtn = new GuiButton(2,
                leaveMsgField.xPosition + leaveMsgField.width + 5, leaveMsgField.yPosition,
                30, 20, "Set");
    }
}
