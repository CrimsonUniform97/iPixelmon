package com.ipixelmon.tablet.client.apps.mail.packets;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.tablet.client.App;
import com.ipixelmon.tablet.client.apps.mail.AppConversation;
import com.ipixelmon.tablet.client.apps.mail.Conversation;
import com.ipixelmon.tablet.client.apps.mail.Mail;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by colby on 12/9/2016.
 */
public class PacketReceiveMessage implements IMessage {

    public PacketReceiveMessage() {
    }

    UUID messageID, player;
    String message;

    public PacketReceiveMessage(UUID messageID, UUID player, String message) {
        this.messageID = messageID;
        this.player = player;
        this.message = message;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        messageID = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        player = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        message = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, messageID.toString());
        ByteBufUtils.writeUTF8String(buf, player.toString());
        ByteBufUtils.writeUTF8String(buf, message);
    }

    public static class Handler implements IMessageHandler<PacketReceiveMessage, IMessage> {

        @Override
        public IMessage onMessage(PacketReceiveMessage message, MessageContext ctx) {
            for (Conversation conversation : Mail.messages) {
                if (conversation.messageID.equals(message.messageID)) {
                    conversation.getMessages().add(message.player.toString() + "\\u2666" + message.message);
                    conversation.sync();

                    if(App.getActiveApp() instanceof AppConversation) {
                        AppConversation appConversation = (AppConversation) App.getActiveApp();
                        appConversation.getGuiConversation().setScrollY(appConversation.getGuiConversation().getMaxScrollY());
                    }

                    return null;
                }
            }

            ResultSet result = iPixelmon.mysql.query("SELECT * FROM tabletMessages WHERE messageID='" + message.messageID.toString() + "';");

            try {
                if (result.next()) {
                    iPixelmon.clientDb.query("INSERT INTO tabletMessages (messageID, players, messages) VALUES (" +
                            "'" + message.messageID.toString() + "', " +
                            "'" + result.getString("players") + "', " +
                            "'" + (message.player.toString() + "\\u2666" + message.message + "\\u2665") + "'" +
                            ");");
                }

                Mail.messages.add(new Conversation(message.messageID));
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
