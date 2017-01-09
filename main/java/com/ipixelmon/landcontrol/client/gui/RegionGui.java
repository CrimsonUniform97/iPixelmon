package com.ipixelmon.landcontrol.client.gui;

import com.google.common.collect.Lists;
import com.ipixelmon.GuiTickBox;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.server.regions.EnumRegionProperty;
import com.ipixelmon.landcontrol.server.regions.PacketModifyRegion;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.BlockPos;
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
    private GuiTextField addPlayerField, entranceMessageField, exitMessageField;
    private GuiButton addPlayerBtn;

    private UUID regionID;
    private String owner;
    private BlockPos min, max;
    private boolean isSubRegion;

    private Map<EnumRegionProperty, Boolean> properties;
    private Map<UUID, String> players;

    public RegionGui(UUID regionID, boolean isSubRegion, String owner, BlockPos min, BlockPos max, Map<EnumRegionProperty, Boolean> properties, Map<UUID, String> players) {
        this.regionID = regionID;
        this.isSubRegion = isSubRegion;
        this.owner = owner;
        this.min = min;
        this.max = max;
        this.properties = properties;
        this.players = players;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);

        drawDefaultBackground();

        mc.getTextureManager().bindTexture(bgTexture);
        drawTexturedModalRect(POS_X, POS_Y, 0, 0, BG_WIDTH, BG_HEIGHT);

        for (GuiTickBox tickBox : tickBoxList) tickBox.draw(mc, mouseX, mouseY);


        fontRendererObj.drawStringWithShadow("Min: " + min.getX() + ", " + min.getY() + ", " + min.getZ(), POS_X + 10, POS_Y + BG_HEIGHT - 56, 0xFFFFFF);
        fontRendererObj.drawStringWithShadow("Max: " + max.getX() + ", " + max.getY() + ", " + max.getZ(), POS_X + 10, POS_Y + BG_HEIGHT - 43, 0xFFFFFF);
        fontRendererObj.drawStringWithShadow("Region Type: " + (isSubRegion ? "SubRegion" : "Region"), POS_X + 10, POS_Y + BG_HEIGHT - 30, 0xFFFFFF);
        fontRendererObj.drawStringWithShadow("Owner: " + owner, POS_X + 10, POS_Y + BG_HEIGHT - 17, 0xFFFFFF);

        fontRendererObj.drawStringWithShadow("Members:", playerList.xPosition, playerList.yPosition - 10, 0xFFFFFF);
        playerList.draw(mouseX, mouseY, Mouse.getDWheel());
        addPlayerField.drawTextBox();
        addPlayerBtn.drawButton(mc, mouseX, mouseY);

        fontRendererObj.drawStringWithShadow("Enter Message: ", POS_X + 10, entranceMessageField.yPosition + (11 / 2), 0xFFFFFF);
        fontRendererObj.drawStringWithShadow("Leave Message: ", POS_X + 10, exitMessageField.yPosition + (11 / 2), 0xFFFFFF);
        entranceMessageField.drawTextBox();
        exitMessageField.drawTextBox();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        playerList.keyTyped(typedChar, keyCode);
        addPlayerField.textboxKeyTyped(typedChar, keyCode);

        if (keyCode == Keyboard.KEY_RETURN && !addPlayerField.getText().isEmpty() && addPlayerBtn.enabled)
            iPixelmon.network.sendToServer(new PacketModifyRegion(regionID, "addPlayer", addPlayerField.getText()));
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        for (GuiTickBox tickBox : tickBoxList)
            if (tickBox.mouseClicked(mouseX, mouseY)) {
                iPixelmon.network.sendToServer(new PacketModifyRegion(regionID, tickBox.getKey(), String.valueOf(tickBox.getValue())));
            }

        if (owner.equalsIgnoreCase(mc.thePlayer.getName()))
            addPlayerField.mouseClicked(mouseX, mouseY, mouseButton);

        if (addPlayerBtn.mousePressed(mc, mouseX, mouseY) && !addPlayerField.getText().isEmpty() && addPlayerBtn.enabled)
            iPixelmon.network.sendToServer(new PacketModifyRegion(regionID, "addPlayer", addPlayerField.getText()));
    }

    @Override
    public void initGui() {
        super.initGui();

        POS_X = (this.width - BG_WIDTH) / 2;
        POS_Y = (this.height - BG_HEIGHT) / 2;

        tickBoxList.clear();

        int yTotal = -16;
        int i = 0;
        for (EnumRegionProperty property : properties.keySet()) {
            if (yTotal + 25 >= BG_HEIGHT - 140) yTotal = -16;
            tickBoxList.add(new GuiTickBox(POS_X + 50 + (i < 4 ? 30 : 150),
                    POS_Y + (yTotal += 25), property.name(), properties.get(property)));
            i++;
        }

        if (!(owner.equalsIgnoreCase(Minecraft.getMinecraft().thePlayer.getName()))) {
            for (GuiTickBox tickBox : tickBoxList) tickBox.enabled = false;
        }

        playerList = new PlayerListX(POS_X + BG_WIDTH - 125, POS_Y + BG_HEIGHT - 70, 120, 35, regionID, players);
        addPlayerField = new GuiTextField(0, fontRendererObj, playerList.xPosition + 1,
                playerList.yPosition + playerList.height + 5, playerList.width - 37, 20);
        addPlayerBtn = new GuiButton(0, addPlayerField.xPosition + addPlayerField.width + 2, addPlayerField.yPosition, 35, 20, "Add");
        addPlayerBtn.enabled = false;
        addPlayerField.setEnabled(false);

        entranceMessageField = new GuiTextField(1, fontRendererObj,playerList.xPosition +  - 37, playerList.yPosition - 57, 156, 20);
        exitMessageField = new GuiTextField(2, fontRendererObj,entranceMessageField.xPosition,
                entranceMessageField.yPosition + 23, entranceMessageField.width, entranceMessageField.height);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        addPlayerField.updateCursorCounter();
        addPlayerBtn.enabled = owner.equalsIgnoreCase(mc.thePlayer.getName()) ? !addPlayerField.getText().isEmpty() : false;
        addPlayerField.setEnabled(owner.equalsIgnoreCase(mc.thePlayer.getName()));
    }


}
