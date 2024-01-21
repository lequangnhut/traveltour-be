package com.main.traveltour.utils;

public class GenerateNextID {

    public static String generateNextCode(String prefix, String maxCode) {
        if (maxCode == null) {
            return prefix + "0001";
        }

        int lastNumber = Integer.parseInt(maxCode.replaceAll("[^0-9]+", ""));
        return String.format("%s%04d", prefix, lastNumber + 1);
    }
}
