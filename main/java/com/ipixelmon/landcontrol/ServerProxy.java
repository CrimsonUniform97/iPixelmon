package com.ipixelmon.landcontrol;

import com.ipixelmon.CommonProxy;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by colby on 1/6/2017.
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
