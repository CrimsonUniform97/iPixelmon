package com.ipixelmon.tablet;

import com.ipixelmon.CommonProxy;
import com.ipixelmon.IMod;
import com.ipixelmon.tablet.client.ClientProxy;
import com.ipixelmon.tablet.server.ServerProxy;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;

/**
 * Created by colbymchenry on 10/3/16.
 */
public class Tablet implements IMod {
    @Override
    public String getID() {
        return  "tablet";
    }

    @Override
    public void preInit() {

    }

    @Override
    public void init() {

    }

    @Override
    public void serverStartingEvent(FMLServerStartingEvent event) {

    }

    @Override
    public Class<? extends CommonProxy> clientProxyClass() {
        return ClientProxy.class;
    }

    @Override
    public Class<? extends CommonProxy> serverProxyClass() {
        return ServerProxy.class;
    }

    @Override
    public IGuiHandler getGuiHandler() {
        return null;
    }
}
