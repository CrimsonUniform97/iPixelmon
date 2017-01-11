package com.ipixelmon.gym.server;

import com.ipixelmon.CommonProxy;
import com.ipixelmon.gym.GymAPI;
import com.ipixelmon.landcontrol.LandControlAPI;
import com.pixelmonmod.pixelmon.Pixelmon;
import net.minecraftforge.common.MinecraftForge;

public class ServerProxy extends CommonProxy{
    @Override
    public void preInit() {
        GymAPI.Server.initGymSQL();
    }

    @Override
    public void init() {
        PixelmonListener pixelmonListener = new PixelmonListener();
        Pixelmon.EVENT_BUS.register(pixelmonListener);
        MinecraftForge.EVENT_BUS.register(pixelmonListener);
        LandControlAPI.Server.EVENT_BUS.register(pixelmonListener);

    }
}
