package com.ipixelmon.gyms.client;

import com.ipixelmon.CommonProxy;
import com.ipixelmon.gyms.BlockGymInfo;
import com.ipixelmon.gyms.EntityGymLeader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit()
    {

    }

    @Override
    public void init()
    {
        RenderingRegistry.registerEntityRenderingHandler(EntityGymLeader.class, new RenderGymLeader(Minecraft.getMinecraft().getRenderManager()));
        Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(Item.getItemFromBlock(BlockGymInfo.instance), 0, new ModelResourceLocation("minecraft:iron_block", "inventory"));
    }
}
