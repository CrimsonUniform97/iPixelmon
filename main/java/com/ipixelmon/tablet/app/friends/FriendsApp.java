package com.ipixelmon.tablet.app.friends;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.AppBase;
import com.ipixelmon.tablet.AppProxy;
import com.ipixelmon.tablet.app.friends.packet.PacketFriendInfo;
import com.ipixelmon.tablet.app.friends.packet.PacketFriendRequestInfo;
import com.ipixelmon.tablet.app.friends.packet.PacketFriendRequestToServer;
import com.ipixelmon.tablet.app.friends.packet.PacketRemoveFriend;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Created by colby on 12/31/2016.
 */
public class FriendsApp implements AppBase {

    @Override
    public void preInit() {
        iPixelmon.registerPacket(PacketFriendRequestInfo.Handler.class, PacketFriendRequestInfo.class, Side.CLIENT);
        iPixelmon.registerPacket(PacketFriendInfo.Handler.class, PacketFriendInfo.class, Side.CLIENT);
        iPixelmon.registerPacket(PacketFriendRequestToServer.Handler.class, PacketFriendRequestToServer.class, Side.SERVER);
        iPixelmon.registerPacket(PacketRemoveFriend.Handler.class, PacketRemoveFriend.class, Side.SERVER);
    }

    @Override
    public void init() {

    }

    @Override
    public String getName() {
        return "Friends";
    }

    @Override
    public Class<? extends AppProxy> clientProxyClass() {
        return ClientProxy.class;
    }

    @Override
    public Class<? extends AppProxy> serverProxyClass() {
        return ServerProxy.class;
    }

}
