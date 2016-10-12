package com.ipixelmon.pixelegg;

import com.ipixelmon.CommonProxy;
import net.minecraftforge.common.MinecraftForge;

public class ServerProxy extends CommonProxy
{
    @Override
    public void preInit()
    {

    }

    @Override
    public void init()
    {
        MinecraftForge.EVENT_BUS.register(new PlayerTickHandler());
    }
}
