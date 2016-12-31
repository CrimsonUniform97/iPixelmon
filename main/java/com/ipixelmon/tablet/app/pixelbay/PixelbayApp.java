package com.ipixelmon.tablet.app.pixelbay;

import com.ipixelmon.tablet.AppBase;
import com.ipixelmon.tablet.AppProxy;

/**
 * Created by colby on 12/31/2016.
 */
public class PixelbayApp implements AppBase {

    @Override
    public void preInit() {

    }

    @Override
    public void init() {

    }

    @Override
    public String getName() {
        return "Pixelbay";
    }

    @Override
    public Class<? extends AppProxy> clientProxyClass() {
        return ClientProxy.class;
    }

    @Override
    public Class<? extends AppProxy> serverProxyClass() {
        return ServerProxy.class;
    }
}
