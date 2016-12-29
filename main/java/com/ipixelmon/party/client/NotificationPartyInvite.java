package com.ipixelmon.party.client;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.party.PacketAcceptPartyInvite;
import com.ipixelmon.tablet.notification.Notification;
import com.ipixelmon.util.Utils;
import net.minecraft.client.gui.GuiButton;

import java.awt.*;
import java.util.UUID;

/**
 * Created by colbymchenry on 12/18/16.
 */
public class NotificationPartyInvite extends Notification {

    private String playerName;
    private UUID partyID;
    private GuiButton accept;

    public NotificationPartyInvite(String playerName, UUID partyID) {
        super();
        this.playerName = playerName;
        this.partyID = partyID;
        accept = new GuiButton(0, 0, 22, 35, 20, "Accept");
    }

    @Override
    public void draw(int mouseX, int mouseY) {
        Utils.Client.gui.drawRectFill(- 2, 0, getWidth() + 4, getHeight(), Color.black);
        mc.fontRendererObj.drawSplitString(playerName + " invited you to their party.", 0, 2, maxWidth,0xFFFFFF);

        accept.drawButton(mc, mouseX, mouseY);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY) {
        if (accept.mousePressed(mc, mouseX, mouseY))
            iPixelmon.network.sendToServer(new PacketAcceptPartyInvite(partyID));

    }

    @Override
    public void actionPerformed() {
        super.actionPerformed();
    }

    @Override
    public int getHeight() {
        return 44;
    }

    @Override
    public int getWidth() {
        return maxWidth;
    }

    @Override
    public long getDuration() {
        return 10 * 1000L;
    }
}
