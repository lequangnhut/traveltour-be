package com.main.traveltour.service;

import java.io.*;
import java.net.*;

public class ZaloNotificationService {

    private static final String ZALO_API_URL = "https://oauth.zaloapp.com/v4/oa/permission?app_id=4488250605950481941&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2F";

    public void sendZaloMessage(String recipientId, String message) {
        try {
            URL url = new URL(ZALO_API_URL);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Authorization", "Bearer 8klOW7IHB7G38urMAbN8");

            con.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes("{\"recipient\":{\"user_id\":\"" + recipientId + "\"},\"message\":{\"text\":\"" + message + "\"}}");
            out.flush();
            out.close();

            int responseCode = con.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            System.out.println("Response: " + response.toString());
        } catch (Exception e) {
            e.printStackTrace(); // Handle exception appropriately
        }
    }
}

