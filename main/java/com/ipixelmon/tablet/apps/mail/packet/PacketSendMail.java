package com.ipixelmon.tablet.apps.mail.packet;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import org.lwjgl.input.Keyboard;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by colby on 12/14/2016.
 */
public class PacketSendMail implements IMessage {

    public PacketSendMail() {
    }

    public static boolean checkChar(char c) {
        Pattern p = Pattern.compile("[^a-z0-9 ^!-+ ^`~ ^_= -]", Pattern.CASE_INSENSITIVE);
        return p.matcher("" + c).find() && c != Keyboard.KEY_BACK;
    }

    private String message;
    private String[] players;

    public PacketSendMail(String message, String... players) {
        this.message = message;
        this.players = players;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        message = ByteBufUtils.readUTF8String(buf);
        players = ByteBufUtils.readUTF8String(buf).split(",");
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, message);

        String toSend = "";
        for (String s : players) toSend += s + ",";

        ByteBufUtils.writeUTF8String(buf, toSend);
    }

    public static class Handler implements IMessageHandler<PacketSendMail, IMessage> {

        @Override
        public IMessage onMessage(PacketSendMail message, MessageContext ctx) {
            return null;
        }

    }

}
