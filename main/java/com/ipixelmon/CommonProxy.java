package com.ipixelmon;

import com.ipixelmon.mysql.MySQLHandler;
import lib.PatPeter.SQLibrary.MySQL;
import net.minecraft.world.World;

import java.io.File;
import java.util.logging.Logger;

import static com.ipixelmon.iPixelmon.id;
import static com.ipixelmon.iPixelmon.config;

public abstract class CommonProxy {

    public abstract void preInit();
    public abstract void init();

    public MySQLHandler getMySQL() {
       return null;
    }

    public final Config getConfig() {
        return new Config(new File(iPixelmon.path, "config.txt"));
    }

    public World getDefaultWorld() { return null; }

}
