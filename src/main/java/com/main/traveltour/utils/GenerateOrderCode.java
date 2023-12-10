package com.main.traveltour.utils;

import java.util.Calendar;
import java.util.Random;

public class GenerateOrderCode {

    public static String generateOrderCode() {
        Random random = new Random();
        int randomNumber = random.nextInt(1000000);

        Calendar currentDate = Calendar.getInstance();
        int day = currentDate.get(Calendar.DAY_OF_MONTH);
        int month = currentDate.get(Calendar.MONTH) + 1; // Tháng bắt đầu từ 0
        int year = currentDate.get(Calendar.YEAR);

        return "DH" + year + month + day + randomNumber;
    }
}
