package com.ipixelmon.permission;

import com.google.common.collect.Lists;
import com.ipixelmon.permission.Server.ServerProxy;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;
import java.util.ListIterator;
import java.util.Set;
import java.util.TreeSet;

public class Group {

    private String name = null;
    private Set<String> permissions = new TreeSet<>();
    private Set<String> inheritance = new TreeSet<>();

    private boolean defaultGroup = false;
    private String prefix, suffix = "";

    protected Group(String name) throws Exception {
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

                    while (permissionIterator.hasNext()) {
                        String perm = String.valueOf(permissionIterator.next());
                        permissions.add(perm);
                    }
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

    public Set<String> getPermissions() {
        return getAllInheritedPermissions();
    }

    public Set<String> getInheritance() {
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

    public void addPermission(String permission) {
        JSONArray jsonArray = (JSONArray) ServerProxy.jsonObject.get("groups");
        ListIterator listIterator = jsonArray.listIterator();

        while (listIterator.hasNext()) {
            JSONObject object = (JSONObject) listIterator.next();

            if (object.get("name") != null && String.valueOf(object.get("name")).equals(name)) {
                if (object.get("permissions") != null) {
                    JSONArray permissionArray = (JSONArray) object.get("permissions");
                    permissionArray.add(permission.toLowerCase());
                }
            }
        }

        ServerProxy.config.write(ServerProxy.jsonObject);
    }

    public void removePermission(String permission) {
        JSONArray jsonArray = (JSONArray) ServerProxy.jsonObject.get("groups");
        ListIterator listIterator = jsonArray.listIterator();

        while (listIterator.hasNext()) {
            JSONObject object = (JSONObject) listIterator.next();

            if (object.get("name") != null && String.valueOf(object.get("name")).equals(name)) {
                if (object.get("permissions") != null) {
                    JSONArray permissionArray = (JSONArray) object.get("permissions");
                    permissionArray.remove(permission.toLowerCase());
                }
            }
        }

        ServerProxy.config.write(ServerProxy.jsonObject);
    }

    private Set<String> getAllInheritedPermissions() {
        Set<String> permissions = new TreeSet<>();
        permissions.addAll(this.permissions);

        for (String gName : getInheritance()) {
            permissions.addAll(getGroup(gName).permissions);
            permissions.addAll(getGroup(gName).getAllInheritedPermissions());
        }

        return permissions;
    }

    public static Group getGroup(String name) {
        for (Group group : getGroups()) if (group.getName().equalsIgnoreCase(name)) return group;
        return null;
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
}
