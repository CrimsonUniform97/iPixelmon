package com.ipixelmon.tablet.app.pixelbay;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.AppProxy;
import net.minecraft.util.ResourceLocation;

/**
 * Created by colby on 12/31/2016.
 */
public class ClientProxy extends AppProxy {

    @Override
    public void preInit() {

    }

    @Override
    public void init() {

    }

    @Override
    public Object getIcon() {
        return new ResourceLocation(iPixelmon.id, "textures/apps/pixelbay/icon.png");
    }

    @Override
    public Object getGuiForApp(Object... parameters) {
        return new PixelbayGui(parameters);
    }
}
