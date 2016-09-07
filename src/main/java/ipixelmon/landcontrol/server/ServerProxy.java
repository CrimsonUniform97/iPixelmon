package ipixelmon.landcontrol.server;

import ipixelmon.CommonProxy;
import ipixelmon.iPixelmon;
import ipixelmon.landcontrol.LandControl;
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
        CreateForm regionsForm = new CreateForm("Regions");
        regionsForm.add("owner", DataType.TEXT);
        regionsForm.add("members", DataType.TEXT);
        regionsForm.add("world", DataType.TEXT);
        regionsForm.add("xMin", DataType.INT);
        regionsForm.add("xMax", DataType.INT);
        regionsForm.add("zMin", DataType.INT);
        regionsForm.add("zMax", DataType.INT);
        iPixelmon.mysql.createTable(LandControl.class, regionsForm);
    }
}
