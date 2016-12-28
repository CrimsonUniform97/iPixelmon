package com.ipixelmon.tablet.apps.friends;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.mysql.DeleteForm;
import com.ipixelmon.mysql.InsertForm;
import com.ipixelmon.tablet.Tablet;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by colbymchenry on 12/27/16.
 */
public class PacketFriendRequestToServer implements IMessage {

    public PacketFriendRequestToServer() {
    }

    private UUID player;
    private boolean delete;

    public PacketFriendRequestToServer(UUID player, boolean delete) {
        this.player = player;
        this.delete = delete;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        player = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        delete = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, player.toString());
        buf.writeBoolean(delete);
    }

    public static class Handler implements IMessageHandler<PacketFriendRequestToServer, IMessage> {

        @Override
        public IMessage onMessage(PacketFriendRequestToServer message, MessageContext ctx) {
            UUID senderID = ctx.getServerHandler().playerEntity.getUniqueID();
            String senderName = ctx.getServerHandler().playerEntity.getName();
            String senderIDString = ctx.getServerHandler().playerEntity.getUniqueID().toString();
            UUID receiverID = message.player;
            String receiverIDString = message.player.toString();

            ResultSet result = iPixelmon.mysql.query("SELECT * FROM tabletFriendRequests " +
                    "WHERE sender='" + senderIDString + "' AND receiver='" + receiverIDString + "';");
            try {
                if (!message.delete) {
                    if (!result.next()) {
                        iPixelmon.mysql.insert(Tablet.class, new InsertForm("FriendRequests")
                                .add("sender", senderIDString).add("receiver", receiverIDString).add("sentDate",
                                        iPixelmon.util.time.dateToString(iPixelmon.util.time.getCurrentTime())));

                        // send friend request to receiver
                        if (iPixelmon.util.player.isPlayerOnline(receiverID)) {
                            iPixelmon.network.sendTo(new PacketFriendRequestToClient(senderName, senderID),
                                    iPixelmon.util.player.getPlayer(receiverID));
                        }
                    }
                } else {
                    if (result.next()) {
                        iPixelmon.mysql.delete(Tablet.class, new DeleteForm("FriendRequests")
                                .add("sender", receiverIDString).add("receiver", senderIDString));
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

    }

}
