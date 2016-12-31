package com.ipixelmon.notification;

import com.ipixelmon.CommonProxy;
import com.ipixelmon.IMod;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;

/**
 * Created by colby on 12/31/2016.
 */
public class NotificationMod implements IMod {

    @Override
    public String getID() {
        return "Notification";
    }

    @Override
    public void preInit() {

    }

    @Override
    public void init() {

    }

    @Override
    public void serverStarting(FMLServerStartingEvent event) {

    }

    @Override
    public void serverStarted(FMLServerStartedEvent event) {

    }

    @Override
    public Class<? extends CommonProxy> clientProxyClass() {
        return ClientProxy.class;
    }

    @Override
    public Class<? extends CommonProxy> serverProxyClass() {
        return null;
    }

    @Override
    public IGuiHandler getGuiHandler() {
        return null;
    }
}
