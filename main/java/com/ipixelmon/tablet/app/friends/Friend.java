package com.ipixelmon.tablet.app.friends;

import com.ipixelmon.util.ArrayUtil;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;

import java.util.UUID;

/**
 * Created by colby on 1/5/2017.
 */
public class Friend implements Comparable<Friend> {

    private String name;
    private UUID uuid;
    private boolean online;

    public Friend(String name, UUID uuid, boolean online) {
        this.name = name;
        this.uuid = uuid;
        this.online = online;
    }

    public String getName() {
        return name;
    }

    public UUID getUUID() {
        return uuid;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return getName() + "," + getUUID().toString();
    }

    public static Friend fromString(String s) {
        if(s == null || s.isEmpty() || !s.contains(",")) return null;

        String[] data = s.split(",");
        if(data.length != 2) return null;
        return new Friend(data[0], UUID.fromString(data[1]), false);
    }

    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, getName());
        ByteBufUtils.writeUTF8String(buf, getUUID().toString());
        buf.writeBoolean(isOnline());
    }

    public static Friend fromBytes(ByteBuf buf) {
        return new Friend(ByteBufUtils.readUTF8String(buf),
                UUID.fromString(ByteBufUtils.readUTF8String(buf)),
                buf.readBoolean());
    }

    @Override
    public int compareTo(Friend o) {
        return o.getUUID().equals(getUUID()) ? 0 : -999;
    }


}
