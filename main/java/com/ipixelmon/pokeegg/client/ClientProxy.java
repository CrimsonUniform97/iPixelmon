package com.ipixelmon.pokeegg.client;

import com.ipixelmon.CommonProxy;
import com.ipixelmon.pokeegg.egg.PokeEggItem;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit()
    {
        OBJLoader.instance.addDomain("ipixelmon");
        PokeEggItem.instance.initModel();
    }

    @Override
    public void init()
    {
    }
}
