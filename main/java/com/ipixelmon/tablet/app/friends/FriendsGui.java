package com.ipixelmon.tablet.app.friends;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.AppGui;
import com.ipixelmon.tablet.app.friends.packet.PacketFriendRequestToServer;
import com.ipixelmon.tablet.app.friends.packet.PacketRemoveFriend;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;

import java.io.IOException;

/**
 * Created by colby on 12/31/2016.
 */
public class FriendsGui extends AppGui {

    public FriendsGui(Object[] objects) {
        super(objects);
    }

    private FriendRequestsList friendRequestsList;
    private FriendsList friendsList;
    private GuiTextField playerNameField;

    @Override
    public void drawScreen(int mouseX, int mouseY, int dWheel, float partialTicks) {
        friendsList.draw(mouseX, mouseY, dWheel);
        friendRequestsList.draw(mouseX, mouseY, dWheel);
        playerNameField.drawTextBox();
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);

        switch(button.id) {
            // accept btn
            case 0: {
                iPixelmon.network.sendToServer(
                        new PacketFriendRequestToServer(friendRequestsList.getSelectedFriendRequest(), true));
                break;
            }
            // deny btn
            case 1: {
                iPixelmon.network.sendToServer(
                        new PacketFriendRequestToServer(friendRequestsList.getSelectedFriendRequest(), false));
                break;
            }
            // send btn
            case 2: {
                FriendsAPI.Client.sendFriendRequest(playerNameField.getText());
                break;
            }
            // remove btn
            case 3: {
                iPixelmon.network.sendToServer(new PacketRemoveFriend(friendsList.getSelectedFriend()));
                break;
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        playerNameField.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        playerNameField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        playerNameField.updateCursorCounter();

        boolean enableAcceptDenyBtns = friendRequestsList.getSelected() > -1;

        this.buttonList.get(0).enabled = enableAcceptDenyBtns;
        this.buttonList.get(1).enabled = enableAcceptDenyBtns;
        this.buttonList.get(2).enabled = !playerNameField.getText().isEmpty();
        this.buttonList.get(3).enabled = friendsList.getSelected() > -1;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();

        int listX = screenBounds.getX() + 2;
        int listY = screenBounds.getY() + 2;
        int listWidth = screenBounds.getWidth() / 2;
        int listHeight = screenBounds.getHeight() - 50;
        listWidth -= listWidth / 2;

        friendsList = new FriendsList(listX, listY, listWidth, listHeight);
        friendRequestsList = new FriendRequestsList(listX + listWidth + 2, listY, listWidth, listHeight);

        this.buttonList.add(new GuiButton(0,
                friendRequestsList.xPosition,
                friendRequestsList.yPosition + friendRequestsList.height + 1,
                listWidth / 2, 20, "Accept"));

        this.buttonList.add(new GuiButton(1,
                friendRequestsList.xPosition + (listWidth / 2) + 1,
                friendRequestsList.yPosition + friendRequestsList.height + 1,
                (listWidth / 2), 20, "Deny"));

        playerNameField = new GuiTextField(0, mc.fontRendererObj, listX + 1, listY + listHeight + 25,
                listWidth + (listWidth  / 2) - 1, 20);

        this.buttonList.add(new GuiButton(2,
                playerNameField.xPosition + playerNameField.width + 3,
                playerNameField.yPosition,
                (listWidth / 2), 20, "Send"));

        this.buttonList.add(new GuiButton(3,
                friendsList.xPosition,
                friendsList.yPosition + friendsList.height + 1,
                friendsList.width + 1, 20, "Remove"));
    }
}
