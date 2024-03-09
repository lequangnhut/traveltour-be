package com.main.traveltour.utils;

import com.google.gson.Gson;

public class Base64Utils {

    private static final Gson gson = new Gson();

    public static <T> T decodeToObject(String base64Data, Class<T> clazz) {
        // Giải mã dữ liệu từ Base64
        byte[] decodedBytes = java.util.Base64.getDecoder().decode(base64Data);
        // Chuyển đổi byte[] thành chuỗi JSON
        String json = new String(decodedBytes);
        // Chuyển đổi chuỗi JSON thành đối tượng Java sử dụng Gson
        return gson.fromJson(json, clazz);
    }
}

