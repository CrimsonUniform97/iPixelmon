package com.ipixelmon.tablet;

import com.google.common.collect.Lists;
import com.ipixelmon.CommonProxy;
import com.ipixelmon.IMod;
import com.ipixelmon.tablet.app.friends.FriendsApp;
import com.ipixelmon.tablet.app.gallery.GalleryApp;
import com.ipixelmon.tablet.app.pixelbay.PixelbayApp;
import com.ipixelmon.tablet.client.ClientProxy;
import com.ipixelmon.tablet.server.ServerProxy;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;

/**
 * Created by colbymchenry on 10/3/16.
 */
public class Tablet implements IMod {

    @Override
    public String getID() {
        return  "tablet";
    }

    public static List<AppBase> apps = Lists.newArrayList();

    // TODO: Test and add other apps
    public Tablet() {
        apps.add(new FriendsApp());
        apps.add(new GalleryApp());
        apps.add(new PixelbayApp());
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        for(AppBase app : apps) app.preInit();
    }

    @Override
    public void init(FMLInitializationEvent event) {

        for(AppBase app : apps) app.init();
    }

    @Override
    public void serverStarting(FMLServerStartingEvent event) {

    }

    @Override
    public void serverStarted(FMLServerStartedEvent event) {

    }

    @Override
    public Class<? extends CommonProxy> clientProxyClass() {
        return ClientProxy.class;
    }

    @Override
    public Class<? extends CommonProxy> serverProxyClass() {
        return ServerProxy.class;
    }

    @Override
    public IGuiHandler getGuiHandler() {
        return null;
    }

    @SideOnly(Side.CLIENT)
    public static void displayApp(Class<? extends AppBase> appClass, Object... parameters) {
        try {
            AppProxy proxy = appClass.newInstance().clientProxyClass().newInstance();
            if(proxy != null) {
                Object gui = proxy.getGuiForApp(parameters);
                if (gui != null) {
                    if (gui instanceof AppGui) {
                        Minecraft.getMinecraft().displayGuiScreen((AppGui) gui);
                    }
                }
            }
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

}
