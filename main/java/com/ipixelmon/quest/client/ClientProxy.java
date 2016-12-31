package com.ipixelmon.quest.client;

import com.ipixelmon.CommonProxy;
import com.ipixelmon.quest.EntityQuestGiver;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

/**
 * Created by colbymchenry on 12/28/16.
 */
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {
    }

    @Override
    public void init() {
        RenderingRegistry.registerEntityRenderingHandler(EntityQuestGiver.class, new RenderQuestGiver());
    }
}
