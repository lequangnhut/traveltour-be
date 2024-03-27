package com.main.traveltour.restcontroller.customer.bookingtransport;

import com.main.traveltour.dto.agent.transport.OrderTransportationsDto;
import com.main.traveltour.entity.OrderTransportations;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.TransportationSchedules;
import com.main.traveltour.restcontroller.customer.bookingtransport.service.BookingTransportAPIService;
import com.main.traveltour.service.agent.OrderTransportService;
import com.main.traveltour.service.agent.TransportationScheduleService;
import com.main.traveltour.service.utils.EmailService;
import com.main.traveltour.utils.EntityDtoUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/customer/transport/")
public class BookingTransportCusAPI {

    @Autowired
    private OrderTransportService orderTransportService;

    @Autowired
    private BookingTransportAPIService bookingTransportAPIService;

    @Autowired
    private TransportationScheduleService transportationScheduleService;

    @Autowired
    private EmailService emailService;

    @PostMapping("create-booking-transport/{seatNumber}")
    private ResponseObject createBookingTransport(@RequestBody OrderTransportationsDto orderTransportationsDto,
                                                  @PathVariable List<Integer> seatNumber) {
        OrderTransportations orderTransportationSuccess;
        TransportationSchedules schedules = transportationScheduleService.findBySchedulesId(orderTransportationsDto.getTransportationScheduleId());

        try {
            Integer userId = orderTransportationsDto.getUserId();
            BigDecimal orderTotal = new BigDecimal(orderTransportationsDto.getAmountTicket()).multiply(new BigDecimal(schedules.getUnitPrice().intValue()));

            if (userId != null) {
                OrderTransportations orderTransportations = EntityDtoUtils.convertToEntity(orderTransportationsDto, OrderTransportations.class);
                orderTransportations.setOrderTotal(orderTotal);
                orderTransportations.setDateCreated(new Timestamp(System.currentTimeMillis()));
                orderTransportations.setOrderStatus(0); // chờ thanh toán
                orderTransportationSuccess = orderTransportService.save(orderTransportations);

                schedules.setBookedSeat(schedules.getBookedSeat() + orderTransportationsDto.getAmountTicket()); // set chổ ngồi đã đặt trong lịch trình
                transportationScheduleService.save(schedules);

                bookingTransportAPIService.createOrderDetailScheduleSeat(schedules, orderTransportations.getId(), seatNumber);
            } else {
                orderTransportationSuccess = bookingTransportAPIService.createUserPayment(orderTransportationsDto, seatNumber, 0); // chờ thanh toán
            }

            emailService.queueEmailCustomerBookingTransport(orderTransportationsDto);
            return new ResponseObject("200", "Thành công", orderTransportationSuccess);
        } catch (Exception e) {
            return new ResponseObject("404", "Thất bại", null);
        }
    }
}
