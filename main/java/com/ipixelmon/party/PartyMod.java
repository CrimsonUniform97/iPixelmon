package com.ipixelmon.party;

import com.google.common.collect.Maps;
import com.ipixelmon.CommonProxy;
import com.ipixelmon.IMod;
import com.ipixelmon.iPixelmon;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.*;

/**
 * Created by colby on 10/27/2016.
 */
public class PartyMod implements IMod {

    private static final Map<UUID, TreeSet<UUID>> parties = Maps.newHashMap();

    @Override
    public String getID() {
        return "party";
    }

    @Override
    public void preInit() {
        iPixelmon.registerPacket(PacketSendPartyInvite.Handler.class, PacketSendPartyInvite.class, Side.SERVER);
        iPixelmon.registerPacket(PacketReceivePartyInvite.Handler.class, PacketReceivePartyInvite.class, Side.CLIENT);
        iPixelmon.registerPacket(PacketAcceptPartyInvite.Handler.class, PacketAcceptPartyInvite.class, Side.SERVER);
        iPixelmon.registerPacket(PacketJoinedParty.Handler.class, PacketJoinedParty.class, Side.CLIENT);
        iPixelmon.registerPacket(PacketLeaveParty.Handler.class, PacketLeaveParty.class, Side.CLIENT);
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
