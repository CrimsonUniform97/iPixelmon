package com.ipixelmon.gyms.server;

import com.ipixelmon.CommonProxy;
import com.ipixelmon.gyms.Gyms;
import com.ipixelmon.mysql.CreateForm;
import com.ipixelmon.mysql.DataType;
import com.ipixelmon.CommonProxy;
import com.ipixelmon.gyms.Gyms;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.mysql.CreateForm;
import com.ipixelmon.mysql.DataType;
import com.pixelmonmod.pixelmon.Pixelmon;
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
        CreateForm gymsForm = new CreateForm("Gyms");
        gymsForm.add("name", DataType.TEXT);
        gymsForm.add("regionID", DataType.TEXT);
        gymsForm.add("power", DataType.INT);
        gymsForm.add("team", DataType.TEXT);
        gymsForm.add("gymLeaders", DataType.TEXT);
        gymsForm.add("displayblocks", DataType.TEXT);
        iPixelmon.mysql.createTable(Gyms.class, gymsForm);

        PixelmonSendOutListener sendOutListener;

        Pixelmon.EVENT_BUS.register(sendOutListener = new PixelmonSendOutListener());
        MinecraftForge.EVENT_BUS.register(sendOutListener);
    }
}
