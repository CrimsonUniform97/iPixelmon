package com.ipixelmon;

import java.util.Calendar;

public class TimedMessage {

    private Object[] message = {"", Calendar.getInstance()};

    public TimedMessage(String message, int duration) {
        this.message[0] = message;
    }

    public String getMessage() {
        if (Calendar.getInstance().compareTo((Calendar) message[1]) < 0)
            return (String) message[0];
        return "";
    }

    public boolean hasMessage() {
        return !getMessage().isEmpty();
    }

    public void setMessage(String message, int duration) {
        this.message[0] = message;
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, duration);
        this.message[1] = calendar;
    }

}
