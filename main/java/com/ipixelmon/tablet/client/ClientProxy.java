package com.ipixelmon.tablet.client;

import com.ipixelmon.CommonProxy;
import com.ipixelmon.notification.NotificationOverlay;
import com.ipixelmon.tablet.AppBase;
import com.ipixelmon.tablet.Tablet;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by colbymchenry on 10/3/16.
 */
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {
        for (AppBase app : Tablet.apps) {
            if (app.clientProxyClass() != null) {
                try {
                    app.clientProxyClass().newInstance().preInit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void init() {
        for (AppBase app : Tablet.apps) {
            if (app.clientProxyClass() != null) {
                try {
                    app.clientProxyClass().newInstance().init();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        MinecraftForge.EVENT_BUS.register(new KeyListener());
        // TODO
//
//        try {
//            iPixelmon.clientDb.query("CREATE TABLE IF NOT EXISTS tabletMail (sentDate TEXT, sender TEXT, message TEXT, read BOOLEAN);");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
    }

}
