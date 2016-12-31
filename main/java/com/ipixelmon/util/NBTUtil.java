package com.ipixelmon.util;

import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Created by colby on 12/31/2016.
 */
public class NBTUtil {

    public static NBTTagCompound fromString(String str) {
        try {
            return JsonToNBT.getTagFromJson(str);
        } catch (NBTException e) {
            e.printStackTrace();
        }

        return null;
    }

}
