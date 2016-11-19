package com.ipixelmon.tablet.client.apps.mail;

import java.util.Date;
import java.util.UUID;

/**
 * Created by colby on 11/18/2016.
 */
public class MailObject {

    public boolean read;
    public String message;
    public String playerName;
    public UUID playerUUID;
    public Date date;

    public MailObject(String playerName, UUID playerUUID, Date date, boolean read) {
        this.playerName = playerName;
        this.playerUUID = playerUUID;
        this.date = date;
        this.read = read;
    }

}
