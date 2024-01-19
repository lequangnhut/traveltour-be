package com.main.traveltour.utils;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeUtils {
    public static Time parseTimeString(String timeString) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date parsedDate = dateFormat.parse(timeString);
        return new Time(parsedDate.getTime());
    }
}
