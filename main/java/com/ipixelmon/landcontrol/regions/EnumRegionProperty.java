package com.ipixelmon.landcontrol.regions;

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

    public static EnumRegionProperty fromOrdinal(int i) {
        return EnumRegionProperty.values()[i];
    }

}
