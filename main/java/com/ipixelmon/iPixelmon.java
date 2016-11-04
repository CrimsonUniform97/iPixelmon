package com.ipixelmon;

import com.ipixelmon.gym.GymMod;
import com.ipixelmon.landcontrol.LandControl;
import com.ipixelmon.pixelbay.Pixelbay;
import com.ipixelmon.pixelegg.PixelEgg;
import com.ipixelmon.tablet.Tablet;
import com.ipixelmon.uuidmanager.UUIDManager;
import com.ipixelmon.mysql.MySQLHandler;
import com.ipixelmon.team.TeamMod;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Mod(modid = iPixelmon.id, name = iPixelmon.name, version = iPixelmon.version)
public final class iPixelmon {

    public static final String id = "ipixelmon", name = "iPixelmon", version = "dev";

    public static final List<IMod> mods = new ArrayList<IMod>();

    @SidedProxy(clientSide = "com.ipixelmon.ClientProxy", serverSide = "com.ipixelmon.ServerProxy")
    public static CommonProxy proxy;

    public static MySQLHandler mysql;
    public static Config config;
    public static final File path = new File(System.getProperty("user.dir") + "/" + id + "/");

    public static SimpleNetworkWrapper network;

    private static int packetID = 0;

    @Mod.Instance(id)
    public static iPixelmon instance;

    public iPixelmon() {
        mods.add(new UUIDManager());
        mods.add(new Pixelbay());
        mods.add(new LandControl());
        mods.add(new GymMod());
        mods.add(new TeamMod());
        mods.add(new PixelEgg());
        mods.add(new Tablet());
    }

    @Mod.EventHandler
    public final void preInit(final FMLPreInitializationEvent event) {
        path.getParentFile().mkdirs();

        network = NetworkRegistry.INSTANCE.newSimpleChannel(id);

        config = proxy.getConfig();
        mysql = proxy.getMySQL();

        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

        proxy.preInit();

        for(IMod mod : mods) mod.preInit();
    }

    @Mod.EventHandler
    public final void init(final FMLInitializationEvent event) {
        proxy.init();

        for(IMod mod : mods) mod.init();
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event)
    {
        for(IMod mod : mods) mod.serverStarting(event);
    }

    @Mod.EventHandler
    public void serverLoaded(FMLServerStartedEvent event)
    {
        for(IMod mod : mods) mod.serverStarted(event);
    }

    public static final void registerPacket(final Class handlerClass, final Class messageClass, final Side side) {
        network.registerMessage(handlerClass, messageClass, packetID++, side);
    }

}
