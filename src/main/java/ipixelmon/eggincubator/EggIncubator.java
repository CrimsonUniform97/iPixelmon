package ipixelmon.eggincubator;

import ipixelmon.CommonProxy;
import ipixelmon.IMod;
import ipixelmon.eggincubator.client.ClientProxy;
import ipixelmon.eggincubator.egg.EggBlock;
import ipixelmon.eggincubator.egg.EggItemBlock;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class EggIncubator implements IMod
{

    @Override
    public String getID()
    {
        return "eggincubator";
    }

    @Override
    public void preInit()
    {
        GameRegistry.registerBlock(EggBlock.instance, EggItemBlock.class, EggBlock.name);
    }

    @Override
    public void init()
    {
    }

    @Override
    public void serverStartingEvent(FMLServerStartingEvent event)
    {

    }

    @Override
    public Class<? extends CommonProxy> clientProxyClass()
    {
        return ipixelmon.eggincubator.client.ClientProxy.class;
    }

    @Override
    public Class<? extends CommonProxy> serverProxyClass()
    {
        return ipixelmon.eggincubator.ServerProxy.class;
    }

    @Override
    public IGuiHandler getGuiHandler()
    {
        return null;
    }
}
