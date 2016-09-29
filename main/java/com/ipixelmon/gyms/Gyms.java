package com.ipixelmon.gyms;

import com.ipixelmon.gyms.client.ClientProxy;
import com.ipixelmon.gyms.server.ServerProxy;
import com.ipixelmon.CommonProxy;
import com.ipixelmon.IMod;
import com.ipixelmon.gyms.server.CommandGym;
import com.ipixelmon.iPixelmon;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.registry.EntityRegistry;

public class Gyms implements IMod
{
    @Override
    public String getID()
    {
        return "gyms";
    }

    @Override
    public void preInit()
    {
        EntityRegistry.registerModEntity(EntityGymLeader.class, "entityGymLeader", 487, iPixelmon.instance, 80, 3, false);
    }

    @Override
    public void init()
    {

    }

    @Override
    public void serverStartingEvent(FMLServerStartingEvent event)
    {
        event.registerServerCommand(new CommandGym());
    }

    @Override
    public Class<? extends CommonProxy> clientProxyClass()
    {
        return ClientProxy.class;
    }

    @Override
    public Class<? extends CommonProxy> serverProxyClass()
    {
        return ServerProxy.class;
    }

    @Override
    public IGuiHandler getGuiHandler()
    {
        return null;
    }
}
