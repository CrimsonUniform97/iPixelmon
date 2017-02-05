package com.ipixelmon.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by colbymchenry on 12/28/16.
 */
public class DateUtil {

    private static DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public static Date getCurrentTime() {
        return Calendar.getInstance().getTime();
    }

    public  static String dateToString(Date date) {
        return df.format(date);
    }

    public  static Date stringToDate(String str) {
        try {
            return df.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getDifferenceFormatted(Date d2) {
        Date d1 = Calendar.getInstance().getTime();
        long seconds = getDifferenceInSeconds(d1, d2);
        long minutes = getDifferenceInMinutes(d1, d2);
        long hours = getDifferenceInHours(d1, d2);

        String text = "";

        if(hours > 0 || (hours <= 0 && minutes <= 0 && seconds <= 0)) {
            text += hours + " hours, ";
            text += minutes - (hours * 60) + " minutes, ";
            text += seconds - (minutes * 60) + " seconds.";
        } else if (minutes > 0) {
            text += minutes + " minutes, ";
            text += seconds - (minutes * 60) + " seconds.";
        } else if (seconds > 0) {
            text += seconds + " seconds.";
        } else {
            text += "Something went wrong.";
        }

        return text;
    }

    public static long getDifferenceInMilliSeconds(Date d1, Date d2) {
        return d2.getTime() - d1.getTime();
    }

    public static long getDifferenceInSeconds(Date d1, Date d2) {
        return (d2.getTime() - d1.getTime()) / 1000;
    }

    public static long getDifferenceInMinutes(Date d1, Date d2) {
        return getDifferenceInSeconds(d1, d2) / 60;
    }

    public static long getDifferenceInHours(Date d1, Date d2) {
        return getDifferenceInMinutes(d1, d2) / 60;
    }

}
