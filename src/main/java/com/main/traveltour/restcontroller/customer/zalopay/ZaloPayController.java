package com.main.traveltour.restcontroller.customer.zalopay;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

@RestController
public class ZaloPayController {

    private String key1 = "sdngKKJmqEMzvh5QQcdD2A9XBSKUNaYn";

    @PostMapping("/createZaloPayPayment")
    public ResponseEntity<String> createZaloPayPayment(@RequestBody Map<String, Object> requestData) {
        // Trích xuất dữ liệu từ yêu cầu
        int appId = (Integer) requestData.get("app_id");
        String appUser = (String) requestData.get("app_user");
        long appTime = (long) requestData.get("app_time");
        int amount = (int) requestData.get("amount");
        String appTransId = (String) requestData.get("app_trans_id");
        String bankCode = (String) requestData.get("bank_code");
        String embedData = (String) requestData.get("embed_data");
        String description = (String) requestData.get("description");

        // Tạo chữ ký MAC
        String mac = generateMac(appId, appUser, appTime, amount, appTransId, bankCode, embedData, description);

        // Thêm chữ ký MAC vào dữ liệu
        requestData.put("mac", mac);

        // Trả về dữ liệu đã ký
        return ResponseEntity.ok().body(requestData.toString());
    }

    private String generateMac(int appId, String appUser, long appTime, long amount, String appTransId, String bankCode, String embedData, String description) {
        try {
            // Tạo dữ liệu cần ký
            String dataToSign = String.format("%s|%s|%s|%s|%s|%s|%s|%s", appId, appTransId, appUser, appTime, amount, embedData, description, bankCode);

            // Tạo đối tượng MAC với thuật toán SHA-256
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(key1.getBytes(), "HmacSHA256");
            mac.init(secretKey);

            // Ký dữ liệu và chuyển đổi thành dạng hex
            byte[] macBytes = mac.doFinal(dataToSign.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : macBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            e.printStackTrace();
            return null;
        }
    }
}
