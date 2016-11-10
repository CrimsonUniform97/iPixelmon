package com.ipixelmon.tablet.client.apps.friends.packet;

import com.ipixelmon.PlayerUtil;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.mysql.SelectionForm;
import com.ipixelmon.tablet.Tablet;
import com.ipixelmon.tablet.client.apps.friends.Friend;
import com.ipixelmon.uuidmanager.UUIDManager;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

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
            Set<Friend> friends = new TreeSet<>();

            ResultSet result = iPixelmon.mysql.selectAllFrom(Tablet.class, new SelectionForm("Friends").where("player", ctx.getServerHandler().playerEntity.getUniqueID()));

            try {
                if (result.next())
                    for (String s : result.getString("friends").split(",")) {
                        if (!s.isEmpty()) {
                            UUID uuid = UUID.fromString(s);
                            friends.add(new Friend(uuid, UUIDManager.getPlayerName(uuid), PlayerUtil.isPlayerOnline(uuid)));
                        }
                    }
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if(!friends.isEmpty()) iPixelmon.network.sendTo(new PacketFriendsListRes(friends), ctx.getServerHandler().playerEntity);


            return null;
        }
    }
}
