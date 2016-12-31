package com.ipixelmon.tablet.app.friends;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.mysql.CreateForm;
import com.ipixelmon.mysql.DataType;
import com.ipixelmon.tablet.AppProxy;
import com.ipixelmon.tablet.Tablet;

/**
 * Created by colby on 12/31/2016.
 */
public class ServerProxy extends AppProxy {

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
    }
}
