package com.ipixelmon.mcstats.client;

import com.ipixelmon.CommonProxy;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by colbymchenry on 12/28/16.
 */
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {

    }

    @Override
    public void init() {
        MinecraftForge.EVENT_BUS.register(new PlayerListener());
    }
}
