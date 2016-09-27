package ipixelmon.landcontrol.server;

import ipixelmon.CommonProxy;
import ipixelmon.iPixelmon;
import ipixelmon.landcontrol.LandControl;
import ipixelmon.landcontrol.Region;
import ipixelmon.mysql.CreateForm;
import ipixelmon.mysql.DataType;
import ipixelmon.mysql.SelectionForm;
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

    }

    @Override
    public void init()
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
