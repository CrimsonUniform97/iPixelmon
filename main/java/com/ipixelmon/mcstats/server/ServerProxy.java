package com.ipixelmon.mcstats.server;

import com.google.common.collect.Maps;
import com.ipixelmon.CommonProxy;
import com.ipixelmon.Config;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.mcstats.McStatsMod;
import com.ipixelmon.mysql.CreateForm;
import com.ipixelmon.mysql.DataType;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;

import java.io.File;
import java.util.Map;

/**
 * Created by colbymchenry on 12/28/16.
 */
public class ServerProxy extends CommonProxy {

    public static final Config config = new Config(new File(iPixelmon.path, "mcstats.txt"));

    public static Map<Integer, Integer> xpValues = Maps.newHashMap();

    @Override
    public void preInit() {
        setupConfig();
        iPixelmon.mysql.createTable(McStatsMod.class, new CreateForm("STATS")
                .add("player", DataType.TEXT).add("exp", DataType.LONG));
    }

    @Override
    public void init() {
        MinecraftForge.EVENT_BUS.register(new PlayerListener());
    }

    private void setupConfig() {
        Map<String, String> allData = config.toMap();

        Block block;
        for(String key : allData.keySet()) {
            block = Block.getBlockFromName(key);

            if(block != null) {
                xpValues.put(Block.getIdFromBlock(block), Integer.parseInt(allData.get(key)));
            }
        }
    }
}
