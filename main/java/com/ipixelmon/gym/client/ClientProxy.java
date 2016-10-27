package com.ipixelmon.gym.client;

import com.ipixelmon.CommonProxy;
import com.ipixelmon.gym.BlockGymInfo;
import com.ipixelmon.gym.EntityGymLeader;
import com.ipixelmon.gym.TileEntityGymInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.fml.client.registry.ClientRegistry;
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
        ForgeHooksClient.registerTESRItemStack(Item.getItemFromBlock(BlockGymInfo.instance), 0, TileEntityGymInfo.class);
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGymInfo.class, new GymInfoRenderer());
    }
}
