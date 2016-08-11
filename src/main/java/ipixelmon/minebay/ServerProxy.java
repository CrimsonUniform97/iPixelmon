package ipixelmon.minebay;

import ipixelmon.CommonProxy;
import net.minecraftforge.common.MinecraftForge;

public final class ServerProxy extends CommonProxy {
    @Override
    public final void preInit() {

    }

    @Override
    public final void init() {
        MinecraftForge.EVENT_BUS.register(new ServerBreakListener());
    }
}
