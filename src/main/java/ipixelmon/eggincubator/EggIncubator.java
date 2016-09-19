package ipixelmon.eggincubator;

import ipixelmon.CommonProxy;
import ipixelmon.IMod;
import ipixelmon.eggincubator.egg.EggItem;
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
        GameRegistry.registerItem(EggItem.instance);
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
