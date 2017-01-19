package com.ipixelmon.util;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by colby on 1/7/2017.
 */
public class WorldUtil {

    @SideOnly(Side.SERVER)
    public static World getWorld(String name) {
        for(World world : FMLCommonHandler.instance().getMinecraftServerInstance().worldServers) {
            if(world.getWorldInfo().getWorldName().equalsIgnoreCase(name)) return world;
        }

        return null;
    }

}
