package com.ipixelmon.tablet.server;

import com.ipixelmon.CommonProxy;
import com.ipixelmon.tablet.AppBase;
import com.ipixelmon.tablet.Tablet;

/**
 * Created by colbymchenry on 10/3/16.
 */
public class ServerProxy extends CommonProxy {
    @Override
    public void preInit() {
        for (AppBase app : Tablet.apps) {
            if (app.serverProxyClass() != null) {
                try {
                    app.serverProxyClass().newInstance().preInit();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void init() {

        for (AppBase app : Tablet.apps) {
            if (app.serverProxyClass() != null) {
                try {
                    app.serverProxyClass().newInstance().init();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }


//
//        CreateForm messages = new CreateForm("mail");
//        messages.add("sentDate", DataType.TEXT);
//        messages.add("receiver", DataType.TEXT);
//        messages.add("sender", DataType.TEXT);
//        messages.add("message", DataType.TEXT);
//        iPixelmon.mysql.createTable(Tablet.class, messages);
//
//        Pixelbay.registerSQL();
//
//        MinecraftForge.EVENT_BUS.register(new PlayerListener());
//        MinecraftForge.EVENT_BUS.register(new com.ipixelmon.tablet.apps.friends.PlayerListener());
    }
}
