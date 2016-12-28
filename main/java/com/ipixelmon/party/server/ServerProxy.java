package com.ipixelmon.party.server;

import com.ipixelmon.CommonProxy;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by colby on 10/27/2016.
 */
public class ServerProxy extends CommonProxy {
    @Override
    public void preInit() {

    }

    @Override
    public void init() {
        MinecraftForge.EVENT_BUS.register(new PlayerListener());
    }
}
