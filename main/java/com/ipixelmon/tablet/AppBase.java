package com.ipixelmon.tablet;

/**
 * Created by colby on 12/31/2016.
 */
public interface AppBase {

    void preInit();

    void init();

    String getName();

    Class<?extends AppProxy> clientProxyClass();
    Class<?extends AppProxy> serverProxyClass();

}
