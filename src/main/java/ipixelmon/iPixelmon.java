package ipixelmon;

import ipixelmon.pixelbay.Pixelbay;
import ipixelmon.mysql.MySQLHandler;
import ipixelmon.uuidmanager.UUIDManager;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;
import java.util.List;

@Mod(modid = iPixelmon.MOD_ID, name = iPixelmon.NAME, version = iPixelmon.VERSION)
public final class iPixelmon {

    public static final String MOD_ID = "ipixelmon", NAME = "iPixelmon", VERSION = "dev";

    public static final List<IMod> mods = new ArrayList<IMod>();

    @SidedProxy(clientSide = "ipixelmon.ClientProxy", serverSide = "ipixelmon.ServerProxy")
    public static CommonProxy proxy;

    public static MySQLHandler mysql;
    public static Config config;

    public static SimpleNetworkWrapper network;

    private static int packetID = 0;

    @Mod.Instance(MOD_ID)
    public static iPixelmon instance;

    public iPixelmon() {
        mods.add(new Pixelbay());
        mods.add(new UUIDManager());
    }

    @Mod.EventHandler
    public final void preInit(final FMLPreInitializationEvent event) {
        network = NetworkRegistry.INSTANCE.newSimpleChannel(MOD_ID);

        config = proxy.getConfig();
        mysql = proxy.getMySQL();

        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

        for(IMod mod : mods) mod.preInit();

        proxy.preInit();
    }

    @Mod.EventHandler
    public final void init(final FMLInitializationEvent event) {
        for(IMod mod : mods) mod.init();

        proxy.init();
    }

    public static final void registerPacket(final Class handlerClass, final Class messageClass, final Side side) {
        network.registerMessage(handlerClass, messageClass, packetID++, side);
    }

}
