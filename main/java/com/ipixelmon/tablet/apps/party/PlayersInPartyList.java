package com.ipixelmon.tablet.apps.party;

import com.ipixelmon.GuiScrollList;
import com.ipixelmon.party.PartyAPI;

/**
 * Created by colby on 12/25/2016.
 */
public class PlayersInPartyList extends GuiScrollList {

    public PlayersInPartyList(int xPosition, int yPosition, int width, int height) {
        super(xPosition, yPosition, width, height);
    }

    @Override
    public int getObjectHeight(int index) {
        return 10;
    }

    @Override
    public void drawObject(int index, int mouseX, int mouseY, boolean isHovering) {
        String player = (String) PartyAPI.Client.getPlayersInPartyName().toArray()[index];
        mc.fontRendererObj.drawString(player, 0, 0, 0xFFFFFF);
    }

    @Override
    public int getSize() {
        return PartyAPI.Client.getPlayersInPartyID().size();
    }

    @Override
    public void elementClicked(int index, int mouseX, int mouseY, boolean doubleClick) {

    }
}
