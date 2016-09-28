package com.ipixelmon;

import net.minecraftforge.client.model.obj.OBJLoader;

import java.util.ArrayList;
import java.util.List;

import static com.ipixelmon.iPixelmon.config;

public final class ClientProxy extends CommonProxy {

    protected static final List<CommonProxy> proxies = new ArrayList<CommonProxy>();

    @Override
    public final void preInit() {
        OBJLoader.instance.addDomain(iPixelmon.id);

        try {
            for (IMod mod : iPixelmon.mods) if(mod.clientProxyClass() != null) proxies.add(mod.clientProxyClass().newInstance());
        }catch(Exception e) {
            e.printStackTrace();
        }

        for(CommonProxy proxy : proxies) proxy.preInit();

    }

    @Override
    public final void init() {
        for(CommonProxy proxy : proxies) proxy.init();

    }

}
