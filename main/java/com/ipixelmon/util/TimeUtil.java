package com.ipixelmon.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by colbymchenry on 12/28/16.
 */
public class TimeUtil {

    private static DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

    public Date getCurrentTime() {
        return Calendar.getInstance().getTime();
    }

    public String dateToString(Date date) {
        return df.format(date);
    }

    public Date stringToDate(String str) {
        try {
            return df.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

}
