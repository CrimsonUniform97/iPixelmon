package com.ipixelmon;

import com.ipixelmon.mysql.MySQLHandler;
import lib.PatPeter.SQLibrary.MySQL;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static com.ipixelmon.iPixelmon.config;
import static com.ipixelmon.iPixelmon.id;

public final class ServerProxy extends CommonProxy {

    protected static final List<CommonProxy> proxies = new ArrayList<CommonProxy>();

    @Override
    public final void preInit() {
        try {
            for (IMod mod : iPixelmon.mods) if(mod.serverProxyClass() != null) proxies.add(mod.serverProxyClass().newInstance());
        }catch(Exception e) {
            e.printStackTrace();
        }

        for(CommonProxy proxy : proxies) proxy.preInit();
    }

    @Override
    public final void init() {
        for(CommonProxy proxy : proxies) proxy.init();
    }

    @Override
    public World getDefaultWorld() {
        return FMLCommonHandler.instance().getMinecraftServerInstance().getEntityWorld();
    }

    @Override
    public MySQLHandler getMySQL() {
        if(!config.hasKey("dbHost")) config.setString("dbHost", "");
        if(!config.hasKey("dbPort")) config.setString("dbPort", "");
        if(!config.hasKey("dbName")) config.setString("dbName", "");
        if(!config.hasKey("dbUser")) config.setString("dbUser", "");
        if(!config.hasKey("dbPass")) config.setString("dbPass", "");
        return new MySQLHandler(new MySQL(Logger.getLogger("Minecraft"), "[" + id + "]", config.getString("dbHost"), config.getInt("dbPort"), config.getString("dbName"), config.getString("dbUser"), config.getString("dbPass")));
    }
}
