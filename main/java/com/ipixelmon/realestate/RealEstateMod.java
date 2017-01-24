package com.ipixelmon.realestate;

import com.ipixelmon.CommonProxy;
import com.ipixelmon.IMod;
import com.ipixelmon.realestate.client.ClientProxy;
import com.ipixelmon.realestate.client.ForSaleSignStanding;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RealEstateMod implements IMod {

    @Override
    public String getID() {
        return "realestate";
    }

    public static ForSaleSignStanding saleSignStanding;

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        saleSignStanding = new ForSaleSignStanding();
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
