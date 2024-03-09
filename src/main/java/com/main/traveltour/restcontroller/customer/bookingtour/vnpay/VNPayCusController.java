package com.main.traveltour.restcontroller.customer.bookingtour.vnpay;

import com.main.traveltour.configpayment.vnpay.VNPayService;
import com.main.traveltour.dto.customer.hotel.OrderDetailsHotelCustomerDto;
import com.main.traveltour.dto.customer.hotel.OrderHotelCustomerDto;
import com.main.traveltour.entity.RoomTypes;
import com.main.traveltour.entity.TourDetails;
import com.main.traveltour.service.agent.RoomTypeService;
import com.main.traveltour.service.staff.TourDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class VNPayCusController {

    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private TourDetailsService tourDetailsService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    RoomTypeService roomTypeService;

    @PostMapping("vnpay/submit-payment")
    private ResponseEntity<Map<String, Object>> submitOrderVNPay(@RequestParam("tourDetailId") String tourDetailId, @RequestParam("orderInfo") String orderInfo) {
        TourDetails tourDetails = tourDetailsService.findById(tourDetailId);
        BigDecimal unitPriceDecimal = tourDetails.getUnitPrice();
        int orderTotal = unitPriceDecimal.intValue();

        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnPayUrl = vnPayService.createOrder(orderTotal, orderInfo, baseUrl);

        Map<String, Object> response = new HashMap<>();
        response.put("redirectUrl", vnPayUrl);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

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


    @PostMapping("customer/order-hotel/createOrderHotelWithVNPay")
    private ResponseEntity<Map<String, Object>> createOrderHotelWithVNPay(
            @RequestPart(value = "orderHotel") OrderHotelCustomerDto orderHotel,
            @RequestPart(value = "orderDetailsHotel") List<OrderDetailsHotelCustomerDto> orderDetailsHotel) {
        BigDecimal orderTotal = orderDetailsHotel.stream()
                .map(orderDetails -> {
                    Optional<RoomTypes> roomTypes = roomTypeService.findRoomTypeById(orderDetails.getRoomTypeId());
                    BigDecimal roomPrice = roomTypes.get().getPrice();
                    BigDecimal amount = BigDecimal.valueOf(orderDetails.getAmount());

                    return roomPrice.multiply(amount);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnPayUrl = vnPayService.createOrder(orderTotal.intValue(), orderHotel.getId(), baseUrl);

        Map<String, Object> response = new HashMap<>();
        response.put("redirectUrl", vnPayUrl);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
