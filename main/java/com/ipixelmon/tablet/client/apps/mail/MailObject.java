package com.ipixelmon.tablet.client.apps.mail;

import com.ipixelmon.iPixelmon;
import com.ipixelmon.mysql.SelectionForm;
import com.ipixelmon.tablet.Tablet;
import com.ipixelmon.tablet.client.apps.mail.packet.PacketReceiveMail;
import com.ipixelmon.uuidmanager.UUIDManager;

import java.sql.ResultSet;
import java.util.Date;
import java.util.UUID;

/**
 * Created by colby on 11/18/2016.
 */
public class MailObject implements Comparable {

    public UUID id;
    public boolean read;
    public String message;
    public String playerName;
    public UUID playerUUID;
    public Date date;

    public MailObject(UUID id) {
        ResultSet result = iPixelmon.mysql.selectAllFrom(Tablet.class, new SelectionForm("Mail").where("mailID", id.toString()));

        try {
            if(result.next()) {
                read = result.getBoolean("hasRead");
                message = result.getString("message");
                playerUUID = UUID.fromString(result.getString("sender"));
                playerName = UUIDManager.getPlayerName(playerUUID);
                date = PacketReceiveMail.df.parse(result.getString("sentDate"));
                this.id = id;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int compareTo(Object o) {
        if(o instanceof MailObject) {
            return ((MailObject) o).id.equals(id) ? 0 : 1;
        }
        return 1;
    }
}
