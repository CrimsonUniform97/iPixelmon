package com.ipixelmon.landcontrol.client;

import com.ipixelmon.CommonProxy;
import com.ipixelmon.landcontrol.toolCupboard.ToolCupboardBlock;
import com.ipixelmon.landcontrol.toolCupboard.ToolCupboardItem;
import net.minecraftforge.common.MinecraftForge;

/**
 * Created by colby on 1/6/2017.
 */
public class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {
    }

    @Override
    public void init() {
        ToolCupboardBlock.instance.initModel();
        ToolCupboardItem.instance.initModel();
        MinecraftForge.EVENT_BUS.register(new PlayerListener());
    }

}
