package com.ipixelmon.quest;

import com.ipixelmon.CommonProxy;
import com.ipixelmon.IMod;
import com.ipixelmon.gym.EntityGymLeader;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.quest.client.ClientProxy;
import com.ipixelmon.quest.server.ServerProxy;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.registry.EntityRegistry;

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
        EntityRegistry.registerModEntity(EntityQuestGiver.class, "entityQuestGiver", 488, iPixelmon.instance,
                80, 3, false, 0xFFFFFF, 0x000000);
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
