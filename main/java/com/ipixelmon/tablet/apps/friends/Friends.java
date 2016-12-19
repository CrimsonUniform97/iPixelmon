package com.ipixelmon.tablet.apps.friends;

import com.ipixelmon.TimedMessage;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.apps.App;
import com.ipixelmon.tablet.client.CustomGuiTextField;
import com.ipixelmon.tablet.client.TextBtn;
import com.ipixelmon.tablet.apps.friends.packet.PacketAddFriendReq;
import com.ipixelmon.tablet.apps.friends.packet.PacketAcceptDeny;
import com.ipixelmon.tablet.apps.friends.packet.PacketRemoveFriend;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.*;

/**
 * Created by colby on 11/4/2016.
 */
public class Friends extends App {

    private GuiFriendsList friendsList;
    private GuiFriendRequests requestsList;
    private CustomGuiTextField addFriendTxtField;
    public static Set<Friend> friends = new TreeSet<>();
    public static Set<FriendRequest> requests = new TreeSet<>();
    public static TimedMessage message = new TimedMessage("", 0);

    public Friends(String name) {
        super(name, true);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        mc.fontRendererObj.setUnicodeFlag(true);
        super.drawScreen(mouseX, mouseY, partialTicks);
        mc.fontRendererObj.setUnicodeFlag(false);

        friendsList.draw(mouseX, mouseY, Mouse.getDWheel());
        requestsList.drawScreen(mouseX, mouseY, partialTicks);

        addFriendTxtField.drawTextBox();

        if(addFriendTxtField.isFocused()) {
            mc.fontRendererObj.setUnicodeFlag(true);
            mc.fontRendererObj.drawStringWithShadow(EnumChatFormatting.ITALIC + "Press Enter to Send",
                    addFriendTxtField.xPosition +
                            ((addFriendTxtField.getWidth() - mc.fontRendererObj.getStringWidth("Press Enter to Send")) / 2),
                    addFriendTxtField.yPosition - 12, 0xFFFFFF);
            mc.fontRendererObj.setUnicodeFlag(false);
        }

        mc.fontRendererObj.setUnicodeFlag(true);
        mc.fontRendererObj.drawStringWithShadow("Friends",
                friendsList.xPosition + ((friendsList.width - mc.fontRendererObj.getStringWidth("Friends")) / 2),
                friendsList.yPosition - 10, 0xFFFFFF);

        mc.fontRendererObj.drawStringWithShadow("Friend Requests",
                requestsList.xPosition + ((requestsList.width - mc.fontRendererObj.getStringWidth("Friend Requests")) / 2),
                requestsList.yPosition - 10, 0xFFFFFF);

        mc.fontRendererObj.drawStringWithShadow("Add Friend:",
                addFriendTxtField.xPosition - mc.fontRendererObj.getStringWidth("Add Friend:") - 2,
                addFriendTxtField.yPosition, 0xFFFFFF);
        mc.fontRendererObj.setUnicodeFlag(false);


            mc.fontRendererObj.drawString(message.getMessage(), getScreenBounds().getX() +
                    ((getScreenBounds().getWidth() - mc.fontRendererObj.getStringWidth(message.getMessage())) / 2), getScreenBounds().getY() + 2, 0xFFFFFF);

    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {

        if(button.id == 2) {
            if(friends.toArray().length > friendsList.getSelected()) {
                Friend friend = (Friend) friends.toArray()[friendsList.getSelected()];
                iPixelmon.network.sendToServer(new PacketRemoveFriend(friend.uuid));
            }
        }

        if (button.id <= 1) {
            if(requests.toArray().length > requestsList.selectedIndex) {
                FriendRequest friendRequest = (FriendRequest) requests.toArray()[requestsList.selectedIndex];
                if (friendRequest != null) {
                    UUID player = friendRequest.friend;
                    iPixelmon.network.sendToServer(new PacketAcceptDeny(player, button.id == 0));
                }
            }
        }
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

        if (keyCode == Keyboard.KEY_RETURN && addFriendTxtField.isFocused())
            iPixelmon.network.sendToServer(new PacketAddFriendReq(addFriendTxtField.getText()));
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        addFriendTxtField.updateCursorCounter();

        this.buttonList.get(0).enabled = requestsList.selectedIndex != -1;
        this.buttonList.get(1).enabled = requestsList.selectedIndex != -1;
        this.buttonList.get(2).enabled = friendsList.getSelected() != -1;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();

        FriendsAPI.populateFriendRequests();

        FontRenderer fontRenderer = fontRendererObj;
        fontRenderer.setUnicodeFlag(true);
        addFriendTxtField = new CustomGuiTextField(0, fontRenderer, getScreenBounds().getX() + getScreenBounds().getWidth() - 67, getScreenBounds().getY() + getScreenBounds().getHeight() - 12, 65, 10);

        friendsList = new GuiFriendsList(getScreenBounds().getX() + 8, getScreenBounds().getY() + 12, 65, 65);

        requestsList = new GuiFriendRequests(mc, friendsList.xPosition, friendsList.yPosition + friendsList.height + 34, 65, 65, 12, this);

        requestsList.setDrawThumbAllTheTime(true);

        this.buttonList.add(new TextBtn(0, requestsList.xPosition + 1, requestsList.yPosition + requestsList.height + 2, mc.fontRendererObj.getStringWidth("Accept"), 12, "Accept"));
        this.buttonList.add(new TextBtn(1, requestsList.xPosition + requestsList.width - 16, this.buttonList.get(0).yPosition, mc.fontRendererObj.getStringWidth("Deny"), 12, "Deny"));
        this.buttonList.add(new TextBtn(2, friendsList.xPosition + ((friendsList.width - 24) / 2), friendsList.yPosition + friendsList.height + 2, mc.fontRendererObj.getStringWidth("Remove"), 12, "Remove"));

        this.buttonList.get(0).enabled = false;
        this.buttonList.get(1).enabled = false;
        this.buttonList.get(2).enabled = false;
    }


}
