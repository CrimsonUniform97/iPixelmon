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
        for(Group g : PermissionMod.getGroups()) {
            Set<String> perms = getAllInheritedPermissions(g);
            perms.addAll(g.getPermissions());
            System.out.println("\n" + g.getName());
            for(String s : perms) System.out.println(s);
        }
    }

    // TODO: Test out if one group inherits another group and that other group inherits the other group that inherited it.
    public Set<String> getAllInheritedPermissions(Group g) {
        Set<String> permissions = new TreeSet<>();

        for (String gName : g.getInheritance()) {
            permissions.addAll(PermissionMod.getGroup(gName).getPermissions());
            permissions.addAll(getAllInheritedPermissions(PermissionMod.getGroup(gName)));
        }

        return permissions;
    }

}
