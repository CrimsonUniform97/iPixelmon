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

import java.util.*;

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

    public static boolean hasPermission(UUID player, String permission) {
        Group g = getGroup(player);

        if(g.getPermissions().contains("-" + permission)) return false;
        if(getPlayerPermissions(player).contains("-" + permission)) return false;
        if(g.getPermissions().contains(permission)) return true;
        if(getPlayerPermissions(player).contains(permission)) return true;
        return false;
    }

    public static Set<String> getPlayerPermissions(UUID player) {
        JSONArray jsonArray = (JSONArray) ServerProxy.jsonObject.get("players");

        if (jsonArray == null) return null;

        ListIterator listIterator = jsonArray.listIterator();

        Set<String> permSet = new TreeSet<>();

        while (listIterator.hasNext()) {
            JSONObject object = (JSONObject) listIterator.next();
            try {
                if (UUID.fromString(String.valueOf(object.get("uuid"))).equals(player)) {
                    JSONArray permArray = (JSONArray) object.get("permissions");

                    ListIterator permIterator = permArray.listIterator();

                    while(permIterator.hasNext()) {
                        permSet.add(String.valueOf(permIterator.next()));
                    }

                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return permSet;
    }

    public static List<Group> getGroups() {
        JSONArray jsonArray = (JSONArray) ServerProxy.jsonObject.get("groups");

        List<Group> groups = Lists.newArrayList();

        if (jsonArray == null) return groups;

        ListIterator listIterator = jsonArray.listIterator();

        while (listIterator.hasNext()) {
            JSONObject object = (JSONObject) listIterator.next();
            try {
                groups.add(new Group(String.valueOf(object.get("name"))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return groups;
    }

    public static Group getGroup(UUID player) {
        JSONArray jsonArray = (JSONArray) ServerProxy.jsonObject.get("players");

        if (jsonArray == null) return null;

        ListIterator listIterator = jsonArray.listIterator();

        while (listIterator.hasNext()) {
            JSONObject object = (JSONObject) listIterator.next();
            try {
                if (UUID.fromString(String.valueOf(object.get("uuid"))).equals(player)) {
                    return getGroup(String.valueOf(object.get("group")));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static Group getGroup(String name) {
        for (Group group : getGroups()) if (group.getName().equalsIgnoreCase(name)) return group;
        return null;
    }


}
