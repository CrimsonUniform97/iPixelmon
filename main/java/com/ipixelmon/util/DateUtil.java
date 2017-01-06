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

}
