package com.ipixelmon.poketournament.server;

import com.ipixelmon.CommonProxy;
import com.pixelmonmod.pixelmon.Pixelmon;

public class ServerProxy extends CommonProxy {
    @Override
    public void preInit() {

    }

    @Override
    public void init() {
        Pixelmon.EVENT_BUS.register(new BattleListener());
    }
}
