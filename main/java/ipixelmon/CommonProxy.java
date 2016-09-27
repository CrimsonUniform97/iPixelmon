package ipixelmon;

import ipixelmon.mysql.MySQLHandler;
import lib.PatPeter.SQLibrary.MySQL;

import java.io.File;
import java.util.logging.Logger;

import static ipixelmon.iPixelmon.id;
import static ipixelmon.iPixelmon.config;

public abstract class CommonProxy {

    public abstract void preInit();
    public abstract void init();

    public final MySQLHandler getMySQL() {
        if(!config.hasKey("dbHost")) config.setString("dbHost", "");
        if(!config.hasKey("dbPort")) config.setString("dbPort", "");
        if(!config.hasKey("dbName")) config.setString("dbName", "");
        if(!config.hasKey("dbUser")) config.setString("dbUser", "");
        if(!config.hasKey("dbPass")) config.setString("dbPass", "");
         return new MySQLHandler(new MySQL(Logger.getLogger("Minecraft"), "[" + id + "]", config.getString("dbHost"), config.getInt("dbPort"), config.getString("dbName"), config.getString("dbUser"), config.getString("dbPass")));
    }

    public final Config getConfig() {
        return new Config(new File(System.getProperty("user.dir") + "/" + id + "/config.txt"));
    }

}
