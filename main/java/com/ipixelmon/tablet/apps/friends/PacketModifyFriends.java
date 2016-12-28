package com.ipixelmon.tablet.apps.friends;

import com.ipixelmon.ArrayUtil;
import com.ipixelmon.PlayerUtil;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.mysql.SelectionForm;
import com.ipixelmon.mysql.UpdateForm;
import com.ipixelmon.tablet.Tablet;
import com.ipixelmon.uuidmanager.UUIDManager;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
                    addFriend(ctx.getServerHandler().playerEntity.getUniqueID(), message.player);

                    if (PlayerUtil.isPlayerOnline(message.player)) {
                        iPixelmon.network.sendTo(new PacketFriendStatus(ctx.getServerHandler().playerEntity.getUniqueID(),
                                ctx.getServerHandler().playerEntity.getName(), true), PlayerUtil.getPlayer(message.player));
                    }

                    iPixelmon.network.sendTo(new PacketFriendStatus(message.player, UUIDManager.getPlayerName(message.player),
                            PlayerUtil.isPlayerOnline(message.player)), ctx.getServerHandler().playerEntity);
                } else {
                    removeFriend(ctx.getServerHandler().playerEntity.getUniqueID(), message.player);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

    public static void addFriend(UUID player, UUID friend) throws SQLException {
        ResultSet result = iPixelmon.mysql.selectAllFrom(Tablet.class, new SelectionForm("Friends")
                .where("player", player.toString()));

        List<String> friends = Arrays.asList(ArrayUtil.fromString(result.getString("friends")));
        friends.add(friend.toString());

        iPixelmon.mysql.update(Tablet.class, new UpdateForm("Friends")
                .set("friends", ArrayUtil.toString((String[]) friends.toArray())).where("player", player.toString()));
    }

    public static void removeFriend(UUID player, UUID friend) throws SQLException {
        ResultSet result = iPixelmon.mysql.selectAllFrom(Tablet.class, new SelectionForm("Friends")
                .where("player", player.toString()));

        List<String> friends = Arrays.asList(ArrayUtil.fromString(result.getString("friends")));
        friends.remove(friend.toString());

        iPixelmon.mysql.update(Tablet.class, new UpdateForm("Friends")
                .set("friends", ArrayUtil.toString((String[]) friends.toArray())).where("player", player.toString()));
    }

    public static List<UUID> getFriends(UUID player) {
        ResultSet result = iPixelmon.mysql.selectAllFrom(Tablet.class, new SelectionForm("Friends")
                .where("player", player.toString()));

        List<UUID> friends = new ArrayList<>();
        try {
            for (String s : ArrayUtil.fromString(result.getString("friends")))
                friends.add(UUID.fromString(s));

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return friends;
    }

}
