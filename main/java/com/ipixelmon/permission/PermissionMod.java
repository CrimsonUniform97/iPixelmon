package com.ipixelmon.permission;

import com.google.common.collect.Lists;
import com.ipixelmon.CommonProxy;
import com.ipixelmon.IMod;
import com.ipixelmon.permission.Client.ClientProxy;
import com.ipixelmon.permission.Server.ServerProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

public class PermissionMod implements IMod {

    @Override
    public String getID() {
        return "permission";
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {

    }

    @Override
    public void init(FMLInitializationEvent event) {

    }

    @Override
    public void serverStarting(FMLServerStartingEvent event) {

    }

    @Override
    public void serverStarted(FMLServerStartedEvent event) {

    }

    @Override
    public Class<? extends CommonProxy> clientProxyClass() {
        return ClientProxy.class;
    }

    @Override
    public Class<? extends CommonProxy> serverProxyClass() {
        return ServerProxy.class;
    }

    @Override
    public IGuiHandler getGuiHandler() {
        return null;
    }

    public static boolean hasPermission(UUID player) {
        return false;
    }

    public static List<String> getPermissions(UUID player) {
        return null;
    }

    public static List<Group> getGroups() {
        JSONArray jsonArray = (JSONArray) ServerProxy.jsonObject.get("groups");

        ListIterator listIterator = jsonArray.listIterator();

        List<Group> groups = Lists.newArrayList();

        while(listIterator.hasNext()) {
            JSONObject object = (JSONObject) listIterator.next();
            try {
                groups.add(new Group(String.valueOf(object.get("name"))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return groups;
    }

    public static String getGroup(UUID player) {
        return null;
    }
}
