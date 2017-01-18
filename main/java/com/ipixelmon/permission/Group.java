package com.ipixelmon.permission;

import com.google.common.collect.Lists;
import com.ipixelmon.permission.Server.ServerProxy;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;
import java.util.ListIterator;

public class Group {

    private String name = null;
    private List<String> permissions = Lists.newArrayList();
    private List<String> inheritance = Lists.newArrayList();

    private boolean defaultGroup = false;
    private String prefix, suffix = "";

    public Group(String name) throws Exception {
        JSONArray jsonArray = (JSONArray) ServerProxy.jsonObject.get("groups");
        ListIterator listIterator = jsonArray.listIterator();

        while (listIterator.hasNext()) {
            JSONObject object = (JSONObject) listIterator.next();

            if (object.get("name") != null && String.valueOf(object.get("name")).equals(name)) {
                this.name = name;
                if (object.get("prefix") != null)
                    prefix = String.valueOf(object.get("prefix")).replaceAll("&", String.valueOf('\u00a7'));
                if (object.get("suffix") != null)
                    suffix = String.valueOf(object.get("suffix")).replaceAll("&", String.valueOf('\u00a7'));
                if (object.get("default") != null)
                    defaultGroup = Boolean.valueOf(String.valueOf(object.get("default")));

                if (object.get("permissions") != null) {
                    JSONArray permissionArray = (JSONArray) object.get("permissions");
                    ListIterator permissionIterator = permissionArray.listIterator();

                    while (permissionIterator.hasNext()) permissions.add(String.valueOf(permissionIterator.next()));
                }

                if (object.get("inheritance") != null) {
                    JSONArray inheritanceArray = (JSONArray) object.get("inheritance");
                    ListIterator inheritanceIterator = inheritanceArray.listIterator();

                    while (inheritanceIterator.hasNext()) inheritance.add(String.valueOf(inheritanceIterator.next()));
                }

                break;
            }
        }

        if(this.name == null) throw new Exception("Group " + name + " does not exist.");
    }

    public String getName() {
        return name;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public List<String> getInheritance() {
        return inheritance;
    }

    public boolean isDefaultGroup() {
        return defaultGroup;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }
}
