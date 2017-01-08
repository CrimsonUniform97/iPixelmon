package com.ipixelmon.landcontrol.client.gui;

import com.ipixelmon.GuiScrollList;

import java.util.Map;
import java.util.UUID;

/**
 * Created by colby on 1/7/2017.
 */
public class PlayerList extends GuiScrollList {

    private Map<UUID, String> players;

    public PlayerList(int xPosition, int yPosition, int width, int height, Map<UUID, String> players) {
        super(xPosition, yPosition, width, height);
        this.players = players;
    }

    @Override
    public int getObjectHeight(int index) {
        return 10;
    }

    @Override
    public void drawObject(int index, int mouseX, int mouseY, boolean isHovering) {
        mc.fontRendererObj.drawString((String) players.values().toArray()[index], 0, 0, 0xFFFFFF);
    }

    @Override
    public int getSize() {
        return players.size();
    }

    @Override
    public void elementClicked(int index, int mouseX, int mouseY, boolean doubleClick) {

    }

    public String getSelectedName() {
        return (String) players.values().toArray()[getSelected()];
    }

    public UUID getSelectedUUID() {
        return (UUID) players.keySet().toArray()[getSelected()];
    }

}
