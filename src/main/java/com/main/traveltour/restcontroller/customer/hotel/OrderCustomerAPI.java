package com.main.traveltour.restcontroller.customer.hotel;

import com.main.traveltour.config.DomainURL;
import com.main.traveltour.dto.customer.hotel.OrderDetailsHotelCustomerDto;
import com.main.traveltour.dto.customer.hotel.OrderHotelCustomerDto;
import com.main.traveltour.dto.staff.OrderHotelDetailsDto;
import com.main.traveltour.dto.staff.OrderHotelsDto;
import com.main.traveltour.entity.OrderHotelDetails;
import com.main.traveltour.entity.OrderHotels;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.enums.OrderStatus;
import com.main.traveltour.service.staff.OrderHotelDetailService;
import com.main.traveltour.service.staff.OrderHotelsService;
import com.main.traveltour.service.utils.EmailService;
import com.main.traveltour.utils.Base64Utils;
import com.main.traveltour.utils.EntityDtoUtils;
import com.main.traveltour.utils.GenerateOrderCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1")
public class OrderCustomerAPI {

    @Autowired
    private OrderHotelsService orderHotelsService;

    @Autowired
    private OrderHotelDetailService orderHotelDetailService;

    @Autowired
    private EmailService emailService;

    @PostMapping("customer/booking-hotel/createOrderHotel")
    public ResponseObject createOrderHotel(
            @RequestPart(value = "orderHotel") OrderHotelCustomerDto orderHotel,
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
            String orderStatusBase64 = Base64.getEncoder().encodeToString("1".getBytes());
            String paymentMethodBase64 = Base64.getEncoder().encodeToString("VPO".getBytes());
            String orderIdBase64 = Base64.getEncoder().encodeToString(orderHotel.getId().getBytes());

            emailService.sendEmailBookingHotel(orderHotel, orderDetailsHotel);
            String redirectUrl = "hotel/hotel-details/payment/payment-successful/" + orderStatusBase64 + "/" + paymentMethodBase64 + "/" + orderIdBase64;
            return new ResponseObject("200", "OK", redirectUrl);
        } else {
            return new ResponseObject("400", "Phòng này đã hết, vui lòng đặt phòng khác!", null);
        }
    }

    @PostMapping("customer/booking-hotel/createOrderHotelAG")
    public ResponseObject createOrderHotelAG(
            @RequestPart(value = "orderHotel") OrderHotelCustomerDto orderHotel,
            @RequestPart(value = "orderDetailsHotel") List<OrderDetailsHotelCustomerDto> orderDetailsHotel) {

        // Kiểm tra số lượng phòng đặt có vượt quá số lượng phòng khả dụng hay không
        boolean isAnyRoomExceeding = orderDetailsHotel.stream()
                .anyMatch(orderDetail -> orderHotelDetailService.getTotalBookedRooms(orderDetail.getRoomTypeId(), orderHotel.getCheckIn(), orderHotel.getCheckOut(), orderDetail.getAmount()));

        if (isAnyRoomExceeding) {
            // Thêm hóa đơn khách sạn
            OrderHotels orderHotels = EntityDtoUtils.convertToEntity(orderHotel, OrderHotels.class);
            orderHotelsService.saveOrderHotelAgent(orderHotels, orderDetailsHotel);

            // Thêm chi tiết hóa đơn khách sạn
            List<OrderHotelDetails> orderHotelDetails = EntityDtoUtils.convertToDtoList(orderDetailsHotel, OrderHotelDetails.class);

            orderHotelDetails.forEach(orderHotelDetail -> {
                orderHotelDetail.setOrderHotelId(orderHotels.getId());
                orderHotelDetailService.saveOrderHotelDetailsCustomer(orderHotelDetail);
            });
            String orderStatusBase64 = Base64.getEncoder().encodeToString("1".getBytes());
            String paymentMethodBase64 = Base64.getEncoder().encodeToString("TTTT".getBytes());
            String orderIdBase64 = Base64.getEncoder().encodeToString(orderHotel.getId().getBytes());

            emailService.sendEmailBookingHotel(orderHotel, orderDetailsHotel);
            String redirectUrl = "hotel/hotel-details/payment/payment-successful/" + orderStatusBase64 + "/" + paymentMethodBase64 + "/" + orderIdBase64;
            return new ResponseObject("200", "OK", redirectUrl);
        } else {
            return new ResponseObject("400", "Phòng này đã hết, vui lòng đặt phòng khác!", null);
        }
    }


    @GetMapping("customer/booking-hotel/findOrderHotelById")
    public ResponseObject findOrderHotelById(@RequestParam("orderId") String orderId) {
        OrderHotels orderHotels = orderHotelsService.findById(orderId);
        System.out.println(orderId + " " + orderHotels);

        return new ResponseObject("200", "OK", orderHotels);
    }
}
