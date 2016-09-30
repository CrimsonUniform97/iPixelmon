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
        gymsForm.add("pokemon", DataType.TEXT);
        gymsForm.add("pokemon", DataType.TEXT);
        iPixelmon.mysql.createTable(Gyms.class, gymsForm);
    }
}
