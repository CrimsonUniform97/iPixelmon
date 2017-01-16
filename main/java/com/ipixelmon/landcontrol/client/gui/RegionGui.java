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
    private CustomTextField addPlayerField, enterMsgField, leaveMsgField;
    private GuiButton addPlayerBtn, enterMsgBtn, leaveMsgBtn;
    public TimedMessage infoMessage = new TimedMessage("", 0);
    private ColorPopupWindow colorPopupWindow = new ColorPopupWindow();

    private Region region;
    private boolean isSubRegion;

    public RegionGui(Region region, boolean isSubRegion) {
        this.region = region;
        this.isSubRegion = isSubRegion;
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
        addPlayerField.keyTyped(typedChar, keyCode);
        enterMsgField.keyTyped(typedChar, keyCode);
        leaveMsgField.keyTyped(typedChar, keyCode);

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
            addPlayerField.mouseClicked(mouseX, mouseY);

        if (addPlayerBtn.mousePressed(mc, mouseX, mouseY) && !addPlayerField.getText().isEmpty() && addPlayerBtn.enabled)
            iPixelmon.network.sendToServer(new PacketModifyRegion(region.getID(), "addPlayer", addPlayerField.getText()));

        if (enterMsgBtn.mousePressed(mc, mouseX, mouseY))
            iPixelmon.network.sendToServer(new PacketModifyRegion(region.getID(), "enterMsg", enterMsgField.getText()));

        if (leaveMsgBtn.mousePressed(mc, mouseX, mouseY))
            iPixelmon.network.sendToServer(new PacketModifyRegion(region.getID(), "leaveMsg", leaveMsgField.getText()));

        boolean enabled = colorPopupWindow.isEnabled();

        if (enterMsgField.isFocused() || leaveMsgField.isFocused()) {
            colorPopupWindow.mousePressed(mouseX, mouseY, mouseButton);

            if (enabled) {
                if (enterMsgField.isFocused()) {
                    StringBuilder builder = new StringBuilder(enterMsgField.getText());
                    builder.insert(enterMsgField.getCursorPos(), colorPopupWindow.getSelectedColor().toString());
                    enterMsgField.setText(builder.toString());
                } else if (leaveMsgField.isFocused()) {
                    StringBuilder builder = new StringBuilder(leaveMsgField.getText());
                    builder.insert(leaveMsgField.getCursorPos(), colorPopupWindow.getSelectedColor().toString());
                    leaveMsgField.setText(builder.toString());
                }
            }
        }

        if (!enabled) {
            enterMsgField.mouseClicked(mouseX, mouseY);
            leaveMsgField.mouseClicked(mouseX, mouseY);
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
        addPlayerBtn.enabled = region.getOwner().equals(mc.thePlayer.getUniqueID()) ? !addPlayerField.getText().isEmpty() : false;
        addPlayerField.setEnabled(region.getOwner().equals(mc.thePlayer.getUniqueID()));
    }

    private void drawRegionInfo() {
        fontRendererObj.drawStringWithShadow("Min: " + region.getMin().getX() + ", " + region.getMin().getY() + ", "
                + region.getMin().getZ(), POS_X + 10, POS_Y + BG_HEIGHT - 56, 0xFFFFFF);
        fontRendererObj.drawStringWithShadow("Max: " + region.getMax().getX() + ", " + region.getMax().getY() + ", "
                + region.getMax().getZ(), POS_X + 10, POS_Y + BG_HEIGHT - 43, 0xFFFFFF);
        fontRendererObj.drawStringWithShadow("Region Type: " + (isSubRegion ? "SubRegion" : "Region"),
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
        addPlayerField.drawTextField();
        addPlayerBtn.drawButton(mc, mouseX, mouseY);
    }

    private void drawMsgFields(int mouseX, int mouseY) {
        fontRendererObj.drawStringWithShadow("Enter Message: ", POS_X + 10, enterMsgField.getBounds().getY() + (11 / 2),
                0xFFFFFF);
        fontRendererObj.drawStringWithShadow("Leave Message: ", POS_X + 10, leaveMsgField.getBounds().getY() + (11 / 2),
                0xFFFFFF);
        enterMsgField.drawTextField();
        leaveMsgField.drawTextField();
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
        addPlayerField = new CustomTextField(playerList.xPosition + 1,
                playerList.yPosition + playerList.height + 5, playerList.width - 37, 20);
        addPlayerBtn = new GuiButton(0, addPlayerField.getBounds().getX() + addPlayerField.getBounds().getWidth() + 2,
                addPlayerField.getBounds().getY(), 35, 20, "Add");
        addPlayerBtn.enabled = false;
        addPlayerField.setEnabled(false);
    }

    private void initMsgFields() {
        enterMsgField = new CustomTextField( playerList.xPosition + -37,
                playerList.yPosition - 57, 156 - 35, 20);
        leaveMsgField = new CustomTextField( enterMsgField.getBounds().getX(),
                enterMsgField.getBounds().getY() + 23, enterMsgField.getBounds().getWidth(), enterMsgField.getBounds().getHeight());

        // TODO: Clicking with formatted text still needs work...
//        enterMsgField.setMaxStringLength(100);
//        leaveMsgField.setMaxStringLength(100);

        enterMsgField.setText(region.getEnterMsg() == null ? "" : region.getEnterMsg());
        leaveMsgField.setText(region.getLeaveMsg() == null ? "" : region.getLeaveMsg());


        enterMsgBtn = new GuiButton(1,
                enterMsgField.getBounds().getX() + enterMsgField.getBounds().getWidth() + 5, enterMsgField.getBounds().getY(),
                30, 20, "Set");
        leaveMsgBtn = new GuiButton(2,
                leaveMsgField.getBounds().getX() + leaveMsgField.getBounds().getWidth() + 5, leaveMsgField.getBounds().getY(),
                30, 20, "Set");
    }
}
