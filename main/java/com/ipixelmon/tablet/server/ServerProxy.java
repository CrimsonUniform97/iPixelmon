package com.ipixelmon.tablet.server;

import com.ipixelmon.CommonProxy;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.mysql.CreateForm;
import com.ipixelmon.mysql.DataType;
import com.ipixelmon.tablet.Tablet;
import com.ipixelmon.tablet.client.apps.friends.PlayerJoinListener;
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

        CreateForm friendReqForm = new CreateForm("FriendReqs");
        friendReqForm.add("receiver", DataType.TEXT);
        friendReqForm.add("sender", DataType.TEXT);
        friendReqForm.add("sentDate", DataType.TEXT);
        iPixelmon.mysql.createTable(Tablet.class, friendReqForm);

        CreateForm mailForm = new CreateForm("mail");
        mailForm.add("receiver", DataType.TEXT);
        mailForm.add("sender", DataType.TEXT);
        mailForm.add("message", DataType.TEXT);
        mailForm.add("sentDate", DataType.TEXT);
        mailForm.add("hasRead", DataType.BOOLEAN);
        mailForm.add("mailID", DataType.TEXT);
        iPixelmon.mysql.createTable(Tablet.class, mailForm);

        MinecraftForge.EVENT_BUS.register(new PlayerJoinListener());
    }
}
