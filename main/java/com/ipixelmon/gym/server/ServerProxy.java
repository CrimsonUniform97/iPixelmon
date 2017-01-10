package com.ipixelmon.gym.server;

import com.ipixelmon.CommonProxy;
import com.ipixelmon.gym.GymAPI;
import com.pixelmonmod.pixelmon.Pixelmon;

/**
 * Created by colby on 1/10/2017.
 */
public class ServerProxy extends CommonProxy{
    @Override
    public void preInit() {
        GymAPI.Server.initGymSQL();
    }

    @Override
    public void init() {
        Pixelmon.EVENT_BUS.register(new PixelmonListener());
    }
}
