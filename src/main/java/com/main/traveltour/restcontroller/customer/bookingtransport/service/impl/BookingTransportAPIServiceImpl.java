package com.main.traveltour.restcontroller.customer.bookingtransport.service.impl;

import com.main.traveltour.dto.agent.transport.OrderTransportationsDto;
import com.main.traveltour.entity.*;
import com.main.traveltour.restcontroller.customer.bookingtransport.service.BookingTransportAPIService;
import com.main.traveltour.service.UsersService;
import com.main.traveltour.service.agent.OrderTransportDetailService;
import com.main.traveltour.service.agent.OrderTransportService;
import com.main.traveltour.service.agent.TransportScheduleSeatService;
import com.main.traveltour.service.agent.TransportationScheduleService;
import com.main.traveltour.utils.EntityDtoUtils;
import com.main.traveltour.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Service
public class BookingTransportAPIServiceImpl implements BookingTransportAPIService {

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

    @Override
    public OrderTransportations createUserPayment(OrderTransportationsDto orderTransportationsDto, List<Integer> seatNumber, Integer orderStatus) {
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
        orderTransportations.setOrderStatus(orderStatus); // 0: chờ thanh toán | 1: đã thanh toán | 2: thất bại
        orderTransportService.save(orderTransportations);

        schedules.setBookedSeat(schedules.getBookedSeat() + orderTransportationsDto.getAmountTicket()); // set chổ ngồi đã đặt trong lịch trình
        transportationScheduleService.save(schedules);

        createOrderDetailScheduleSeat(schedules,
                orderTransportations.getId(),
                seatNumber);

        return orderTransportations;
    }

    @Override
    public void createOrderDetailScheduleSeat(TransportationSchedules schedules,
                                              String orderTransportId,
                                              List<Integer> seatNumber) {
        for (Integer seatName : seatNumber) {
            List<TransportationScheduleSeats> scheduleSeats = transportScheduleSeatService.findAllBySeatNumberScheduleId(seatName, schedules.getId());

            for (TransportationScheduleSeats seats : scheduleSeats) {
                seats.setIsBooked(Boolean.TRUE);
                seats.setDelayBooking(null);
                transportScheduleSeatService.save(seats);

                OrderTransportationDetails orderTransportDetails = new OrderTransportationDetails();
                orderTransportDetails.setOrderTransportationId(orderTransportId);
                orderTransportDetails.setTransportationScheduleSeatId(seats.getId());
                orderTransportDetailService.save(orderTransportDetails);
            }
        }
    }
}
