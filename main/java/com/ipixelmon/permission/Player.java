package com.ipixelmon.permission;

import com.ipixelmon.permission.Server.ServerProxy;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ListIterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

public class Player {

    private UUID player;

    protected Player(UUID uuid) {
        this.player = uuid;
    }

    public void setGroup(Group group) {
        JSONArray jsonArray = (JSONArray) ServerProxy.jsonObject.get("players");

        if (jsonArray == null) {
            // add players array and add player into it
            JSONArray newArray = new JSONArray();
            JSONObject playerObject = new JSONObject();
            playerObject.put("uuid", player.toString());
            playerObject.put("group", group.getName());
            newArray.add(playerObject);
            ServerProxy.jsonObject.put("players", newArray);
            ServerProxy.config.write(ServerProxy.jsonObject);
            return;
        }

        ListIterator listIterator = jsonArray.listIterator();

        while (listIterator.hasNext()) {
            JSONObject object = (JSONObject) listIterator.next();
            try {
                if(object.get("uuid") != null) {
                    if (UUID.fromString(String.valueOf(object.get("uuid"))).equals(player)) {
                        object.put("group", group.getName());
                        ServerProxy.config.write(ServerProxy.jsonObject);
                        return;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        JSONObject playerObject = new JSONObject();
        playerObject.put("uuid", player.toString());
        playerObject.put("group", group.getName());

        jsonArray.add(playerObject);
        ServerProxy.config.write(ServerProxy.jsonObject);

    }

    public boolean hasPermission(String permission) {
        Group g = getGroup();

        if(FMLCommonHandler.instance().getMinecraftServerInstance().canCommandSenderUseCommand(2, null)) return true;

        if(g == null) return false;
        if(g.getPermissions().contains("-" + permission)) return false;
        if(getPermissions().contains("-" + permission)) return false;
        if(g.getPermissions().contains(permission)) return true;
        if(getPermissions().contains(permission)) return true;
        return false;
    }

    public Set<String> getPermissions() {
        JSONArray jsonArray = (JSONArray) ServerProxy.jsonObject.get("players");

        if (jsonArray == null) return null;

        ListIterator listIterator = jsonArray.listIterator();

        Set<String> permSet = new TreeSet<>();

        while (listIterator.hasNext()) {
            JSONObject object = (JSONObject) listIterator.next();
            try {
                if(object.get("uuid") != null) {
                    if (UUID.fromString(String.valueOf(object.get("uuid"))).equals(player)) {
                        if(object.get("permissions") != null) {
                            JSONArray permArray = (JSONArray) object.get("permissions");

                            ListIterator permIterator = permArray.listIterator();

                            while (permIterator.hasNext()) {
                                permSet.add(String.valueOf(permIterator.next()));
                            }

                            break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return permSet;
    }

    public Group getGroup() {
        JSONArray jsonArray = (JSONArray) ServerProxy.jsonObject.get("players");

        if (jsonArray == null) return null;

        ListIterator listIterator = jsonArray.listIterator();

        while (listIterator.hasNext()) {
            JSONObject object = (JSONObject) listIterator.next();
            try {
                if(object.get("uuid") != null) {
                    if (UUID.fromString(String.valueOf(object.get("uuid"))).equals(player)) {
                        if(object.get("group") != null) {
                            return Group.getGroup(String.valueOf(object.get("group")));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public void addPermission(String permission) {
        JSONArray jsonArray = (JSONArray) ServerProxy.jsonObject.get("players");

        if (jsonArray == null) return;

        ListIterator listIterator = jsonArray.listIterator();

        while (listIterator.hasNext()) {
            JSONObject object = (JSONObject) listIterator.next();
            try {
                if(object.get("uuid") != null) {
                    if (UUID.fromString(String.valueOf(object.get("uuid"))).equals(player)) {
                        if(object.get("permissions") != null) {
                            JSONArray permArray = (JSONArray) object.get("permissions");
                            permArray.add(permission.toLowerCase());
                            break;
                        } else {
                            JSONArray permArray = new JSONArray();
                            permArray.add(permission.toLowerCase());
                            object.put("permissions", permArray);
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ServerProxy.config.write(ServerProxy.jsonObject);
    }

    public void removePermission(String permission) {
        JSONArray jsonArray = (JSONArray) ServerProxy.jsonObject.get("players");

        if (jsonArray == null) return;

        ListIterator listIterator = jsonArray.listIterator();

        while (listIterator.hasNext()) {
            JSONObject object = (JSONObject) listIterator.next();
            try {
                if(object.get("uuid") != null) {
                    if (UUID.fromString(String.valueOf(object.get("uuid"))).equals(player)) {
                        if(object.get("permissions") != null) {
                            JSONArray permArray = (JSONArray) object.get("permissions");
                            permArray.remove(permission.toLowerCase());
                            break;
                        } else {
                            JSONArray permArray = new JSONArray();
                            permArray.remove(permission.toLowerCase());
                            object.put("permissions", permArray);
                            break;
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        ServerProxy.config.write(ServerProxy.jsonObject);
    }
}
