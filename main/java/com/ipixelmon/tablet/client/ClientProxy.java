package com.ipixelmon.tablet.client;

import com.ipixelmon.CommonProxy;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;

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
    }

}
