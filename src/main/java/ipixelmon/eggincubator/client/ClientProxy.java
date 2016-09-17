package ipixelmon.eggincubator.client;

import ipixelmon.CommonProxy;
import ipixelmon.eggincubator.egg.EggBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy
{
    @Override
    public void preInit()
    {
        OBJLoader.instance.addDomain("ipixelmon");
    }

    @Override
    public void init()
    {
        MinecraftForge.EVENT_BUS.register(new TestListener());
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        renderItem.getItemModelMesher().register(Item.getItemFromBlock(EggBlock.instance), 0, new ModelResourceLocation("ipixelmon:egg", "inventory"));
    }
}
