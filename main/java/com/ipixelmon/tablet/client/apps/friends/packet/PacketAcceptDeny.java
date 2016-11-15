package com.ipixelmon.tablet.client.apps.friends.packet;

import com.ipixelmon.PlayerUtil;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.mysql.DeleteForm;
import com.ipixelmon.mysql.InsertForm;
import com.ipixelmon.mysql.SelectionForm;
import com.ipixelmon.mysql.UpdateForm;
import com.ipixelmon.tablet.Tablet;
import com.ipixelmon.tablet.client.apps.friends.Friend;
import com.ipixelmon.tablet.client.apps.friends.FriendsAPI;
import com.ipixelmon.tablet.notification.Notification;
import com.ipixelmon.tablet.notification.PacketNotification;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
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
 * Created by colby on 11/11/2016.
 */
public class PacketAcceptDeny implements IMessage {

    public PacketAcceptDeny() {
    }

    private UUID player;
    private boolean accept;

    public PacketAcceptDeny(UUID player, boolean accept) {
        this.player = player;
        this.accept = accept;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        player = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        accept = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, player.toString());
        buf.writeBoolean(accept);
    }

    public static class Handler implements IMessageHandler<PacketAcceptDeny, IMessage> {

        @Override
        public IMessage onMessage(PacketAcceptDeny message, MessageContext ctx) {
            final EntityPlayerMP playerFrom = ctx.getServerHandler().playerEntity;
            final EntityPlayerMP playerTo = PlayerUtil.getPlayer(message.player);

            try {
                if (message.accept) {
                    updateFriendsList(playerFrom.getUniqueID(), message.player);
                    updateFriendsList(message.player, playerFrom.getUniqueID());
                }
                iPixelmon.mysql.delete(Tablet.class, new DeleteForm("FriendReqs").add("receiver", playerFrom.getUniqueID().toString()).add("sender", message.player.toString()));
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }

            if (playerTo != null)
                iPixelmon.network.sendTo(new PacketAddFriendRes(message.accept ?
                        PacketAddFriendRes.ResponseType.ACCEPT : PacketAddFriendRes.ResponseType.DENY, playerFrom.getUniqueID().toString() + "," + playerFrom.getName()), playerTo);


            iPixelmon.network.sendTo(new PacketAddFriendRes(PacketAddFriendRes.ResponseType.UPDATE, "none"), playerFrom);
            return null;
        }

        private void updateFriendsList(UUID player, UUID friend) throws SQLException {

            Set<UUID> uuids = FriendsAPI.getFriendsUUIDOnly(player);
            uuids.add(friend);

            String s = "";
            for (UUID uuid : uuids) s += uuid.toString() + ",";

            ResultSet result = iPixelmon.mysql.selectAllFrom(Tablet.class, new SelectionForm("Friends").where("player", player.toString()));
            if (result.next()) {
                iPixelmon.mysql.update(Tablet.class, new UpdateForm("Friends").set("friends", s).where("player", player.toString()));
            } else {
                iPixelmon.mysql.insert(Tablet.class, new InsertForm("Friends").add("friends", s).add("player", player.toString()));
            }
        }

    }

}
