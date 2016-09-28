package com.ipixelmon;

import java.util.ArrayList;
import java.util.List;

public final class ServerProxy extends CommonProxy {

    protected static final List<CommonProxy> proxies = new ArrayList<CommonProxy>();

    @Override
    public final void preInit() {
        try {
            for (IMod mod : iPixelmon.mods) if(mod.serverProxyClass() != null) proxies.add(mod.serverProxyClass().newInstance());
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
