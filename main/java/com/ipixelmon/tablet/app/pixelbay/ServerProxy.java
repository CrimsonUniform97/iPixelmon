package com.ipixelmon.tablet.app.pixelbay;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.mysql.CreateForm;
import com.ipixelmon.mysql.DataType;
import com.ipixelmon.tablet.AppProxy;
import com.ipixelmon.tablet.Tablet;

/**
 * Created by colby on 12/31/2016.
 */
public class ServerProxy extends AppProxy {
    @Override
    public void preInit() {

    }

    @Override
    public void init() {
        CreateForm itemForm = new CreateForm("Items");
        itemForm.add("player", DataType.TEXT);
        itemForm.add("price", DataType.LONG);
        itemForm.add("item", DataType.TEXT);
        iPixelmon.mysql.createTable(Tablet.class, itemForm);

        CreateForm pixelmonForm = new CreateForm("Pixelmon");
        pixelmonForm.add("player", DataType.TEXT);
        pixelmonForm.add("price", DataType.LONG);
        pixelmonForm.add("pixelmonName", DataType.TEXT);
        pixelmonForm.add("pixelmonData", DataType.TEXT);
        iPixelmon.mysql.createTable(Tablet.class, pixelmonForm);
    }
}
