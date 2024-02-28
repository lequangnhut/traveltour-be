package com.main.traveltour.restcontroller.customer.vnpay;

import com.main.traveltour.configpayment.vnpay.VNPayService;
import com.main.traveltour.dto.customer.CustomerInfoDto;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.utils.EntityDtoUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class VNPayController {

    @Autowired
    VNPayService vnPayService;

    @Autowired
    HttpSession session;

    @Autowired
    HttpServletRequest request;

    @PostMapping("vnpay/submit-payment")
    private ResponseEntity<Map<String, Object>> submitOrderVNPay(@RequestParam("price") int orderTotal, @RequestParam("orderInfo") String orderInfo) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnPayUrl = vnPayService.createOrder(orderTotal, orderInfo, baseUrl);

        Map<String, Object> response = new HashMap<>();
        response.put("redirectUrl", vnPayUrl);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("vnpay/success-payment")
    private String successOrderVNPay(HttpServletRequest request) {
        int paymentStatus = vnPayService.orderReturn(request);

        // Lấy các thông tin cần thiết
        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");
        String bankCode = request.getParameter("vnp_BankCode");

        // Thêm thông tin vào URL redirect
        // Thêm thông tin vào URL redirect
        return paymentStatus == 1 ?
                "redirect:/#!/gio-hang/xac-nhan-thong-tin-don-hang/thanh-toan/thanh-cong?orderInfo=" + orderInfo + "&paymentTime=" + paymentTime + "&transactionId=" + transactionId + "&totalPrice=" + totalPrice + "&bankCode=" + bankCode :
                "redirect:/#!/gio-hang/xac-nhan-thong-tin-don-hang/thanh-toan/that-bai?orderInfo=" + orderInfo + "&paymentTime=" + paymentTime + "&transactionId=" + transactionId + "&totalPrice=" + totalPrice + "&bankCode=" + bankCode;
    }
}
