package com.main.traveltour.utils;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {

    public static Time parseTime(String timeString) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        Date date = sdf.parse(timeString);

        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String timeOnly = timeFormat.format(date);

        return Time.valueOf(timeOnly);
    }
}
