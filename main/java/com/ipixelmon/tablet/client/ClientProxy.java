package com.ipixelmon.tablet.client;

import com.ipixelmon.CommonProxy;
import com.ipixelmon.tablet.client.apps.camera.Gallery;
import com.ipixelmon.tablet.notification.NotificationOverlay;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by colbymchenry on 10/3/16.
 */
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {

    }

    @Override
    public void init() {
        MinecraftForge.EVENT_BUS.register(NotificationOverlay.instance);
        MinecraftForge.EVENT_BUS.register(new KeyListener());

        AppHandler.registerApp(new Gallery("Gallery1", "gallery"));
        AppHandler.registerApp(new App("Gallery2", "gallery") {
            @Override
            public void initGui() {
                super.initGui();
            }
        });
        AppHandler.registerApp(new App("Gallery3", "gallery") {
            @Override
            public void initGui() {
                super.initGui();
            }
        });
        AppHandler.registerApp(new App("Gallery4", "gallery") {
            @Override
            public void initGui() {
                super.initGui();
            }
        });
        AppHandler.registerApp(new App("Gallery5", "gallery") {
            @Override
            public void initGui() {
                super.initGui();
            }
        });
        AppHandler.registerApp(new App("Gallery6", "gallery") {
            @Override
            public void initGui() {
                super.initGui();
            }
        });
        AppHandler.registerApp(new App("Gallery7", "gallery") {
            @Override
            public void initGui() {
                super.initGui();
            }
        });
        AppHandler.registerApp(new App("Gallery8", "gallery") {
            @Override
            public void initGui() {
                super.initGui();
            }
        });
    }

}
