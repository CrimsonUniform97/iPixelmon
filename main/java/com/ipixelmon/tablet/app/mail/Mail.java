package com.ipixelmon.tablet.app.mail;

import com.ipixelmon.util.DateUtil;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.Date;
import java.util.UUID;

public class Mail implements Comparable<Mail>{

    private Date date;
    private UUID sender;
    private String senderName;
    private String message;

    public Mail() {}

    public Mail(Date date, UUID sender, String senderName, String message) {
        this.date = date;
        this.sender = sender;
        this.senderName = senderName;
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public UUID getSender() {
        return sender;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getMessage() {
        return message;
    }

    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, DateUtil.dateToString(date));
        ByteBufUtils.writeUTF8String(buf, sender.toString());
        ByteBufUtils.writeUTF8String(buf, senderName);
        ByteBufUtils.writeUTF8String(buf, message);
    }

    public void fromBytes(ByteBuf buf) {
        date = DateUtil.stringToDate(ByteBufUtils.readUTF8String(buf));
        sender = UUID.fromString(ByteBufUtils.readUTF8String(buf));
        senderName = ByteBufUtils.readUTF8String(buf);
        message = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public int compareTo(Mail o) {
        return o.sender.equals(sender) && o.message.equals(message) && o.date.equals(date) ? 0 : -999;
    }
}
