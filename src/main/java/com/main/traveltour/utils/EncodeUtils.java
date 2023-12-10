package com.main.traveltour.utils;

public class EncodeUtils {

    public static String encodePhoneNumber(String phoneNumber) {
        if (phoneNumber != null && phoneNumber.length() >= 10) {
            String prefix = phoneNumber.substring(0, 4); // Lấy 4 ký tự đầu tiên
            String suffix = phoneNumber.substring(phoneNumber.length() - 2); // Lấy 2 ký tự cuối cùng
            return prefix + "****" + suffix;
        }
        return phoneNumber;
    }
}
