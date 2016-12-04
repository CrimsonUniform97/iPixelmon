package com.ipixelmon.tablet.client.apps.mail.packets;

import com.ipixelmon.PlayerUtil;
import com.ipixelmon.iPixelmon;
import com.ipixelmon.uuidmanager.UUIDManager;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by colby on 12/3/2016.
 */
public class PacketSendMessage implements IMessage {

    public PacketSendMessage() {
    }

    private String message;
    private UUID participant;

    public PacketSendMessage(String message, UUID participant) {
        this.message = message;
        this.participant = participant;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        message = ByteBufUtils.readUTF8String(buf);
        participant = UUID.fromString(ByteBufUtils.readUTF8String(buf));
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, message);
        ByteBufUtils.writeUTF8String(buf, participant.toString());
    }

    public static class Handler implements IMessageHandler<PacketSendMessage, IMessage> {

        @Override
        public IMessage onMessage(PacketSendMessage message, MessageContext ctx) {
            UUID senderUUID = ctx.getServerHandler().playerEntity.getUniqueID();
            UUID receiverUUID = message.participant;
            ResultSet result = iPixelmon.mysql.query("SELECT * FROM tabletConversations WHERE (participant1='" + senderUUID.toString() + "' AND" +
                    " participant2='" + receiverUUID.toString() + "') OR (participant1='" + receiverUUID.toString() + "' AND participant2='" + senderUUID.toString() + "');");

            boolean isNew = false;

            try {
                if (result.next()) {
                    int parNum = UUID.fromString(result.getString("participant1")).equals(senderUUID) ? 1 : 2;
                    iPixelmon.mysql.query("UPDATE tabletConversations SET messages='" + (result.getString("messages") + parNum + "," + message.message + ";") + "'" +
                            " WHERE participant1='" + result.getString("participant1") + "' AND participant2='" + result.getString("participant2") + "';");
                } else {
                    iPixelmon.mysql.query("INSERT INTO tabletConversations (participant1, participant2, messages) VALUES ('" + senderUUID.toString() + "', '" + receiverUUID.toString() + "', '" + (message.message + ";") + "');");
                    isNew = true;
                }

                int parNum = UUID.fromString(result.getString("participant1")).equals(senderUUID) ? 1 : 2;

            if(PlayerUtil.isPlayerOnline(receiverUUID))
                iPixelmon.network.sendTo(new PacketReceiveMessage(UUID.fromString(result.getString("participant1")),
                        UUID.fromString(result.getString("participant2")), parNum, message.message, isNew), PlayerUtil.getPlayer(receiverUUID));

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

    }
}
