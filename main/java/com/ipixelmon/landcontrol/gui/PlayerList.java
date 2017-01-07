package com.ipixelmon.landcontrol.gui;

import com.ipixelmon.GuiScrollList;
import com.ipixelmon.landcontrol.ToolCupboardTileEntity;

/**
 * Created by colby on 1/7/2017.
 */
public class PlayerList extends GuiScrollList {

    private ToolCupboardTileEntity tileEntity;

    public PlayerList(int xPosition, int yPosition, int width, int height, ToolCupboardTileEntity tileEntity) {
        super(xPosition, yPosition, width, height);
        this.tileEntity = tileEntity;
    }

    @Override
    public int getObjectHeight(int index) {
        return 10;
    }

    @Override
    public void drawObject(int index, int mouseX, int mouseY, boolean isHovering) {
        mc.fontRendererObj.drawString((String) tileEntity.getPlayers().values().toArray()[index], 0, 0, 0xFFFFFF);
    }

    @Override
    public int getSize() {
        return tileEntity.getPlayers().size();
    }

    @Override
    public void elementClicked(int index, int mouseX, int mouseY, boolean doubleClick) {

    }

}
