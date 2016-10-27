package com.ipixelmon.party;

import com.google.common.collect.Maps;
import com.ipixelmon.CommonProxy;
import com.ipixelmon.IMod;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by colby on 10/27/2016.
 */
public class PartyMod implements IMod {

    public static final Map<UUID, List<UUID>> parties = Maps.newHashMap();
    public static final Map<UUID, UUID> invites = Maps.newHashMap();

    @Override
    public String getID() {
        return "party";
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
        return com.ipixelmon.party.client.ClientProxy.class;
    }

    @Override
    public Class<? extends CommonProxy> serverProxyClass() {
        return com.ipixelmon.party.server.ServerProxy.class;
    }

    @Override
    public IGuiHandler getGuiHandler() {
        return null;
    }

}
