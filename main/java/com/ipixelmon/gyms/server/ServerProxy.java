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
        CreateForm gymsForm = new CreateForm("Gyms");
        gymsForm.add("region", DataType.TEXT);
        gymsForm.add("data", DataType.TEXT);
        gymsForm.add("seats", DataType.TEXT);
        gymsForm.add("gymLeaders", DataType.TEXT);
        iPixelmon.mysql.createTable(Gyms.class, gymsForm);
    }

    @Override
    public void init()
    {
        PixelmonSendOutListener sendOutListener;

        Pixelmon.EVENT_BUS.register(sendOutListener = new PixelmonSendOutListener());
        MinecraftForge.EVENT_BUS.register(sendOutListener);
    }
}
