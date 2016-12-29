package com.ipixelmon.util;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by colbymchenry on 12/28/16.
 */
public final class Utils {

    public static final ArrayUtil array = new ArrayUtil();
    public static final ItemUtil item = new ItemUtil();
    public static final PlayerUtil player = new PlayerUtil();
    public static final TimeUtil time = new TimeUtil();

    @SideOnly(Side.CLIENT)
    public static class Client {
        @SideOnly(Side.CLIENT)
        public static final GuiUtil gui = new GuiUtil();
    }

}
