package com.ipixelmon.tablet.apps.party;

import com.google.common.collect.Maps;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.party.PacketSendPartyInvite;
import com.ipixelmon.tablet.apps.App;
import com.ipixelmon.tablet.apps.friends.GuiFriendsList;
import net.minecraft.client.gui.GuiButton;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

/**
 * Created by colby on 12/18/2016.
 */
public class PartyApp extends App {

    private GuiFriendsList friendsList;
    private GuiButton sendReq;
    private PlayersInPartyList partyList;

    public static Map<UUID, String> playersInParty = Maps.newHashMap();

    public PartyApp(String name, boolean register) {
        super(name, register);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        friendsList.draw(mouseX, mouseY, Mouse.getDWheel());
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        if(button == sendReq) {
            if(friendsList.getSelected() > -1) {
                iPixelmon.network.sendToServer(new PacketSendPartyInvite(friendsList.getSelectedFriend().name));
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        friendsList.keyTyped(typedChar, keyCode);
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();

        friendsList = new GuiFriendsList(getScreenBounds().getX(), getScreenBounds().getY(), 100, 75);

        this.buttonList.add(sendReq = new GuiButton(0, friendsList.xPosition, friendsList.yPosition + friendsList.height, 100, 20, "Send Request"));
    }
}
