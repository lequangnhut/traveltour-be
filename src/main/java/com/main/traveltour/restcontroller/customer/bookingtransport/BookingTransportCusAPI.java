package com.main.traveltour.restcontroller.customer.bookingtransport;

import com.main.traveltour.dto.agent.transport.OrderTransportationsDto;
import com.main.traveltour.entity.OrderTransportations;
import com.main.traveltour.entity.ResponseObject;
import com.main.traveltour.entity.TransportationSchedules;
import com.main.traveltour.entity.Users;
import com.main.traveltour.service.UsersService;
import com.main.traveltour.service.agent.OrderTransportDetailService;
import com.main.traveltour.service.agent.OrderTransportService;
import com.main.traveltour.service.agent.TransportScheduleSeatService;
import com.main.traveltour.service.agent.TransportationScheduleService;
import com.main.traveltour.utils.EntityDtoUtils;
import com.main.traveltour.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.relational.core.sql.In;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/customer/transport/")
public class BookingTransportCusAPI {

    @Autowired
    private UsersService usersService;

    @Autowired
    private OrderTransportService orderTransportService;

    @Autowired
    private OrderTransportDetailService orderTransportDetailService;

    @Autowired
    private TransportationScheduleService transportationScheduleService;

    @Autowired
    private TransportScheduleSeatService transportScheduleSeatService;

    @PostMapping("create-booking-transport/{seatNumber}")
    private ResponseObject createBookingTransport(@RequestBody OrderTransportationsDto orderTransportationsDto,
                                                  @PathVariable List<Integer> seatNumber) {
        TransportationSchedules schedules = transportationScheduleService.findBySchedulesId(orderTransportationsDto.getTransportationScheduleId());

        Integer totalTicket = orderTransportationsDto.getAmountTicket() * schedules.getUnitPrice().intValue();

        try {
            Integer userId = orderTransportationsDto.getUserId();

            if (userId != null) {

            } else {
                createUser(orderTransportationsDto);
            }
            return new ResponseObject("200", "Thành công", orderTransportationsDto);
        } catch (Exception e) {
            return new ResponseObject("404", "Thất bại", null);
        }
    }

    private void createUser(OrderTransportationsDto orderTransportationsDto) {
        String email = orderTransportationsDto.getCustomerEmail();
        String fullName = orderTransportationsDto.getCustomerName();
        String phone = orderTransportationsDto.getCustomerPhone();


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

    }
}
