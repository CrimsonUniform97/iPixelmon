package com.ipixelmon.tablet;

import com.ipixelmon.CommonProxy;
import com.ipixelmon.IMod;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.apps.mail.packet.PacketReceiveMail;
import com.ipixelmon.tablet.apps.mail.packet.PacketSendMail;
import com.ipixelmon.tablet.apps.mail.packet.PacketSendResponse;
import com.ipixelmon.tablet.client.ClientProxy;
import com.ipixelmon.tablet.apps.friends.packet.*;
import com.ipixelmon.tablet.notification.PacketNotification;
import com.ipixelmon.tablet.server.ServerProxy;
import net.minecraftforge.fml.common.event.FMLServerStartedEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;

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
        // friends packets
        iPixelmon.registerPacket(PacketFriendsListReq.Handler.class, PacketFriendsListReq.class, Side.SERVER);
        iPixelmon.registerPacket(PacketFriendsListRes.Handler.class, PacketFriendsListRes.class, Side.CLIENT);
        iPixelmon.registerPacket(PacketAddFriendReq.Handler.class, PacketAddFriendReq.class, Side.SERVER);
        iPixelmon.registerPacket(PacketAddFriendRes.Handler.class, PacketAddFriendRes.class, Side.CLIENT);
        iPixelmon.registerPacket(PacketAcceptDeny.Handler.class, PacketAcceptDeny.class, Side.SERVER);
        iPixelmon.registerPacket(PacketNotification.Handler.class, PacketNotification.class, Side.CLIENT);
        iPixelmon.registerPacket(PacketRemoveFriend.Handler.class, PacketRemoveFriend.class, Side.SERVER);

        // mail packets
        iPixelmon.registerPacket(PacketSendMail.Handler.class, PacketSendMail.class, Side.SERVER);
        iPixelmon.registerPacket(PacketReceiveMail.Handler.class, PacketReceiveMail.class, Side.CLIENT);
        iPixelmon.registerPacket(PacketSendResponse.Handler.class, PacketSendResponse.class, Side.CLIENT);
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

}
