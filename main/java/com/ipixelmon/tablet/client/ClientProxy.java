package com.ipixelmon.tablet.client;

import com.ipixelmon.CommonProxy;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.client.apps.friends.Friends;
import com.ipixelmon.tablet.client.apps.mail.Mail;
import com.ipixelmon.tablet.client.apps.gallery.Gallery;
import com.ipixelmon.tablet.notification.NotificationOverlay;
import net.minecraftforge.common.MinecraftForge;

import java.sql.SQLException;

/**
 * Created by colbymchenry on 10/3/16.
 */
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {
        Gallery.populateWallpapers();
    }

    @Override
    public void init() {
        MinecraftForge.EVENT_BUS.register(NotificationOverlay.instance);
        MinecraftForge.EVENT_BUS.register(new KeyListener());

        try {
            iPixelmon.clientDb.query("CREATE TABLE IF NOT EXISTS tabletMessages (messageID TEXT, players TEXT, messages TEXT);");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        new Gallery("Gallery");
        new Mail("Mail");
        new Friends("Friends");
    }

}
