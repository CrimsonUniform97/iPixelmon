package com.ipixelmon.gym.client;

import com.ipixelmon.CommonProxy;
import com.ipixelmon.gym.EntityTrainer;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

/**
 * Created by colby on 1/10/2017.
 */
public class ClientProxy extends CommonProxy {
    @Override
    public void preInit() {

    }

    @Override
    public void init() {
        KeyListener.registerKeyBindings();
        RenderingRegistry.registerEntityRenderingHandler(EntityTrainer.class, EntityTrainerRenderer::new);
    }
}
