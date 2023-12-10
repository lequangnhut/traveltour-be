package com.main.traveltour.utils;

import java.security.SecureRandom;
import java.util.Random;

public class DiscountCodeGeneratoUtils {

    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int CODE_LENGTH = 8;

    public static String generateDiscountCode() {
        StringBuilder code = new StringBuilder();
        Random random = new SecureRandom();

        for (int i = 0; i < CODE_LENGTH; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            code.append(CHARACTERS.charAt(randomIndex));
        }

        return code.toString();
    }
}
