package com.ipixelmon.party.client;

import com.ipixelmon.GuiUtil;
import com.ipixelmon.tablet.notification.Notification;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

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
        accept = new GuiButton(0, 0, 22, 35, 20, "Accept");
        deny = new GuiButton(0, 36, 22, 35, 20, "Deny");
    }

    // TODO: Work on accept and deny.

    @Override
    public void draw(int mouseX, int mouseY) {
        GuiUtil.drawRectFill(- 2, 0, getWidth() + 4, getHeight(), Color.black);
        mc.fontRendererObj.drawSplitString(playerName + " invited you to their party.", 0, 2, maxWidth,0xFFFFFF);

        accept.drawButton(mc, mouseX, mouseY);
        deny.drawButton(mc, mouseX, mouseY);
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
