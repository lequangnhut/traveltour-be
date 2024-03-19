package com.main.traveltour.restcontroller.customer.paymentcallbackutils.momocallback;

import com.main.traveltour.utils.RandomUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/customer/booking-tour/")
public class BookingTourMomoCusController {

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
