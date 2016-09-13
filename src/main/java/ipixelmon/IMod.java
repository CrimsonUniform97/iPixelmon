package ipixelmon;

import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;

public interface IMod {

    String getID();

    void preInit();
    void init();
    void serverStartingEvent(FMLServerStartingEvent event);

    Class<? extends CommonProxy> clientProxyClass();
    Class<? extends CommonProxy> serverProxyClass();

    IGuiHandler getGuiHandler();

}
