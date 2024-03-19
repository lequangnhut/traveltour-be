package com.main.traveltour.restcontroller.customer.paymentcallbackutils.vnpaycallback;

import com.main.traveltour.configpayment.vnpay.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/customer/booking-tour/")
public class BookingTourVNPayCusController {

    @Autowired
    private VNPayService vnPayService;

    @GetMapping("vnpay/success-payment")
    private String successOrderVNPay(HttpServletRequest request) {
        int paymentStatus = vnPayService.orderReturn(request);
        String tourDetailId = null;

        // Lấy các thông tin cần thiết
        String orderInfo = request.getParameter("vnp_OrderInfo");
        int index = orderInfo.indexOf("TD");
        if (index != -1) {
            String substring = orderInfo.substring(index);
            tourDetailId = substring.substring(0, Math.min(7, substring.length()));
            tourDetailId = tourDetailId.replace("-", "");
        }

        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");
        String bankCode = request.getParameter("vnp_BankCode");

        // Thêm thông tin vào URL redirect
        return paymentStatus == 1 ?
                "redirect:http://localhost:3000/tours/tour-detail/" + tourDetailId + "/booking-tour/customer-information/check-information/payment-success?orderInfo=" + orderInfo + "&paymentTime=" + paymentTime + "&transactionId=" + transactionId + "&totalPrice=" + totalPrice + "&bankCode=" + bankCode :
                "redirect:http://localhost:3000/tours/tour-detail/" + tourDetailId + "/booking-tour/customer-information/check-information/payment-failure?orderInfo=" + orderInfo + "&paymentTime=" + paymentTime + "&transactionId=" + transactionId + "&totalPrice=" + totalPrice + "&bankCode=" + bankCode;
    }

//    @PostMapping("customer/order-hotel/createOrderHotelWithVNPay")
//    private ResponseEntity<Map<String, Object>> createOrderHotelWithVNPay(
//            @RequestPart(value = "orderHotel") OrderHotelCustomerDto orderHotel,
//            @RequestPart(value = "orderDetailsHotel") List<OrderDetailsHotelCustomerDto> orderDetailsHotel) {
//        BigDecimal orderTotal = orderDetailsHotel.stream()
//                .map(orderDetails -> {
//                    Optional<RoomTypes> roomTypes = roomTypeService.findRoomTypeById(orderDetails.getRoomTypeId());
//                    BigDecimal roomPrice = roomTypes.get().getPrice();
//                    BigDecimal amount = BigDecimal.valueOf(orderDetails.getAmount());
//
//                    return roomPrice.multiply(amount);
//                })
//                .reduce(BigDecimal.ZERO, BigDecimal::add);
//
//        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
//        String vnPayUrl = vnPayService.createOrder(orderTotal.intValue(), orderHotel.getId(), baseUrl);
//
//        Map<String, Object> response = new HashMap<>();
//        response.put("redirectUrl", vnPayUrl);
//
//        return new ResponseEntity<>(response, HttpStatus.OK);
//    }
}
