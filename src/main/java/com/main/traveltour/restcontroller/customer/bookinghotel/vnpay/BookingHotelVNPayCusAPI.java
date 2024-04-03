package com.main.traveltour.restcontroller.customer.bookinghotel.vnpay;

import com.main.traveltour.config.DomainURL;
import com.main.traveltour.configpayment.vnpay.VNPayService;
import com.main.traveltour.dto.customer.booking.BookingDto;
import com.main.traveltour.dto.customer.booking.BookingToursDto;
import com.main.traveltour.dto.customer.hotel.OrderDetailsHotelCustomerDto;
import com.main.traveltour.dto.customer.hotel.OrderHotelCustomerDto;
import com.main.traveltour.entity.*;
import com.main.traveltour.enums.OrderStatus;
import com.main.traveltour.service.UsersService;
import com.main.traveltour.service.agent.RoomTypeService;
import com.main.traveltour.service.customer.BookingTourService;
import com.main.traveltour.service.staff.OrderHotelDetailService;
import com.main.traveltour.service.staff.OrderHotelsService;
import com.main.traveltour.service.staff.TourDetailsService;
import com.main.traveltour.service.utils.EmailService;
import com.main.traveltour.utils.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.*;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/customer/booking-hotel/")
public class BookingHotelVNPayCusAPI {

    @Autowired
    private TourDetailsService tourDetailsService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private RoomTypeService roomTypeService;

    @Autowired
    private OrderHotelsService orderHotelsService;

    @Autowired
    private OrderHotelDetailService orderHotelDetailService;

    @Autowired
    private EmailService emailService;
    @PostMapping("vnPay/hotel/submit-payment")
    private ResponseEntity<Map<String, Object>> submitOrderVNPay(@RequestPart(value = "orderHotel") OrderHotelCustomerDto orderHotel,
                                                                 @RequestPart(value = "orderDetailsHotel") List<OrderDetailsHotelCustomerDto> orderDetailsHotel) {
        // Kiểm tra số lượng phòng đặt có vượt quá số lượng phòng khả dụng hay không
        boolean isAnyRoomExceeding = orderDetailsHotel.stream()
                .anyMatch(orderDetail -> orderHotelDetailService.getTotalBookedRooms(orderDetail.getRoomTypeId(), orderHotel.getCheckIn(), orderHotel.getCheckOut(), orderDetail.getAmount()));
        if (isAnyRoomExceeding) {
            // Thêm hóa đơn khách sạn
            OrderHotels orderHotels = EntityDtoUtils.convertToEntity(orderHotel, OrderHotels.class);
            orderHotelsService.saveOrderHotelCustomer(orderHotels, orderDetailsHotel);

            // Thêm chi tiết hóa đơn khách sạn
            List<OrderHotelDetails> orderHotelDetails = EntityDtoUtils.convertToDtoList(orderDetailsHotel, OrderHotelDetails.class);

            orderHotelDetails.forEach(orderHotelDetail -> {
                orderHotelDetail.setOrderHotelId(orderHotels.getId());
                orderHotelDetailService.saveOrderHotelDetailsCustomer(orderHotelDetail);
            });
            // Tính tổng giá tiền sản phẩm
            BigDecimal orderTotal = orderDetailsHotel.stream()
                    .map(orderDetails -> {
                        Optional<RoomTypes> roomTypes = roomTypeService.findRoomTypeById(orderDetails.getRoomTypeId());
                        if (roomTypes.isPresent()) {
                            BigDecimal roomPrice = roomTypes.get().getPrice();
                            BigDecimal amount = BigDecimal.valueOf(orderDetails.getAmount());
                            return roomPrice.multiply(amount);
                        } else {
                            return BigDecimal.ZERO;
                        }
                    })
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
            baseUrl += "/api/v1/customer/booking-hotel/vnPay/success-payment";
            String vnPayUrl = vnPayService.createOrder(orderTotal.intValue(), orderHotel.getId(), baseUrl);

            Map<String, Object> response = new HashMap<>();
            response.put("redirectUrl", vnPayUrl);

            emailService.sendEmailBookingHotel(orderHotel, orderDetailsHotel);

            SessionAttr.ORDER_HOTEL = orderHotel;
            SessionAttr.ORDER_DETAILS_HOTEL = orderDetailsHotel;
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("vnPay/success-payment")
    public String paymentHotelVNPaySuccess(HttpServletRequest request) {
        int paymentStatus = vnPayService.orderReturn(request);

        OrderHotelCustomerDto orderHotel = SessionAttr.ORDER_HOTEL;
        List<OrderDetailsHotelCustomerDto> orderDetailsHotel = SessionAttr.ORDER_DETAILS_HOTEL;

        var orderStatus = 0;
        if (paymentStatus == 1) {
            // Cập nhật lại trạng thái hóa đơn
            OrderHotels orderHotels = orderHotelsService.findById(orderHotel.getId());

            if (orderHotels != null) {
                orderHotels.setOrderStatus(OrderStatus.PROCESSING.getValue());
                orderHotelsService.save(orderHotels);

                String encodedData = Base64.getEncoder().encodeToString(orderHotels.getId().getBytes());
            }
            orderStatus = 1;
        } else {
            orderStatus = 2;
        }

        String orderStatusBase64 = Base64.getEncoder().encodeToString(String.valueOf(orderStatus).getBytes());
        String paymentMethodBase64 = Base64.getEncoder().encodeToString("VNPAY".getBytes());
        String orderIdBase64 = Base64.getEncoder().encodeToString(orderHotel.getId().getBytes());

        SessionAttr.ORDER_HOTEL = null;
        SessionAttr.ORDER_DETAILS_HOTEL = null;
        return "redirect:" + DomainURL.FRONTEND_URL + "/hotel/hotel-details/payment/payment-successful/" + orderStatusBase64 + "/" + paymentMethodBase64 + "/" + orderIdBase64;
    }
}
