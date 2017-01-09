package com.ipixelmon.landcontrol.server.regions;

/**
 * Created by colby on 1/8/2017.
 */
public enum EnumRegionProperty {

    placeBlocks, breakBlocks, interact, chestAccess, spawnMobs, spawnPixelmon, invincible, pvp;

    public static boolean contains(String test) {

        for (EnumRegionProperty c : EnumRegionProperty.values()) {
            if (c.name().equals(test)) {
                return true;
            }
        }

        return false;
    }

}
