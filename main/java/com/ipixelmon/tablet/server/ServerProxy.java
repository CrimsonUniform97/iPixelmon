package com.ipixelmon.tablet.server;

import com.ipixelmon.CommonProxy;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.mysql.CreateForm;
import com.ipixelmon.mysql.DataType;
import com.ipixelmon.tablet.Tablet;
import com.ipixelmon.tablet.apps.mail.PlayerListener;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by colbymchenry on 10/3/16.
 */
public class ServerProxy extends CommonProxy {
    @Override
    public void preInit() {

    }

    @Override
    public void init() {
        CreateForm friendsForm = new CreateForm("Friends");
        friendsForm.add("player", DataType.TEXT);
        friendsForm.add("friends", DataType.TEXT);
        iPixelmon.mysql.createTable(Tablet.class, friendsForm);

        CreateForm friendReqForm = new CreateForm("FriendRequests");
        friendReqForm.add("receiver", DataType.TEXT);
        friendReqForm.add("sender", DataType.TEXT);
        friendReqForm.add("sentDate", DataType.TEXT);
        iPixelmon.mysql.createTable(Tablet.class, friendReqForm);

        CreateForm messages = new CreateForm("mail");
        messages.add("sentDate", DataType.TEXT);
        messages.add("receiver", DataType.TEXT);
        messages.add("sender", DataType.TEXT);
        messages.add("message", DataType.TEXT);
        iPixelmon.mysql.createTable(Tablet.class, messages);

        MinecraftForge.EVENT_BUS.register(new PlayerListener());
        MinecraftForge.EVENT_BUS.register(new com.ipixelmon.tablet.apps.friends.PlayerListener());
    }
}
