package com.main.traveltour.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    public static String formatDate(Date date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(date);
    }

    public static String formatTimestamp(Timestamp timestamp, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(timestamp.getTime()));
    }

    public static Timestamp convertStringToTimestamp(String dateTimeString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        Date date = sdf.parse(dateTimeString);
        long timeInMillis = date.getTime();
        return new Timestamp(timeInMillis);
    }

    public static String formatTimestamp(String originalDateStr) {
        String newDateString = "";
        SimpleDateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        SimpleDateFormat newFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

        try {
            Date date = originalFormat.parse(originalDateStr);
            newDateString = newFormat.format(date);
        } catch (ParseException e) {
            System.err.println("Format of the original date string is invalid: " + originalDateStr);
            return null;
        }

        return newDateString;
    }

}
