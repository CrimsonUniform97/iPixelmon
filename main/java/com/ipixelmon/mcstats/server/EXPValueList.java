package com.ipixelmon.mcstats.server;

import com.ipixelmon.mcstats.GatherType;
import com.sun.glass.ui.Window;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by colbymchenry on 12/29/16.
 */
public class EXPValueList extends ArrayList {

    public boolean add(Block block, int meta, int exp, GatherType gatherType) {
        Object object[] = new Object[4];
        object[0] = block;
        object[1] = meta;
        object[2] = exp;
        object[3] = gatherType;
        Logger.getLogger("Minecraft").log(Level.INFO, "Registered " + block.getLocalizedName() + ":" + meta +
                " for " + exp + " EXP! Gather Type=" + gatherType.name());
        return super.add(object);
    }

    public int getEXP(IBlockState blockState) {
        Block block = blockState.getBlock();
        int meta = block.getMetaFromState(blockState);

        Block b;
        int m;
        for(Object o : this.toArray()) {
            Object[] objects = (Object[]) o;
            b = (Block) objects[0];
            m = (int) objects[1];

            if(b == block && m == meta) return (int) objects[2];
        }

        return 0;
    }

    public GatherType getGatherType(IBlockState blockState) {
        Block block = blockState.getBlock();
        int meta = block.getMetaFromState(blockState);

        Block b;
        int m;
        for(Object o : this.toArray()) {
            Object[] objects = (Object[]) o;
            b = (Block) objects[0];
            m = (int) objects[1];

            if(b == block && m == meta) return (GatherType) objects[3];
        }

        return null;
    }

}
