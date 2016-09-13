package ipixelmon.teams.server;

import ipixelmon.CommonProxy;
import ipixelmon.iPixelmon;
import ipixelmon.mysql.CreateForm;
import ipixelmon.mysql.DataType;
import ipixelmon.teams.Teams;
import net.minecraftforge.common.MinecraftForge;

public class ServerProxy extends CommonProxy
{
    @Override
    public void preInit()
    {

    }

    @Override
    public void init()
    {
        CreateForm playersForm = new CreateForm("Players");
        playersForm.add("uuid", DataType.TEXT);
        playersForm.add("team", DataType.TEXT);

        iPixelmon.mysql.createTable(Teams.class, playersForm);
        MinecraftForge.EVENT_BUS.register(new PlayerListener());
    }
}
