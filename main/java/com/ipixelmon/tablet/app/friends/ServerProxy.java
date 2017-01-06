package com.ipixelmon.tablet.app.friends;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.mysql.CreateForm;
import com.ipixelmon.mysql.DataType;
import com.ipixelmon.tablet.AppProxy;
import com.ipixelmon.tablet.Tablet;
import net.minecraftforge.common.MinecraftForge;

import java.sql.SQLException;

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

        try {
            iPixelmon.mysql.getDatabase().query("CREATE TABLE IF NOT EXISTS tabletFriendRequests (" +
                    "sender text NOT NULL, " +
                    "receiver text NOT NULL, " +
                    "sentDate datetime NOT NULL DEFAULT NOW());");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        MinecraftForge.EVENT_BUS.register(new PlayerListener());
    }
}
