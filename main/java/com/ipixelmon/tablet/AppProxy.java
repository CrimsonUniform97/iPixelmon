package com.ipixelmon.tablet;

/**
 * Created by colby on 12/31/2016.
 */
public abstract class AppProxy {

    public abstract void preInit();
    public abstract void init();
    public Object getIcon() { return null; }
    public Object getGuiForApp(Object... parameters) {return null;}

}
