package com.ipixelmon;

import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;

public interface IMod {

    String getID();

    void preInit();
    void init();
    void serverStarting(FMLServerStartingEvent event);
    void serverStarted(FMLServerStartedEvent event);

    Class<? extends CommonProxy> clientProxyClass();
    Class<? extends CommonProxy> serverProxyClass();

    IGuiHandler getGuiHandler();

}
