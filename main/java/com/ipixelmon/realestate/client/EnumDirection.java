package com.ipixelmon.realestate.client;

import net.minecraft.util.IStringSerializable;

public enum EnumDirection implements IStringSerializable {

    NORTH(90), //good
    NORTH_EAST(45),//good
    EAST(0),//good
    SOUTH_EAST(315),//good
    SOUTH(270), // good
    SOUTH_WEST(225),//good
    WEST(180), // good
    NORTH_WEST(135);//good

    private int degree;

    EnumDirection(int degree) {
        this.degree = degree;
    }

    @Override
    public String getName() {
        return this.name().toLowerCase();
    }

    public int getDegree() {
        return degree;
    }
}
