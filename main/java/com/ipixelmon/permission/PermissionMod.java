package com.ipixelmon.permission;

import com.ipixelmon.CommonProxy;
import com.ipixelmon.IMod;
import com.ipixelmon.permission.Client.ClientProxy;
import com.ipixelmon.permission.Server.ServerProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class PermissionMod implements IMod {

    @Override
    public String getID() {
        return "permission";
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {

    }

    @Override
    public void init(FMLInitializationEvent event) {

    }

    @Override
    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new CommandPermission());
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










}
