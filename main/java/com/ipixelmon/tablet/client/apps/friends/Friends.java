package com.ipixelmon.tablet.client.apps.friends;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.client.App;
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

    private FriendsScrollingList scrollingList;
    private GuiTextField addFriendTxtField;
    public static Set<Friend> friends = new TreeSet<>();

    public Friends(String name) {
        super(name);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        scrollingList.drawScreen(mouseX, mouseY, partialTicks);
        addFriendTxtField.drawTextBox();
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

        if(keyCode == Keyboard.KEY_RETURN) {
            System.out.println("BOOM");
            iPixelmon.network.sendToServer(new PacketAddFriendReq(addFriendTxtField.getText()));
        }
    }

    @Override
    public void updateScreen() {
        super.updateScreen();
        addFriendTxtField.updateCursorCounter();
    }

    @Override
    public void initGui() {
        super.initGui();
        this.buttonList.clear();

        addFriendTxtField = new GuiTextField(0, fontRendererObj, 0, 0, 100, 20);

        scrollingList = new FriendsScrollingList(friends, mc,
                64, screenBounds.getHeight() - (14 * 2), screenBounds.getY() + 14,
                screenBounds.getY() + 14 + screenBounds.getHeight() - (14 * 2), screenBounds.getX() + 2, 10, width, height);
    }

}
