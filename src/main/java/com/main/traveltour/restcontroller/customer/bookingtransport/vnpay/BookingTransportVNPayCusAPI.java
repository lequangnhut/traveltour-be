package com.main.traveltour.restcontroller.customer.bookingtransport.vnpay;

import com.main.traveltour.configpayment.vnpay.VNPayService;
import com.main.traveltour.dto.agent.transport.OrderTransportationsDto;
import com.main.traveltour.entity.*;
import com.main.traveltour.service.UsersService;
import com.main.traveltour.service.agent.OrderTransportDetailService;
import com.main.traveltour.service.agent.OrderTransportService;
import com.main.traveltour.service.agent.TransportScheduleSeatService;
import com.main.traveltour.service.agent.TransportationScheduleService;
import com.main.traveltour.service.utils.EmailService;
import com.main.traveltour.utils.EntityDtoUtils;
import com.main.traveltour.utils.RandomUtils;
import com.main.traveltour.utils.SessionAttr;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/customer/transport/")
public class BookingTransportVNPayCusAPI {

    @Autowired
    private UsersService usersService;

    @Autowired
    private OrderTransportDetailService orderTransportDetailService;

    @Autowired
    private OrderTransportService orderTransportService;

    @Autowired
    private TransportationScheduleService transportationScheduleService;

    @Autowired
    private TransportScheduleSeatService transportScheduleSeatService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private VNPayService vnPayService;

