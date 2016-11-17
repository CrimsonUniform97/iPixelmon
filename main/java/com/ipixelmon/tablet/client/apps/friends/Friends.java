package com.ipixelmon.tablet.client.apps.friends;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.client.App;
import com.ipixelmon.tablet.client.CustomGuiTextField;
import com.ipixelmon.tablet.client.TextBtn;
import com.ipixelmon.tablet.client.apps.friends.packet.PacketAddFriendReq;
import com.ipixelmon.tablet.client.apps.friends.packet.PacketAcceptDeny;
import com.ipixelmon.tablet.client.apps.friends.packet.PacketRemoveFriend;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.util.*;

/**
 * Created by colby on 11/4/2016.
 */
public class Friends extends App {

    private GuiFriends friendsList;
    private GuiFriendRequests requestsList;
    private CustomGuiTextField addFriendTxtField;
    public static Set<Friend> friends = new TreeSet<>();
    public static Set<FriendRequest> requests = new TreeSet<>();
    private static Object[] message = {"", 0, Calendar.getInstance()};

    public Friends(String name) {
        super(name);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        mc.fontRendererObj.setUnicodeFlag(true);
        super.drawScreen(mouseX, mouseY, partialTicks);
        mc.fontRendererObj.setUnicodeFlag(false);

        friendsList.drawScreen(mouseX, mouseY, partialTicks);
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

        if(Calendar.getInstance().compareTo((Calendar) message[2]) < 0) {
            String txt = (String) message[0];
            mc.fontRendererObj.drawString(txt, screenBounds.getX() +
                    ((screenBounds.getWidth() - mc.fontRendererObj.getStringWidth(txt)) / 2), screenBounds.getY() + 2, 0xFFFFFF);
        }
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {

        if(button.id == 2) {
            if(friends.toArray().length > friendsList.selectedIndex) {
                Friend friend = (Friend) friends.toArray()[friendsList.selectedIndex];
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
        this.buttonList.get(2).enabled = friendsList.selectedIndex != -1;
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();

        FriendsAPI.populateFriendRequests();

        FontRenderer fontRenderer = fontRendererObj;
        fontRenderer.setUnicodeFlag(true);
        addFriendTxtField = new CustomGuiTextField(0, fontRenderer, screenBounds.getX() + screenBounds.getWidth() - 67, screenBounds.getY() + screenBounds.getHeight() - 12, 65, 10);

        friendsList = new GuiFriends(mc, screenBounds.getX() + 8, screenBounds.getY() + 12, 65, 65, 12, this);

        requestsList = new GuiFriendRequests(mc, friendsList.xPosition, friendsList.yPosition + friendsList.height + 34, 65, 65, 12, this);

        requestsList.setDrawThumbAllTheTime(true);
        friendsList.setDrawThumbAllTheTime(true);

        this.buttonList.add(new TextBtn(0, requestsList.xPosition + 1, requestsList.yPosition + requestsList.height + 2, mc.fontRendererObj.getStringWidth("Accept"), 12, "Accept"));
        this.buttonList.add(new TextBtn(1, requestsList.xPosition + requestsList.width - 16, this.buttonList.get(0).yPosition, mc.fontRendererObj.getStringWidth("Deny"), 12, "Deny"));
        this.buttonList.add(new TextBtn(2, friendsList.xPosition + ((friendsList.width - 24) / 2), friendsList.yPosition + friendsList.height + 2, mc.fontRendererObj.getStringWidth("Remove"), 12, "Remove"));

        this.buttonList.get(0).enabled = false;
        this.buttonList.get(1).enabled = false;
        this.buttonList.get(2).enabled = false;
    }

    public static void setMessage(String message, int duration) {
        Friends.message[0] = message;
        Friends.message[1] = duration;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, duration);
        Friends.message[2] = calendar;
    }


}
