package com.main.traveltour.restcontroller.customer.bookingtour.momo;

import com.main.traveltour.config.URLConfig;
import com.main.traveltour.configpayment.momo.config.Environment;
import com.main.traveltour.configpayment.momo.enums.RequestType;
import com.main.traveltour.configpayment.momo.models.PaymentResponse;
import com.main.traveltour.configpayment.momo.processor.CreateOrderMoMo;
import com.main.traveltour.configpayment.momo.shared.utils.LogUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class MomoCusController {

    @PostMapping("momo/submit-payment")
    private ResponseEntity<Map<String, Object>> submitOrderVNPay(@RequestParam("price") int price, @RequestParam("bookingTourId") String bookingTourId) throws Exception {
        Map<String, Object> response = new HashMap<>();

        LogUtils.init();
        String requestId = String.valueOf(System.currentTimeMillis());
        String orderId = String.valueOf(System.currentTimeMillis());

        String orderInfo = "Thanh Toan Don Hang #" + bookingTourId;
        String returnURL = URLConfig.ConfigUrl + "/api/v1/momo/success-payment";
        String notifyURL = "/api/v1/momo/success-payment";

        Environment environment = Environment.selectEnv("dev");

        PaymentResponse captureWalletMoMoResponse = CreateOrderMoMo.process(environment, orderId, requestId, Long.toString(price), orderInfo, returnURL, notifyURL, "", RequestType.CAPTURE_WALLET, Boolean.TRUE);

        response.put("redirectUrl", captureWalletMoMoResponse.getPayUrl());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("momo/success-payment")
    private String successOrderVNPay() {
        return "Thanh toan thanh cong";
    }
}
