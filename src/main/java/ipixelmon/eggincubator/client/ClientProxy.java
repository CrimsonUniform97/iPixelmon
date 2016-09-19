package ipixelmon.eggincubator.client;

import ipixelmon.CommonProxy;
import ipixelmon.eggincubator.egg.EggItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.List;

public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit()
    {
        OBJLoader.instance.addDomain("ipixelmon");
        MinecraftForge.EVENT_BUS.register(new TestListener());
        EggItem.instance.initModel();
    }

    @Override
    public void init()
    {
    }
}
