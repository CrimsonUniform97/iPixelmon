package com.ipixelmon.tablet.apps.friends;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.uuidmanager.UUIDManager;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by colbymchenry on 12/27/16.
 */
public class PacketModifyFriends implements IMessage {

    private UUID player;
    private boolean add;

    public PacketModifyFriends() {
    }

    public PacketModifyFriends(UUID player, boolean add) {
        this.player = player;
        this.add = add;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        player = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        add = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, player.toString());
        buf.writeBoolean(add);
    }

    public static class Handler implements IMessageHandler<PacketModifyFriends, IMessage> {

        @Override
        public IMessage onMessage(PacketModifyFriends message, MessageContext ctx) {
            try {
                if (message.add) {
                    FriendsAPI.Server.addFriend(ctx.getServerHandler().playerEntity.getUniqueID(), message.player);

                    if (iPixelmon.util.player.isPlayerOnline(message.player)) {
                        iPixelmon.network.sendTo(new PacketFriendStatus(ctx.getServerHandler().playerEntity.getUniqueID(),
                                ctx.getServerHandler().playerEntity.getName(), true), iPixelmon.util.player.getPlayer(message.player));
                    }
                    iPixelmon.network.sendTo(new PacketFriendStatus(message.player, UUIDManager.getPlayerName(message.player),
                            iPixelmon.util.player.isPlayerOnline(message.player)), ctx.getServerHandler().playerEntity);
                } else {
                    FriendsAPI.Server.removeFriend(ctx.getServerHandler().playerEntity.getUniqueID(), message.player);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

}
