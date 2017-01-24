package com.ipixelmon.realestate.client;

import com.ipixelmon.CommonProxy;
import com.ipixelmon.realestate.RealEstateMod;

public class ClientProxy extends CommonProxy {
    @Override
    public void preInit() {

    }

    @Override
    public void init() {
        RealEstateMod.saleSignStanding.initModel();
    }
}
