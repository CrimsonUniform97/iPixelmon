package com.ipixelmon.permission;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.UUID;

@SideOnly(Side.SERVER)
public class PermissionAPI {

    public static boolean hasPermission(UUID player, String permission) {
        return new Player(player).hasPermission(permission);
    }

}
