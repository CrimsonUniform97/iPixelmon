package ipixelmon.pokeegg;

import ipixelmon.CommonProxy;
import ipixelmon.IMod;
import ipixelmon.iPixelmon;
import ipixelmon.pokeegg.egg.PokeEggItem;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;

public class PokeEgg implements IMod
{

    @Override
    public String getID()
    {
        return "pokeegg";
    }

    @Override
    public void preInit()
    {
        GameRegistry.registerItem(PokeEggItem.instance);
    }

    @Override
    public void init()
    {
        iPixelmon.registerPacket(PacketOpenGuiPokeEgg.Handler.class, PacketOpenGuiPokeEgg.class, Side.CLIENT);
    }

    @Override
    public void serverStartingEvent(FMLServerStartingEvent event)
    {

    }

    @Override
    public Class<? extends CommonProxy> clientProxyClass()
    {
        return ipixelmon.pokeegg.client.ClientProxy.class;
    }

    @Override
    public Class<? extends CommonProxy> serverProxyClass()
    {
        return ipixelmon.pokeegg.ServerProxy.class;
    }

    @Override
    public IGuiHandler getGuiHandler()
    {
        return null;
    }
}
