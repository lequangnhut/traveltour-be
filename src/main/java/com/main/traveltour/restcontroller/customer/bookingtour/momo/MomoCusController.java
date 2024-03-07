package com.main.traveltour.restcontroller.customer.bookingtour.momo;

import com.main.traveltour.config.URLConfig;
import com.main.traveltour.configpayment.momo.config.Environment;
import com.main.traveltour.configpayment.momo.enums.RequestType;
import com.main.traveltour.configpayment.momo.models.PaymentResponse;
import com.main.traveltour.configpayment.momo.processor.CreateOrderMoMo;
import com.main.traveltour.configpayment.momo.shared.utils.LogUtils;
import com.main.traveltour.entity.TourDetails;
import com.main.traveltour.service.staff.TourDetailsService;
import com.main.traveltour.utils.RandomUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class MomoCusController {

    @Autowired
    private TourDetailsService tourDetailsService;

    @PostMapping("momo/submit-payment")
    private ResponseEntity<Map<String, Object>> submitOrderVNPay(@RequestParam("tourDetailId") String tourDetailId, @RequestParam("bookingTourId") String bookingTourId) throws Exception {
        TourDetails tourDetails = tourDetailsService.findById(tourDetailId);
        BigDecimal unitPriceDecimal = tourDetails.getUnitPrice();
        int price = unitPriceDecimal.intValue();

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
    private String successOrderVNPay(HttpServletRequest request) {
        String payType = request.getParameter("payType");
        int paymentStatus = payType.equals("qr") ? 1 : 0;

        String orderMessage = request.getParameter("orderInfo");
        String tourDetailId = null;
        String orderInfo = null;
        int startIndex = orderMessage.lastIndexOf("-");
        int startIndexOrderInfo = orderMessage.indexOf("#");
        if (startIndex != -1 && startIndexOrderInfo != -1) {
            orderInfo = orderMessage.substring(startIndexOrderInfo + 1);
            tourDetailId = orderMessage.substring(startIndex - 6, startIndex);
        }

        String paymentTime = request.getParameter("responseTime");
        String totalPrice = request.getParameter("amount");
        int transactionId = RandomUtils.RandomOtpValue(5);

        // Thêm thông tin vào URL redirect
        return "redirect:http://localhost:3000/tours/tour-detail/" + tourDetailId + "/booking-tour/customer-information/check-information/" +
                (paymentStatus == 1 ? "payment-success" : "payment-failure") +
                "?orderInfo=" + orderInfo +
                (paymentStatus == 1 ? "&transactionId=" + transactionId : "&transactionId=0") +
                "&paymentTime=" + paymentTime +
                "&totalPrice=" + totalPrice;
    }
}
