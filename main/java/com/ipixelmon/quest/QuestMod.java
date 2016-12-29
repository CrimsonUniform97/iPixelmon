package com.ipixelmon.quest;

import com.ipixelmon.CommonProxy;
import com.ipixelmon.IMod;
import com.ipixelmon.quest.client.ClientProxy;
import com.ipixelmon.quest.server.ServerProxy;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;

/**
 * Created by colbymchenry on 12/28/16.
 */
public class QuestMod implements IMod {

    @Override
    public String getID() {
        return "quest";
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
        return ServerProxy.class;
    }

    @Override
    public IGuiHandler getGuiHandler() {
        return null;
    }
}
