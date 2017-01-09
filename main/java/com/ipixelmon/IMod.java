package com.ipixelmon;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;

public interface IMod {

    String getID();

    void preInit(FMLPreInitializationEvent event);
    void init(FMLInitializationEvent event);
    void serverStarting(FMLServerStartingEvent event);
    void serverStarted(FMLServerStartedEvent event);

    Class<? extends CommonProxy> clientProxyClass();
    Class<? extends CommonProxy> serverProxyClass();

    IGuiHandler getGuiHandler();

}
