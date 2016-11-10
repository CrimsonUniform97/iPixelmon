package com.ipixelmon.tablet.client.apps.friends;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.client.App;
import com.ipixelmon.tablet.client.apps.friends.packet.PacketAddFriendReq;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.*;

/**
 * Created by colby on 11/4/2016.
 */
// TODO: Once this is done, finish up the mail app
public class Friends extends App {

    // TODO: Come up with a better icon
    // TODO: Make a friend request packet and system.
    // TODO: Need a list for friend requests
    // TODO: Send packet when friend comes online

    private GuiFriends friendsList;
    private GuiFriendRequests requestsList;
    private GuiTextField addFriendTxtField;
    public static Set<Friend> friends = new TreeSet<>();
    public static Set<FriendRequest> requests;

    public Friends(String name) {
        super(name);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        friendsList.drawScreen(mouseX, mouseY, partialTicks);
        requestsList.drawScreen(mouseX, mouseY, partialTicks);
        addFriendTxtField.drawTextBox();

        mc.fontRendererObj.drawStringWithShadow("Friend Requests",
                requestsList.xPosition + ((requestsList.width - mc.fontRendererObj.getStringWidth("Friend Requests")) / 2),
                requestsList.yPosition - 10, 0xFFFFFF);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        addFriendTxtField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        addFriendTxtField.textboxKeyTyped(typedChar, keyCode);

        if(keyCode == Keyboard.KEY_RETURN)
            iPixelmon.network.sendToServer(new PacketAddFriendReq(addFriendTxtField.getText()));
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        addFriendTxtField.updateCursorCounter();

        this.buttonList.get(0).enabled = requestsList.selectedIndex != -1;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();

        if(requests == null) {
            requests = new TreeSet<>();
            FriendsAPI.populateFriendRequests();
        }

        addFriendTxtField = new GuiTextField(0, fontRendererObj, 300, 0, 100, 20);

        friendsList = new GuiFriends(mc, screenBounds.getX() + 10, screenBounds.getY() + 30, 100, 100, 12, this);

        requestsList = new GuiFriendRequests(mc, screenBounds.getX() + screenBounds.getWidth() - 110, screenBounds.getY() + screenBounds.getHeight() - 130, 100, 100, 12, this);

        requestsList.setDrawThumbAllTheTime(true);
        friendsList.setDrawThumbAllTheTime(true);

        this.buttonList.add(new GuiButton(0, requestsList.xPosition - 1, requestsList.yPosition + requestsList.height + 2, 50, 20, "Accept"));
        this.buttonList.add(new GuiButton(1, this.buttonList.get(0).xPosition + this.buttonList.get(0).width + 2, this.buttonList.get(0).yPosition, 50, 20, "Deny"));

        this.buttonList.get(0).enabled = false;
        this.buttonList.get(1).enabled = false;
    }


}
