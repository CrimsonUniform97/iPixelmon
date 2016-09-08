package ipixelmon.landcontrol;

import ipixelmon.CommonProxy;
import ipixelmon.IMod;
import net.minecraftforge.fml.common.network.IGuiHandler;

import java.util.ArrayList;
import java.util.List;

public class LandControl implements IMod
{

    public static final List<Region> regions = new ArrayList<>();

    @Override
    public String getID()
    {
        return "landcontrol";
    }

    @Override
    public void preInit()
    {

    }

    @Override
    public void init()
    {

    }

    @Override
    public Class<? extends CommonProxy> clientProxyClass()
    {
        return ipixelmon.landcontrol.client.ClientProxy.class;
    }

    @Override
    public Class<? extends CommonProxy> serverProxyClass()
    {
        return ipixelmon.landcontrol.server.ServerProxy.class;
    }

    @Override
    public IGuiHandler getGuiHandler()
    {
        return new GuiHandler();
    }
}
