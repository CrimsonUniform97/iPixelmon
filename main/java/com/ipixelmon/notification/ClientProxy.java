package com.ipixelmon.notification;

import com.ipixelmon.CommonProxy;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by colby on 12/31/2016.
 */
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit() {

    }

    @Override
    public void init() {
        MinecraftForge.EVENT_BUS.register(NotificationOverlay.instance);
    }
}
