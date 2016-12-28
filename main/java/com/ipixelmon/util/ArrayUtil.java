package com.ipixelmon.util;

import java.util.Arrays;

/**
 * Created by colbymchenry on 12/27/16.
 */
public class ArrayUtil {

    public String toString(String[] objects) {
        return Arrays.toString(objects);
    }

    public String[] fromString(String string) {
        String[] strings = string.replace("[", "").replace("]", "").split(", ");
        String result[] = new String[strings.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = strings[i];
        }
        return result;
    }

}
