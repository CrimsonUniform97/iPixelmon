package com.ipixelmon.team.server;

import com.ipixelmon.mysql.DataType;
import com.ipixelmon.CommonProxy;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.mysql.CreateForm;
import com.ipixelmon.team.TeamMod;
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

        iPixelmon.mysql.createTable(TeamMod.class, playersForm);
        MinecraftForge.EVENT_BUS.register(new PlayerListener());
    }
}
