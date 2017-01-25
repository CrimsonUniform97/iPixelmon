package com.ipixelmon;

import com.ipixelmon.util.SkinUtil;
import lib.PatPeter.SQLibrary.SQLite;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import org.apache.commons.io.FileUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public final class ClientProxy extends CommonProxy {

    protected static final List<CommonProxy> proxies = new ArrayList<CommonProxy>();

    @Override
    public final void preInit() {
//        SkinUtil.skinCacheDir.delete();
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(HiddenBlock.instance), 0 , new ModelResourceLocation(HiddenBlock.instance.getRegistryName(), "inventory"));

        OBJLoader.INSTANCE.addDomain(iPixelmon.id);

        try {
            for (IMod mod : iPixelmon.mods) if(mod.clientProxyClass() != null) proxies.add(mod.clientProxyClass().newInstance());
        }catch(Exception e) {
            e.printStackTrace();
        }

        for(CommonProxy proxy : proxies) proxy.preInit();

    }

    @Override
    public final void init() {


        for(CommonProxy proxy : proxies) proxy.init();

    }

    @Override
    public World getDefaultWorld() {
        return Minecraft.getMinecraft().theWorld;
    }
}
