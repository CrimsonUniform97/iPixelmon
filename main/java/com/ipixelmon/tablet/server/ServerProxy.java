package com.ipixelmon.tablet.server;

import com.ipixelmon.CommonProxy;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.mysql.CreateForm;
import com.ipixelmon.mysql.DataType;
import com.ipixelmon.tablet.Tablet;

/**
 * Created by colbymchenry on 10/3/16.
 */
public class ServerProxy extends CommonProxy {
    @Override
    public void preInit() {

    }

    @Override
    public void init() {
        CreateForm createForm = new CreateForm("Friends");
        createForm.add("player", DataType.TEXT);
        createForm.add("friends", DataType.TEXT);

        iPixelmon.mysql.createTable(Tablet.class, createForm);
    }
}
