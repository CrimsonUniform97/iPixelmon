package com.ipixelmon.tablet.app.friends;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.mysql.SelectionForm;
import com.ipixelmon.tablet.Tablet;
import com.ipixelmon.util.DateUtil;
import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.UUID;

/**
 * Created by colby on 1/5/2017.
 */
public class FriendRequest implements Comparable<FriendRequest> {

    private String senderName, receiverName;
    private UUID senderUUID, receiverUUID;

    public FriendRequest(UUID senderUUID, UUID receiverUUID, String senderName, String receiverName) {
        this.senderUUID = senderUUID;
        this.receiverUUID = receiverUUID;
        this.senderName = senderName;
        this.receiverName = receiverName;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public UUID getSenderUUID() {
        return senderUUID;
    }

    public UUID getReceiverUUID() {
        return receiverUUID;
    }

    @SideOnly(Side.SERVER)
    public Date getSentDateServerSideOnly() {
        ResultSet result = iPixelmon.mysql.selectAllFrom(Tablet.class, new SelectionForm("FriendRequests")
        .where("sender", senderUUID.toString()).where("receiver", receiverUUID.toString()));

        try {
            if(result.next()) {
                return DateUtil.stringToDate(result.getString("sentDate"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, getSenderName() + "," + getReceiverName());
        ByteBufUtils.writeUTF8String(buf, getSenderUUID().toString() + "," + getReceiverUUID().toString());
    }

    public static FriendRequest fromBytes(ByteBuf buf) {
        String[] dataNames = ByteBufUtils.readUTF8String(buf).split(",");
        String[] dataUUIDs = ByteBufUtils.readUTF8String(buf).split(",");
        FriendRequest friendRequest = new FriendRequest(
                UUID.fromString(dataUUIDs[0]), UUID.fromString(dataUUIDs[1]),
                dataNames[0], dataNames[1]);
        return friendRequest;
    }

    @Override
    public int compareTo(FriendRequest o) {
        return o.getReceiverUUID().equals(getReceiverUUID()) && o.getSenderUUID().equals(getSenderUUID()) ? 0 : -999;
    }
}
