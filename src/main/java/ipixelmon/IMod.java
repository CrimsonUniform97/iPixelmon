package ipixelmon;

import net.minecraftforge.fml.common.network.IGuiHandler;

public interface IMod {

    String getID();

    void preInit();
    void init();

    Class<? extends CommonProxy> clientProxyClass();
    Class<? extends CommonProxy> serverProxyClass();

    IGuiHandler getGuiHandler();

}
