package com.ipixelmon.quest.client;

import com.google.common.collect.Lists;
import com.ipixelmon.CommonProxy;
import com.ipixelmon.quest.EntityQuestGiver;
import com.ipixelmon.quest.Quest;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import java.util.List;

/**
 * Created by colbymchenry on 12/28/16.
 */
public class ClientProxy extends CommonProxy {

    public static List<Quest> activeQuests = Lists.newArrayList();

    @Override
    public void preInit() {
    }

    @Override
    public void init() {
        RenderingRegistry.registerEntityRenderingHandler(EntityQuestGiver.class, new RenderQuestGiver());
    }
}
