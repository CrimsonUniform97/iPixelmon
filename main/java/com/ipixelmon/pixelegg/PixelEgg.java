package com.ipixelmon.pixelegg;

import com.ipixelmon.pixelegg.client.ClientProxy;
import com.ipixelmon.CommonProxy;
import com.ipixelmon.IMod;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.pixelegg.egg.PixelEggItem;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class PixelEgg implements IMod
{

    @Override
    public String getID()
    {
        return "pixelegg";
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        GameRegistry.registerItem(PixelEggItem.instance);
    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        iPixelmon.registerPacket(PacketOpenGuiPixelEgg.Handler.class, PacketOpenGuiPixelEgg.class, Side.CLIENT);
    }

    @Override
    public void serverStarting(FMLServerStartingEvent event)
    {

    }

    @Override
    public void serverStarted(FMLServerStartedEvent event) {

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
