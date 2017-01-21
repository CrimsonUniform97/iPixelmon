package com.ipixelmon.permission.Server;

import com.google.common.collect.Lists;
import com.ipixelmon.CommonProxy;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.permission.Group;
import com.ipixelmon.permission.PermissionConfig;
import com.ipixelmon.permission.PermissionMod;
import org.json.simple.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class ServerProxy extends CommonProxy {

    public static final File configFile = new File(iPixelmon.path, "permissions.json");
    public static PermissionConfig config;

    public static JSONObject jsonObject = new JSONObject();

    @Override
    public void preInit() {
        if (!configFile.exists()) try {
            configFile.createNewFile();
            config = new PermissionConfig(configFile);
            config.write(jsonObject);
            return;
        } catch (IOException e) {
            e.printStackTrace();
        }

        config = new PermissionConfig(configFile);
        jsonObject = config.read();
    }

    @Override
    public void init() {
        Group g = PermissionMod.getGroup("admin");
        for(String s : g.getPermissions()) System.out.println(s);
    }

}
