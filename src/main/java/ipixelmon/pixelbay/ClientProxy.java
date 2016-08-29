package ipixelmon.pixelbay;

import ipixelmon.CommonProxy;
import net.minecraftforge.fml.common.FMLCommonHandler;

public final class ClientProxy extends CommonProxy {

    @Override
    public final void preInit() {

    }

    @Override
    public final void init() {
        FMLCommonHandler.instance().bus().register(new BreakListener());
    }
}
