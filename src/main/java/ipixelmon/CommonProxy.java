package ipixelmon;

import ipixelmon.mysql.MySQLHandler;
import lib.PatPeter.SQLibrary.MySQL;

import java.io.File;
import java.util.logging.Logger;

import static ipixelmon.iPixelmon.MOD_ID;
import static ipixelmon.iPixelmon.config;

public abstract class CommonProxy {

    public abstract void preInit();
    public abstract void init();

    public final MySQLHandler getMySQL() {
        return new MySQLHandler(new MySQL(Logger.getLogger("Minecraft"), "[" + MOD_ID + "]", config.getString("dbHost"), config.getInt("dbPort"), config.getString("dbName"), config.getString("dbUser"), config.getString("dbPass")));
    }

    public final Config getConfig() {
        return new Config(new File(System.getProperty("user.dir") + "/" + MOD_ID + "/config.txt"));
    }

}
