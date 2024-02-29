package com.main.traveltour.utils;

public class GenerateNextID {

    public static String generateNextCode(String prefix, String maxCode) {
        if (maxCode == null) {
            return prefix + "0001";
        }

        int lastNumber = Integer.parseInt(maxCode.replaceAll("[^0-9]+", ""));
        return String.format("%s%04d", prefix, lastNumber + 1);
    }


    public static String generateNextInvoiceID(String maxCode) {
        if (maxCode == null) {
            return "000001";
        }

        int nextInvoiceNumber = Integer.parseInt(maxCode) + 1;
        return String.format("%06d", nextInvoiceNumber);
    }

    public static String generateNextContractID(String maxCode) {
        if (maxCode == null) {
            return "HDG" + "00001";
        }

        int lastNumber = Integer.parseInt(maxCode.replaceAll("[^0-9]+", ""));
        return String.format("%s%05d", "HDG", lastNumber + 1);
    }
}
