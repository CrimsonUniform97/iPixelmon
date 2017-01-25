package com.ipixelmon;

import com.ipixelmon.gym.GymMod;
import com.ipixelmon.itemdisplay.ItemDisplayMod;
import com.ipixelmon.landcontrol.LandControl;
import com.ipixelmon.mcstats.McStatsMod;
import com.ipixelmon.notification.NotificationMod;
import com.ipixelmon.party.PartyMod;
import com.ipixelmon.permission.PermissionMod;
import com.ipixelmon.pixelegg.PixelEgg;
import com.ipixelmon.quest.QuestMod;
import com.ipixelmon.realestate.RealEstateMod;
import com.ipixelmon.tablet.Tablet;
import com.ipixelmon.uuidmanager.UUIDManager;
import com.ipixelmon.mysql.MySQLHandler;
import com.ipixelmon.team.TeamMod;
import lib.PatPeter.SQLibrary.Database;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Mod(modid = iPixelmon.id, name = iPixelmon.name, version = iPixelmon.version)
public final class iPixelmon {

    public static final String id = "ipixelmon", name = "iPixelmon", version = "dev";

    public static final List<IMod> mods = new ArrayList<IMod>();

    @SidedProxy(clientSide = "com.ipixelmon.ClientProxy", serverSide = "com.ipixelmon.ServerProxy")
    public static CommonProxy proxy;

    @SideOnly(Side.SERVER)
    public static MySQLHandler mysql;
    public static Config config;
    public static final File path = new File(System.getProperty("user.dir") + "/" + id + "/");

    public static SimpleNetworkWrapper network;

    private static int packetID = 0;

    @Mod.Instance(id)
    public static iPixelmon instance;

    public iPixelmon() {
        loadMod(new PermissionMod());
        loadMod(new UUIDManager());
        loadMod(new NotificationMod());
        loadMod(new TeamMod());
        loadMod(new PixelEgg());
        loadMod(new LandControl());
        loadMod(new PartyMod());
        loadMod(new Tablet());
        loadMod(new McStatsMod());
        loadMod(new QuestMod());
        loadMod(new GymMod());
        loadMod(new RealEstateMod());
        loadMod(new ItemDisplayMod());
    }

    @Mod.EventHandler
    public final void preInit(final FMLPreInitializationEvent event) {
        GameRegistry.registerBlock(HiddenBlock.instance);

        path.getParentFile().mkdirs();

        network = NetworkRegistry.INSTANCE.newSimpleChannel(id);

        config = proxy.getConfig();

        if (event.getSide() == Side.SERVER)
            mysql = proxy.getMySQL();

        NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

        proxy.preInit();

        for (IMod mod : mods) mod.preInit(event);
    }

    @Mod.EventHandler
    public final void init(final FMLInitializationEvent event) {
        proxy.init();

        for (IMod mod : mods) mod.init(event);
    }

    @Mod.EventHandler
    public void serverLoad(FMLServerStartingEvent event) {
        for (IMod mod : mods) mod.serverStarting(event);
    }

    @Mod.EventHandler
    public void serverLoaded(FMLServerStartedEvent event) {
        for (IMod mod : mods) mod.serverStarted(event);
    }

    public static final void registerPacket(final Class handlerClass, final Class messageClass, final Side side) {
        network.registerMessage(handlerClass, messageClass, packetID++, side);
    }

    private void loadMod(IMod mod) {
        mods.add(mod);
    }

}
