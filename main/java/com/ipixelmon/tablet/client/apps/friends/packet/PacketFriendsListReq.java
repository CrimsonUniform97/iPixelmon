package com.ipixelmon.tablet.client.apps.friends.packet;

import com.ipixelmon.PlayerUtil;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.mysql.SelectionForm;
import com.ipixelmon.tablet.Tablet;
import com.ipixelmon.tablet.client.apps.friends.Friend;
import com.ipixelmon.tablet.client.apps.friends.FriendsAPI;
import com.ipixelmon.uuidmanager.UUIDManager;
import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

/**
 * Created by colbymchenry on 11/4/16.
 */
public class PacketFriendsListReq implements IMessage {

    public PacketFriendsListReq() {
    }

    @Override
    public void fromBytes(ByteBuf buf) {

    }

    @Override
    public void toBytes(ByteBuf buf) {

    }

    public static class Handler implements IMessageHandler<PacketFriendsListReq, IMessage> {

        @Override
        public IMessage onMessage(PacketFriendsListReq message, MessageContext ctx) {
            Set<Friend> friends = FriendsAPI.getFriends(ctx.getServerHandler().playerEntity.getUniqueID());
            iPixelmon.network.sendTo(new PacketFriendsListRes(friends), ctx.getServerHandler().playerEntity);
            return null;
        }
    }
}