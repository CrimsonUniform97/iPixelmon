package com.ipixelmon.poketournament;

import com.ipixelmon.CommonProxy;
import com.ipixelmon.IMod;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.poketournament.client.ClientProxy;
import com.ipixelmon.poketournament.server.CommandHandler;
import com.ipixelmon.poketournament.server.PacketOpenTournamentGui;
import com.ipixelmon.poketournament.server.ServerProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;

public class PokeTournamentMod implements IMod {

    @Override
    public String getID() {
        return "poketournament";
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        iPixelmon.registerPacket(PacketOpenTournamentGui.Handler.class, PacketOpenTournamentGui.class, Side.CLIENT);
    }

    @Override
    public void init(FMLInitializationEvent event) {

    }

    @Override
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandHandler());
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
