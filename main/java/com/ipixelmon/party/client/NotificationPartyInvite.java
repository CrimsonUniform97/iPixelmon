package com.ipixelmon.party.client;

import com.ipixelmon.GuiUtil;
import com.ipixelmon.tablet.notification.Notification;
import net.minecraft.client.gui.GuiButton;

import java.awt.*;
import java.util.UUID;

/**
 * Created by colbymchenry on 12/18/16.
 */
public class NotificationPartyInvite extends Notification {

    private String playerName;
    private UUID partyID;
    private GuiButton accept, deny;

    public NotificationPartyInvite(String playerName, UUID partyID) {
        super();
        this.playerName = playerName;
        this.partyID = partyID;
    }

    @Override
    public void draw() {
        GuiUtil.drawRectFill(0, 0, getWidth(), getHeight(), Color.black);
        mc.fontRendererObj.drawString(playerName + " invited you to their party.", 0, 2, 0xFFFFFF);
    }

    @Override
    public void actionPerformed() {
        super.actionPerformed();
    }

    @Override
    public int getHeight() {
        return 12;
    }

    @Override
    public int getWidth() {
        return maxWidth;
    }

    @Override
    public long getDuration() {
        return 10;
    }
}
