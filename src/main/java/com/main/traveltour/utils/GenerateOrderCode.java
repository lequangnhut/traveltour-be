package com.main.traveltour.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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

    public static String generateCodeBooking(String paymentMethod, int id) {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyy");
        String formattedDate = dateFormat.format(currentDate);

        Random random = new Random();
        int randomSuffix = random.nextInt(10000);

        return String.format("%s-%d-%s%d", paymentMethod, id, formattedDate, randomSuffix);
    }

    public static String generateCodePayment(String paymentMethod) {
        Date currentDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("ddMMyyyy");
        String formattedDate = dateFormat.format(currentDate);

        Random random = new Random();
        int randomNumber = random.nextInt(1000000);

        return String.format("%s-%s%d", paymentMethod, formattedDate, randomNumber);
    }
}
