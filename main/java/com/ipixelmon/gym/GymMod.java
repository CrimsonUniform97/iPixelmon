package com.ipixelmon.gym;

import com.ipixelmon.CommonProxy;
import com.ipixelmon.IMod;
import com.ipixelmon.gym.client.ClientProxy;
import com.ipixelmon.gym.packet.*;
import com.ipixelmon.gym.server.BattleListenerThread;
import com.ipixelmon.gym.server.ServerProxy;
import com.ipixelmon.iPixelmon;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class GymMod implements IMod {

    @Override
    public String getID() {
        return "gym";
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        EntityRegistry.registerModEntity(EntityTrainer.class, "entityTrainer", 489, iPixelmon.instance, 80, 3, false);
        iPixelmon.registerPacket(PacketOpenGymGuiToClient.Handler.class, PacketOpenGymGuiToClient.class, Side.CLIENT);
        iPixelmon.registerPacket(PacketOpenGymGuiToServer.Handler.class, PacketOpenGymGuiToServer.class, Side.SERVER);
        iPixelmon.registerPacket(PacketClaimGymToServer.Handler.class, PacketClaimGymToServer.class, Side.SERVER);
        iPixelmon.registerPacket(EntityTrainerSyncPacket.Handler.class, EntityTrainerSyncPacket.class, Side.CLIENT);
        iPixelmon.registerPacket(PacketBattle.Handler.class, PacketBattle.class, Side.SERVER);
    }

    @Override
    public void init(FMLInitializationEvent event) {

    }

    @Override
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new GymCommand());
    }

    @Override
    public void serverStarted(FMLServerStartedEvent event) {
        new Thread(new BattleListenerThread()).start();
        GymAPI.Server.loadGyms();
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
