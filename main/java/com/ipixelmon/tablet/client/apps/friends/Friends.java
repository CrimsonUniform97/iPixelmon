package com.ipixelmon.tablet.client.apps.friends;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.mysql.SelectionForm;
import com.ipixelmon.tablet.Tablet;
import com.ipixelmon.tablet.client.App;
import com.ipixelmon.tablet.client.apps.friends.packet.PacketAddFriendReq;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiTextField;
import org.lwjgl.input.Keyboard;

import java.io.IOException;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by colby on 11/4/2016.
 */
// TODO: Once this is done, finish up the mail app
public class Friends extends App {

    // TODO: Come up with a better icon
    // TODO: Make a friend request packet and system.
    // TODO: Need a list for friend requests

    private FriendsScrollingList friendsList;
    private FriendRequestsScrollingList requestsList;
    private GuiTextField addFriendTxtField;
    public static Set<Friend> friends = new TreeSet<>();
    public static Set<FriendRequest> requests;

    public Friends(String name) {
        super(name);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
//        friendsList.drawScreen(mouseX, mouseY, partialTicks);
        requestsList.drawScreen(mouseX, mouseY, partialTicks);
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

        if(keyCode == Keyboard.KEY_RETURN)
            iPixelmon.network.sendToServer(new PacketAddFriendReq(addFriendTxtField.getText()));
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

        if(requests == null) {
            requests = new TreeSet<>();
            populateFriendRequests();
        }

        System.out.println(requests.size());

        addFriendTxtField = new GuiTextField(0, fontRendererObj, 300, 0, 100, 20);

        friendsList = new FriendsScrollingList(friends, mc, 0, 0, 100, 100, 10, this);

        requestsList = new FriendRequestsScrollingList(requests, mc, 110, 0, 100, 100, 10, this);
    }

    public static void populateFriendRequests() {
        ResultSet result = iPixelmon.mysql.selectAllFrom(Tablet.class, new SelectionForm("FriendReqs").where("receiver", Minecraft.getMinecraft().thePlayer.getUniqueID().toString()));

        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            while(result.next()) {
                requests.add(new FriendRequest(UUID.fromString(result.getString("sender")), formatter.parse(result.getString("sentDate"))));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
