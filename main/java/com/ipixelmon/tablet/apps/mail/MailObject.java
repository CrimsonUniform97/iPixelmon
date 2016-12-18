package com.ipixelmon.tablet.apps.mail;

import java.util.Date;

/**
 * Created by colby on 12/14/2016.
 */
public class MailObject implements Comparable<MailObject> {

    private Date sentDate;
    private String sender, message;
    private boolean read;

    public MailObject(Date sentDate, String sender, String message, boolean read) {
        this.sentDate = sentDate;
        this.sender = sender;
        this.message = message;
        this.read = read;
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

    public boolean isRead() {
        return read;
    }

    @Override
    public int compareTo(MailObject o) {
        if (getSentDate() == null || o.getSentDate() == null)
            return 0;

        int dateComparison = getSentDate().compareTo(o.getSentDate());
        int readComparison = (o.isRead() == isRead() ? 0 : (isRead() ? 1 : -1));

        return dateComparison + readComparison;
    }
}
