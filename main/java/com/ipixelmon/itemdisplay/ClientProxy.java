package com.ipixelmon.itemdisplay;

import com.ipixelmon.CommonProxy;
import net.minecraftforge.fml.client.registry.ClientRegistry;

public class ClientProxy extends CommonProxy {

    @Override
    public void preInit() {

    }

    @Override
    public void init() {
        ClientRegistry.bindTileEntitySpecialRenderer(ItemDisplayBlockTileEntity.class, new ItemDisplayBlockRenderer());
    }

}
