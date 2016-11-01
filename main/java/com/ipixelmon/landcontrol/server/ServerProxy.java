package com.ipixelmon.landcontrol.server;

import com.ipixelmon.mysql.DataType;
import com.ipixelmon.CommonProxy;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.LandControl;
import com.ipixelmon.landcontrol.Region;
import com.ipixelmon.mysql.CreateForm;
import com.ipixelmon.mysql.DataType;
import com.ipixelmon.mysql.SelectionForm;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.MinecraftForge;

import java.sql.ResultSet;

public class ServerProxy extends CommonProxy
{
    @Override
    public void preInit()
    {
        CreateForm regionsForm = new CreateForm("Regions");
        regionsForm.add("uuid", DataType.TEXT);
        regionsForm.add("owner", DataType.TEXT);
        regionsForm.add("members", DataType.TEXT);
        regionsForm.add("world", DataType.TEXT);
        regionsForm.add("xMin", DataType.INT);
        regionsForm.add("xMax", DataType.INT);
        regionsForm.add("zMin", DataType.INT);
        regionsForm.add("zMax", DataType.INT);
        iPixelmon.mysql.createTable(LandControl.class, regionsForm);
    }

    @Override
    public void init()
    {
        MinecraftForge.EVENT_BUS.register(new PlayerListener());
    }

    public World getWorld(String name)
    {
        for(WorldServer world : MinecraftServer.getServer().worldServers)
        {
            if(world.getWorldInfo().getWorldName().equals(name))
            {
                return world;
            }
        }

        return null;
    }
}
