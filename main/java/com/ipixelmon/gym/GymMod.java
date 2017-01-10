package com.ipixelmon.gym;

import com.ipixelmon.CommonProxy;
import com.ipixelmon.IMod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class GymMod implements IMod {

    @Override
    public String getID() {
        return "gym";
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {

    }

    @Override
    public void init(FMLInitializationEvent event) {

    }

    @Override
    public void serverStarting(FMLServerStartingEvent event) {

    }

    @Override
    public void serverStarted(FMLServerStartedEvent event) {

    }

    @Override
    public Class<? extends CommonProxy> clientProxyClass() {
        return null;
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
