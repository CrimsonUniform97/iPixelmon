package ipixelmon.eggincubator;

import ipixelmon.CommonProxy;
import ipixelmon.IMod;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class EggIncubator implements IMod
{

    // TODO: Egg renders, atleast that works. Now isolate the issue and get obj's working.
    @Override
    public String getID()
    {
        return "eggincubator";
    }

    @Override
    public void preInit()
    {

        GameRegistry.registerBlock(EggBlock.instance, EggBlock.name);
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
        return ipixelmon.eggincubator.ClientProxy.class;
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
