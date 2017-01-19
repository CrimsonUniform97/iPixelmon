package com.ipixelmon.mcstats.server;

import com.ipixelmon.CommonProxy;
import com.ipixelmon.Config;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.mcstats.GatherType;
import com.ipixelmon.mcstats.McStatsAPI;
import com.ipixelmon.mcstats.McStatsMod;
import com.ipixelmon.mysql.CreateForm;
import com.ipixelmon.mysql.DataType;
import net.minecraft.block.Block;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.io.File;
import java.util.Map;

/**
 * Created by colbymchenry on 12/28/16.
 */
public class ServerProxy extends CommonProxy {

    public static final Config config = new Config(new File(iPixelmon.path, "mcstats.txt"));

    @Override
    public void preInit() {
        setupConfig();
        CreateForm statsTable = new CreateForm("STATS").add("player", DataType.TEXT);

        for (GatherType gatherType : GatherType.values()) statsTable.add(gatherType.name(), DataType.LONG);

        iPixelmon.mysql.createTable(McStatsMod.class, statsTable);
    }

    @Override
    public void init() {
        MinecraftForge.EVENT_BUS.register(new PlayerListener());
    }

    private void setupConfig() {
        try {
            Map<String, String> allData = config.toMap();
            for (String key : allData.keySet()) {
                if (key.contains(".")) {
                    String[] data = key.split("\\.");
                    GatherType gatherType = GatherType.valueOf(data[0].toUpperCase());
                    Block block;
                    int meta = 0;

                    if (data[1].contains(":")) {
                        block = Block.getBlockFromName(data[1].split(":")[0].toLowerCase());
                        meta = Integer.parseInt(data[1].split(":")[1]);
                    } else {
                        block = Block.getBlockFromName(data[1].toLowerCase());
                    }

                    McStatsAPI.Server.expValueList.add(block, meta, Integer.parseInt(allData.get(key)), gatherType);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            FMLCommonHandler.instance().getMinecraftServerInstance().stopServer();
        }
    }

    // TODO: Use for wood breaking tree
//    public void breakBlock(World worldIn, BlockPos pos, IBlockState state)
//    {
//        int i = 4;
//        int j = i + 1;
//
//        if (worldIn.isAreaLoaded(pos.add(-j, -j, -j), pos.add(j, j, j)))
//        {
//            for (BlockPos blockpos : BlockPos.getAllInBox(pos.add(-i, -i, -i), pos.add(i, i, i)))
//            {
//                IBlockState iblockstate = worldIn.getBlockState(blockpos);
//
//                if (iblockstate.getBlock().isLeaves(worldIn, blockpos))
//                {
//                    iblockstate.getBlock().beginLeavesDecay(worldIn, blockpos);
//                }
//            }
//        }
//    }
}
