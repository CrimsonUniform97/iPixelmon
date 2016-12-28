package com.ipixelmon.tablet.apps.friends;


import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.apps.App;
import com.ipixelmon.uuidmanager.UUIDManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Mouse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Created by colby on 11/4/2016.
 */
public class Friends extends App {

    private static final Map<UUID, Object[]> friends = new HashMap<>();
    private static final Map<UUID, String> friendRequests = new HashMap<>();

    private GuiFriendsList friendsList;
    private GuiFriendRequestsList friendRequestsList;
    private GuiTextField playerName;

    public Friends(String name, boolean register) {
        super(name, register);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        int dWheel = Mouse.getDWheel();
        friendsList.draw(mouseX, mouseY, dWheel);
        friendRequestsList.draw(mouseX, mouseY, dWheel);
        playerName.drawTextBox();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        friendsList.keyTyped(typedChar, keyCode);
        friendRequestsList.keyTyped(typedChar, keyCode);
        playerName.textboxKeyTyped(typedChar, keyCode);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        playerName.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException {
        super.actionPerformed(button);
        if(playerName.getText().isEmpty()) return;

        iPixelmon.network.sendToServer(new PacketFriendRequestToServer(UUIDManager.getUUID(playerName.getText()), false));
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();

        int width = getScreenBounds().getWidth() / 2;
        width -= 3;
        int height = getScreenBounds().getHeight();
        height -= 30;
        friendsList = new GuiFriendsList(getScreenBounds().getX() + 2, getScreenBounds().getY() + 2, width, height);
        friendRequestsList = new GuiFriendRequestsList(friendsList.xPosition + friendsList.width + 2, friendsList.yPosition, width, height);
        playerName = new GuiTextField(0, mc.fontRendererObj, friendRequestsList.xPosition,
                friendRequestsList.yPosition + friendRequestsList.height + 3, 110, 20);

        this.buttonList.add(new GuiButton(1, playerName.xPosition + playerName.width + 4, playerName.yPosition, 50, 20, "Send"));
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        this.buttonList.get(0).enabled = !playerName.getText().isEmpty();
    }

    /**
     * Friend List handling
     */
    public static void addFriend(UUID id, String name, boolean online) {
        friends.remove(id);
        friends.put(id, new Object[]{name, online});
    }

    public static void removeFriend(UUID id) {
        friends.remove(id);
    }

    public static boolean isFriendOnline(UUID id) {
        return (boolean) friends.get(id)[1];
    }

    public static String getFriendName(UUID id) {
        return (String) friends.get(id)[0];
    }

    public static Set<UUID> getFriends() {
        return friends.keySet();
    }


    /**
     * Friend Request handling
     */
    public static void addFriendRequest(UUID id, String name) {
        friendRequests.put(id, name);
    }

    public static void removeFriendRequest(UUID id) {
        friendRequests.remove(id);
    }

    public static String getFriendRequestName(UUID id) {
        return friendRequests.get(id);
    }

    public static Set<UUID> getFriendRequests() {
        return friendRequests.keySet();
    }


}