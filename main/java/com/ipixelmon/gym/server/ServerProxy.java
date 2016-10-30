package com.ipixelmon.gym.server;

import com.ipixelmon.CommonProxy;
import com.ipixelmon.gym.GymMod;
import com.ipixelmon.mysql.CreateForm;
import com.ipixelmon.mysql.DataType;
import com.ipixelmon.iPixelmon;
import com.pixelmonmod.pixelmon.Pixelmon;
import net.minecraftforge.common.MinecraftForge;

public class ServerProxy extends CommonProxy {
    @Override
    public void preInit() {
        CreateForm gymsForm = new CreateForm("Gyms");
        gymsForm.add("region", DataType.TEXT);
        gymsForm.add("data", DataType.TEXT);
        gymsForm.add("seats", DataType.TEXT);
        gymsForm.add("gymLeaders", DataType.TEXT);
        iPixelmon.mysql.createTable(GymMod.class, gymsForm);
    }

    @Override
    public void init() {
        PixelmonListener sendOutListener;

        Pixelmon.EVENT_BUS.register(sendOutListener = new PixelmonListener());
        MinecraftForge.EVENT_BUS.register(sendOutListener);
    }

}
