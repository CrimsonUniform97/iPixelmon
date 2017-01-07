package com.ipixelmon.landcontrol;

import com.ipixelmon.CommonProxy;

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
    }

}
