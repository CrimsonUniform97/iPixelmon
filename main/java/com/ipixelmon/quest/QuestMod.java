package com.ipixelmon.quest;

import com.ipixelmon.CommonProxy;
import com.ipixelmon.IMod;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.quest.client.ClientProxy;
import com.ipixelmon.quest.packet.PacketQuestInfoToClient;
import com.ipixelmon.quest.server.ServerProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by colbymchenry on 12/28/16.
 */
public class QuestMod implements IMod {

    @Override
    public String getID() {
        return "quest";
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        EntityRegistry.registerModEntity(EntityQuestGiver.class, "entityQuestGiver", 488, iPixelmon.instance,
                80, 3, false, 0xFFFFFF, 0x000000);
        iPixelmon.registerPacket(PacketQuestInfoToClient.Handler.class, PacketQuestInfoToClient.class, Side.CLIENT);
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
