package com.main.traveltour.restcontroller.customer.bookingLocation;

import com.main.traveltour.dto.customer.visit.BookingLocationCusDto;
import com.main.traveltour.dto.staff.OrderVisitsDto;
import com.main.traveltour.entity.*;
import com.main.traveltour.service.UsersService;
import com.main.traveltour.service.agent.OrderVisitDetailService;
import com.main.traveltour.service.agent.VisitLocationTicketService;
import com.main.traveltour.service.staff.OrderVisitLocationService;
import com.main.traveltour.service.utils.EmailService;
import com.main.traveltour.utils.EntityDtoUtils;
import com.main.traveltour.utils.RandomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/customer/booking-location/")
public class BookingLocationCusAPI {

    @Autowired
    private UsersService usersService;

    @Autowired
    private OrderVisitLocationService orderVisitLocationService;

    @Autowired
    private OrderVisitDetailService orderVisitDetailsService;

    @Autowired
    private VisitLocationTicketService visitLocationTicketService;

    @Autowired
    private EmailService emailService;

    @PostMapping("/create-book-location")
    public ResponseObject createBookingTour(@RequestPart OrderVisitsDto orderVisitsDto) {
        List<VisitLocationTickets> locationTickets = visitLocationTicketService.findByVisitLocationId(orderVisitsDto.getVisitLocationId());
//        Integer userId = orderVisitsDto.getUserId();
        BigDecimal adultPrice = BigDecimal.ZERO;
        BigDecimal childrenPrice = BigDecimal.ZERO;
        Integer adultTicketId = null;
        Integer childrenTicketId = null;

        try {
            Users user = createUserPayment(orderVisitsDto);

            for (VisitLocationTickets tickets : locationTickets) {
                String ticketName = tickets.getTicketTypeName().toLowerCase();
                if (ticketName.equals("vé người lớn")) {
                    adultPrice = tickets.getUnitPrice();
                    adultTicketId = tickets.getId();
                } else if (ticketName.equals("vé trẻ em")) {
                    childrenPrice = tickets.getUnitPrice();
                    childrenTicketId = tickets.getId();
                }
            }

            int capacityAdult = orderVisitsDto.getCapacityAdult();
            int capacityKid = orderVisitsDto.getCapacityKid();

            BigDecimal totalAdultPrice = adultPrice.multiply(BigDecimal.valueOf(capacityAdult));
            BigDecimal totalChildrenPrice = childrenPrice.multiply(BigDecimal.valueOf(capacityKid));

            BigDecimal orderTotal = totalAdultPrice.add(totalChildrenPrice);

            OrderVisits orderVisits = EntityDtoUtils.convertToEntity(orderVisitsDto, OrderVisits.class);
            orderVisits.setUserId(user.getId());
            orderVisits.setCustomerName(user.getFullName());
            orderVisits.setCustomerCitizenCard(user.getCitizenCard());
            orderVisits.setCustomerPhone(orderVisitsDto.getCustomerPhone());
            orderVisits.setCustomerEmail(user.getEmail());
            orderVisits.setOrderTotal(orderTotal);
            orderVisits.setDateCreated(new Timestamp(System.currentTimeMillis()));
            orderVisits.setOrderStatus(0);
            orderVisits = orderVisitLocationService.save(orderVisits);

            if (capacityAdult > 0) {
                saveOrderVisitDetail(orderVisits, adultPrice, capacityAdult, adultTicketId);
            }
            if (capacityKid > 0) {
                saveOrderVisitDetail(orderVisits, childrenPrice, capacityKid, childrenTicketId);
            }

            emailService.queueEmailBookingLocation(new BookingLocationCusDto(orderVisitsDto, orderVisits));
            return new ResponseObject("200", "Thêm mới thành công", orderVisits);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseObject("500", "Thêm mới thất bại", null);
        }

    }

    private void saveOrderVisitDetail(OrderVisits orderVisits, BigDecimal unitPrice, int capacity, Integer ticketId) {
        OrderVisitDetails detail = new OrderVisitDetails();
        detail.setOrderVisitId(orderVisits.getId());
        detail.setUnitPrice(unitPrice);
        detail.setAmount(capacity);
        detail.setVisitLocationTicketId(ticketId);
        orderVisitDetailsService.save(detail);
    }

    private Users createUserPayment(OrderVisitsDto orderVisitsDto) {
        String email = orderVisitsDto.getCustomerEmail();
        String fullName = orderVisitsDto.getCustomerName();
        String phone = orderVisitsDto.getCustomerPhone();
        Users userPhone = usersService.findByPhone(phone);
        Optional<Users> currentUserOptional = Optional.ofNullable(usersService.findByEmail(email));

        if (currentUserOptional.isPresent()) {
            return currentUserOptional.get();
        } else if (userPhone != null) {
            return userPhone;
        } else {
            Users newUser = new Users();
            newUser.setEmail(email);
            newUser.setPassword(RandomUtils.RandomToken(10));
            newUser.setFullName(fullName);
            newUser.setPhone(phone);
            newUser.setIsActive(Boolean.TRUE);
            usersService.authenticateRegister(newUser);
            return newUser;
        }
    }

}
