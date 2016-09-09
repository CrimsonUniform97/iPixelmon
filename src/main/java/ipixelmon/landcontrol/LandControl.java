package ipixelmon.landcontrol;

import ipixelmon.CommonProxy;
import ipixelmon.IMod;
import ipixelmon.iPixelmon;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.List;

public class LandControl implements IMod
{

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
        iPixelmon.registerPacket(PacketOpenRegionInfo.Handler.class, PacketOpenRegionInfo.class, Side.CLIENT);
        iPixelmon.registerPacket(PacketEditMemberRequest.Handler.class, PacketEditMemberRequest.class, Side.SERVER);
        iPixelmon.registerPacket(PacketEditMemberResponse.Handler.class, PacketEditMemberResponse.class, Side.CLIENT);
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
        return null;
    }
}
