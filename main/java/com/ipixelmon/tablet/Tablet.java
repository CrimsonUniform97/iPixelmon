package com.ipixelmon.tablet;

import com.ipixelmon.CommonProxy;
import com.ipixelmon.IMod;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.client.ClientProxy;
import com.ipixelmon.tablet.client.apps.camera.Gallery;
import com.ipixelmon.tablet.client.apps.friends.PacketFriendsListReq;
import com.ipixelmon.tablet.client.apps.friends.PacketFriendsListRes;
import com.ipixelmon.tablet.notification.Notification;
import com.ipixelmon.tablet.notification.NotificationOverlay;
import com.ipixelmon.tablet.server.ServerProxy;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Created by colbymchenry on 10/3/16.
 */
public class Tablet implements IMod {
    @Override
    public String getID() {
        return  "tablet";
    }

    @Override
    public void preInit() {
        iPixelmon.registerPacket(PacketFriendsListReq.Handler.class, PacketFriendsListReq.class, Side.SERVER);
        iPixelmon.registerPacket(PacketFriendsListRes.Handler.class, PacketFriendsListRes.class, Side.CLIENT);
    }

    @Override
    public void init() {

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
    public static final void submitNotification(Notification notification) {
        NotificationOverlay.instance.addNotification(notification);
    }
}
