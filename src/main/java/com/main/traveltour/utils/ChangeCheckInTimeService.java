package com.main.traveltour.utils;

import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;

@Service
public class ChangeCheckInTimeService {
    public Timestamp changeCheckInTime(Timestamp checkIn) {
        // Khởi tạo một đối tượng Calendar và thiết lập giá trị của checkIn
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(checkIn);

        // Thiết lập giờ và phút thành 12:00:00
        calendar.set(Calendar.HOUR_OF_DAY, 12);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // Chuyển đổi lại thành Timestamp và trả về
        return new Timestamp(calendar.getTimeInMillis());
    }

    public Timestamp changeCheckInTimeSearch(Timestamp checkIn) {
        // Khởi tạo một đối tượng Calendar và thiết lập giá trị của checkIn
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(checkIn);

        // Thiết lập giờ và phút thành 12:00:00
        calendar.set(Calendar.HOUR_OF_DAY, 13);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // Chuyển đổi lại thành Timestamp và trả về
        return new Timestamp(calendar.getTimeInMillis());
    }
    public Timestamp changeCheckOutTimeSearch(Timestamp checkIn) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(checkIn);

        // Thiết lập giờ và phút thành 12:00:00
        calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // Chuyển đổi lại thành Timestamp và trả về
        return new Timestamp(calendar.getTimeInMillis());
    }

    public int getDaysDifference(Timestamp checkIn, Timestamp checkOut) {
        // Chuyển đổi Timestamp thành LocalDate
        LocalDate localCheckIn = checkIn.toLocalDateTime().toLocalDate();
        LocalDate localCheckOut = checkOut.toLocalDateTime().toLocalDate();

        // Tính toán số ngày chênh lệch và chuyển đổi về kiểu int
        return (int) ChronoUnit.DAYS.between(localCheckIn, localCheckOut);
    }
}

