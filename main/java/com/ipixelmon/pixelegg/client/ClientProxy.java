package com.ipixelmon.pixelegg.client;

import com.ipixelmon.CommonProxy;
import com.ipixelmon.pixelegg.egg.PixelEggItem;

public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit()
    {
        PixelEggItem.instance.initModel();
    }

    @Override
    public void init()
    {
    }
}
