package ipixelmon.gyms;

import ipixelmon.CommonProxy;
import ipixelmon.IMod;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class Gyms implements IMod
{
    @Override
    public String getID()
    {
        return "gyms";
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
        return ipixelmon.gyms.client.ClientProxy.class;
    }

    @Override
    public Class<? extends CommonProxy> serverProxyClass()
    {
        return ipixelmon.gyms.server.ServerProxy.class;
    }

    @Override
    public IGuiHandler getGuiHandler()
    {
        return null;
    }
}