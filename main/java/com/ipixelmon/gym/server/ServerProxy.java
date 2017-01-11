package com.ipixelmon.gym.server;

import com.ipixelmon.CommonProxy;
import com.ipixelmon.gym.GymAPI;
import com.pixelmonmod.pixelmon.Pixelmon;
import net.minecraftforge.common.MinecraftForge;

public class ServerProxy extends CommonProxy{
    @Override
    public void preInit() {
        GymAPI.Server.initGymSQL();
    }

    @Override
    public void init() {
        Pixelmon.EVENT_BUS.register(new PixelmonListener());
        MinecraftForge.EVENT_BUS.register(new PixelmonListener());
    }
}
