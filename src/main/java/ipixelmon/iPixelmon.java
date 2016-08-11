package ipixelmon;

import ipixelmon.minebay.Minebay;
import ipixelmon.mysql.MySQLHandler;
import ipixelmon.uuidmanager.UUIDManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;

import java.util.ArrayList;
import java.util.List;

@Mod(modid = iPixelmon.MOD_ID, name = iPixelmon.NAME, version = iPixelmon.VERSION)
public final class iPixelmon {

    public static final String MOD_ID = "ipixelmon", NAME = "iPixelmon", VERSION = "dev";

    public static final List<IMod> mods = new ArrayList<IMod>();

    @SidedProxy(clientSide = "ipixelmon.ClientProxy", serverSide = "ipixelmon.ServerProxy")
    public static CommonProxy proxy;

    public static MySQLHandler db;
    public static Config config;

    @Mod.Instance(MOD_ID)
    public static iPixelmon instance;

    public iPixelmon() {
        mods.add(new Minebay());
        mods.add(new UUIDManager());
    }

    @Mod.EventHandler
    public final void preInit(final FMLPreInitializationEvent event) {
        config = proxy.getConfig();
        db = proxy.getMySQL();

        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

        for(IMod mod : mods) mod.preInit();

        proxy.preInit();
    }

    @Mod.EventHandler
    public final void init(final FMLInitializationEvent event) {
        for(IMod mod : mods) mod.init();

        proxy.init();
    }

}
