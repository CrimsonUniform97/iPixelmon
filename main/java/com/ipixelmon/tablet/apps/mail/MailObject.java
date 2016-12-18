package com.ipixelmon.tablet.apps.mail;

import java.util.Date;

/**
 * Created by colby on 12/14/2016.
 */
public class MailObject implements Comparable<MailObject> {

    private Date sentDate;
    private String sender, message;

    public MailObject(Date sentDate, String sender, String message) {
        this.sentDate = sentDate;
        this.sender = sender;
        this.message = message;
    }

    public Date getSentDate() {
        return sentDate;
    }

    public String getMessage() {
        return message;
    }

    public String getSender() {
        return sender;
    }

    @Override
    public int compareTo(MailObject o) {
        if (getSentDate() == null || o.getSentDate() == null)
            return 0;

        return getSentDate().compareTo(o.getSentDate());
    }
}
