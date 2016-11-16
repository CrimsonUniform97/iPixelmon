package com.ipixelmon.tablet.client;

import com.ipixelmon.CommonProxy;
import com.ipixelmon.tablet.client.apps.friends.Friends;
import com.ipixelmon.tablet.client.apps.mail.Mail;
import com.ipixelmon.tablet.client.apps.gallery.Gallery;
import com.ipixelmon.tablet.notification.NotificationOverlay;
import net.minecraftforge.common.MinecraftForge;

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

        new Gallery("Gallery");
        new Mail("Mail");
        new Friends("Friends");
    }

}
