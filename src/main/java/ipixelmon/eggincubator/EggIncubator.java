package ipixelmon.eggincubator;

import ipixelmon.CommonProxy;
import ipixelmon.IMod;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class EggIncubator implements IMod
{

    public static ItemPokeEgg itemPokeEgg;

    @Override
    public String getID()
    {
        return "eggincubator";
    }

    @Override
    public void preInit()
    {

    }

    @Override
    public void init()
    {
        GameRegistry.registerItem(itemPokeEgg = new ItemPokeEgg(), "pokeEgg");
    }

    @Override
    public void serverStartingEvent(FMLServerStartingEvent event)
    {

    }

    @Override
    public Class<? extends CommonProxy> clientProxyClass()
    {
        return null;
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
