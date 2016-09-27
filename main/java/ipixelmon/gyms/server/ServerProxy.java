package ipixelmon.gyms.server;

import ipixelmon.CommonProxy;
import ipixelmon.gyms.Gyms;
import ipixelmon.iPixelmon;
import ipixelmon.mysql.CreateForm;
import ipixelmon.mysql.DataType;

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
        gymsForm.add("prestige", DataType.INT);
        gymsForm.add("team", DataType.TEXT);
        gymsForm.add("pokemon", DataType.TEXT);
        iPixelmon.mysql.createTable(Gyms.class, gymsForm);
    }
}
