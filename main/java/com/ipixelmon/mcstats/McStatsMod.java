package com.ipixelmon.mcstats;

import com.ipixelmon.CommonProxy;
import com.ipixelmon.IMod;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.mcstats.client.ClientProxy;
import com.ipixelmon.mcstats.server.ServerProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by colbymchenry on 12/28/16.
 */
public class McStatsMod implements IMod {



    @Override
    public String getID() {
        return "mcstats";
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        iPixelmon.registerPacket(PacketUpdateEXP.Handler.class, PacketUpdateEXP.class, Side.CLIENT);
        iPixelmon.registerPacket(PacketBrokeBlock.Handler.class, PacketBrokeBlock.class, Side.CLIENT);
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
        return ServerProxy.class;
    }

    @Override
    public IGuiHandler getGuiHandler() {
        return null;
    }

}
