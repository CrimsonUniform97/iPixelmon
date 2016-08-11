package ipixelmon.minebay;

import ipixelmon.CommonProxy;
import ipixelmon.IMod;
import net.minecraftforge.fml.common.network.IGuiHandler;

public final class Minebay implements IMod {

    @Override
    public final String getID() {
        return "minebay";
    }

    @Override
    public final void preInit() {

    }

    @Override
    public final void init() {

    }

    @Override
    public final Class<? extends CommonProxy> clientProxyClass() {
        return ipixelmon.minebay.ClientProxy.class;
    }

    @Override
    public final Class<? extends CommonProxy> serverProxyClass() {
        return ipixelmon.minebay.ServerProxy.class;
    }

    @Override
    public final IGuiHandler getGuiHandler() {
        return new ipixelmon.minebay.GuiHandler();
    }

}
