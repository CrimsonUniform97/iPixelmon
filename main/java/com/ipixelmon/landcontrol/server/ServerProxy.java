package com.ipixelmon.landcontrol.server;

import com.ipixelmon.CommonProxy;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.landcontrol.LandControl;
import com.ipixelmon.mysql.CreateForm;
import com.ipixelmon.mysql.DataType;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by colby on 1/6/2017.
 */
public class ServerProxy extends CommonProxy {

    @Override
    public void preInit() {

    }

    @Override
    public void init() {
        MinecraftForge.EVENT_BUS.register(new PlayerListener());

        CreateForm networksForm = new CreateForm("Networks");
        networksForm.add("player", DataType.TEXT);
        networksForm.add("players", DataType.TEXT);
        iPixelmon.mysql.createTable(LandControl.class, networksForm);
    }
}
