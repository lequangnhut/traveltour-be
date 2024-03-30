package com.main.traveltour.restcontroller.customer.bookingtour.zalopay;

import com.main.traveltour.configpayment.zalopay.ZaloPayUtil;
import com.main.traveltour.entity.ResponseObject;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/customer/booking-tour/")
public class ZaloPayCusController {

    @PostMapping("zalopay/submit-payment")
    public ResponseObject submitZALOPayPayment(@RequestBody Map<String, Object> requestData) {
        int price = (int) requestData.get("amount");
        String bookingTourId = String.valueOf(requestData.get("bookingTourId"));

        Map<String, Object> order = ZaloPayUtil.createZaloPayOrder(price, bookingTourId);

        String orderUrl = ZaloPayUtil.submitPayment(order);
        return new ResponseObject("200", "Thành công", orderUrl);
    }

    @GetMapping("zalopay/success-payment")
    public String createCC() {
        return "cc";
    }
}