    @PostMapping("vnpay/submit-payment")
    private ResponseEntity<Map<String, Object>> submitOrderVNPay(@RequestParam("scheduleId") String scheduleId,
                                                                 @RequestParam("orderInfo") String orderInfo,
                                                                 @RequestParam("amountTicket") int amountTicket,
                                                                 @RequestParam("seatNumber") List<Integer> seatNumber,
                                                                 @RequestBody OrderTransportationsDto orderTransport) {
        TransportationSchedules schedules = transportationScheduleService.findBySchedulesId(scheduleId);
        BigDecimal orderTotal = new BigDecimal(amountTicket).multiply(new BigDecimal(schedules.getUnitPrice().intValue()));

        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        baseUrl += "/api/v1/customer/transport/vnpay/success-payment";
        String vnPayUrl = vnPayService.createOrder(orderTotal.intValue(), orderInfo, baseUrl);

        SessionAttr.ORDER_TRANSPORTATIONS_DTO = orderTransport;
        SessionAttr.SEAT_NUMBER = seatNumber;

        Map<String, Object> response = new HashMap<>();
        response.put("redirectUrl", vnPayUrl);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("vnpay/success-payment")
    private String successBookingTransport(HttpServletRequest request) {
        int paymentStatus = vnPayService.orderReturn(request);

        List<Integer> seatNumber = SessionAttr.SEAT_NUMBER;
        OrderTransportations orderTransportationSuccess = null;
        OrderTransportationsDto orderTransportationsDto = SessionAttr.ORDER_TRANSPORTATIONS_DTO;
        TransportationSchedules schedules = transportationScheduleService.findBySchedulesId(orderTransportationsDto.getTransportationScheduleId());

        try {
            Integer userId = orderTransportationsDto.getUserId();
            BigDecimal orderTotal = new BigDecimal(orderTransportationsDto.getAmountTicket()).multiply(new BigDecimal(schedules.getUnitPrice().intValue()));

            if (paymentStatus == 1) {
                if (userId != null) {
                    OrderTransportations orderTransportations = EntityDtoUtils.convertToEntity(orderTransportationsDto, OrderTransportations.class);
                    orderTransportations.setOrderTotal(orderTotal);
                    orderTransportations.setDateCreated(new Timestamp(System.currentTimeMillis()));
                    orderTransportations.setOrderStatus(1); // đã thanh toán
                    orderTransportationSuccess = orderTransportService.save(orderTransportations);

                    schedules.setBookedSeat(schedules.getBookedSeat() + orderTransportationsDto.getAmountTicket()); // set chổ ngồi đã đặt trong lịch trình
                    transportationScheduleService.save(schedules);

                    createOrderDetailScheduleSeat(schedules, orderTransportations.getId(), seatNumber);
                } else {
                    orderTransportationSuccess = createUserPayment(orderTransportationsDto, seatNumber);
                }
            } else {
                OrderTransportations orderTransportations = EntityDtoUtils.convertToEntity(orderTransportationsDto, OrderTransportations.class);
                orderTransportations.setOrderTotal(orderTotal);
                orderTransportations.setDateCreated(new Timestamp(System.currentTimeMillis()));
                orderTransportations.setOrderStatus(2); // thất bại
                orderTransportationSuccess = orderTransportService.save(orderTransportations);
            }

            emailService.queueEmailCustomerBookingTransport(orderTransportationsDto);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "redirect:http://localhost:3000/drive-move/drive-transport-detail/booking-confirmation/booking-successfully?orderStatus=" + orderTransportationSuccess.getOrderStatus();
    }

    private OrderTransportations createUserPayment(OrderTransportationsDto orderTransportationsDto, List<Integer> seatNumber) {
        String email = orderTransportationsDto.getCustomerEmail();
        String fullName = orderTransportationsDto.getCustomerName();
        String phone = orderTransportationsDto.getCustomerPhone();

        TransportationSchedules schedules = transportationScheduleService.findBySchedulesId(orderTransportationsDto.getTransportationScheduleId());
        BigDecimal orderTotal = new BigDecimal(orderTransportationsDto.getAmountTicket()).multiply(new BigDecimal(schedules.getUnitPrice().intValue()));

        Users userPhone = usersService.findByPhone(phone);
        Optional<Users> currentUserOptional = Optional.ofNullable(usersService.findByEmail(email));

        Users user = currentUserOptional.orElseGet(() -> {
            Users newUser = new Users();
            newUser.setEmail(email);
            newUser.setPassword(RandomUtils.RandomToken(10));
            newUser.setFullName(fullName);
            if (userPhone == null) {
                newUser.setPhone(phone);
            }
            newUser.setIsActive(Boolean.TRUE);
            usersService.authenticateRegister(newUser);
            return newUser;
        });

        OrderTransportations orderTransportations = EntityDtoUtils.convertToEntity(orderTransportationsDto, OrderTransportations.class);
        orderTransportations.setUserId(user.getId());
        orderTransportations.setOrderTotal(orderTotal);
        orderTransportations.setDateCreated(new Timestamp(System.currentTimeMillis()));
        orderTransportations.setOrderStatus(1); // đã thanh toán
        orderTransportService.save(orderTransportations);

        schedules.setBookedSeat(schedules.getBookedSeat() + orderTransportationsDto.getAmountTicket()); // set chổ ngồi đã đặt trong lịch trình
        transportationScheduleService.save(schedules);

        createOrderDetailScheduleSeat(schedules,
                orderTransportations.getId(),
                seatNumber);

        return orderTransportations;
    }

    public void createOrderDetailScheduleSeat(TransportationSchedules schedules,
                                              String orderTransportId,
                                              List<Integer> seatNumber) {
        for (Integer seatName : seatNumber) {
            List<TransportationScheduleSeats> scheduleSeats = transportScheduleSeatService.findAllBySeatNumberScheduleId(seatName, schedules.getId());

            for (TransportationScheduleSeats seats : scheduleSeats) {
                seats.setIsBooked(Boolean.TRUE);
                transportScheduleSeatService.save(seats);

                OrderTransportationDetails orderTransportDetails = new OrderTransportationDetails();
                orderTransportDetails.setOrderTransportationId(orderTransportId);
                orderTransportDetails.setTransportationScheduleSeatId(seats.getId());
                orderTransportDetailService.save(orderTransportDetails);
            }
        }
    }
}
