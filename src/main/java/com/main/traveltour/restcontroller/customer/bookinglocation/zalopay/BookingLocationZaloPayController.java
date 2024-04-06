package com.main.traveltour.restcontroller.customer.bookinglocation.zalopay;

import com.main.traveltour.config.DomainURL;
import com.main.traveltour.configpayment.momo.shared.utils.LogUtils;
import com.main.traveltour.configpayment.zalopay.ZaloPayUtil;
import com.main.traveltour.dto.customer.visit.BookingLocationCusDto;
import com.main.traveltour.dto.staff.OrderVisitsDto;
import com.main.traveltour.entity.*;
import com.main.traveltour.service.UsersService;
import com.main.traveltour.service.agent.OrderVisitDetailService;
import com.main.traveltour.service.agent.VisitLocationTicketService;
import com.main.traveltour.service.staff.OrderVisitLocationService;
import com.main.traveltour.service.utils.EmailService;
import com.main.traveltour.utils.Base64Utils;
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
@RequestMapping("api/v1/customer/booking-location/")
public class BookingLocationZaloPayController {


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

    @PostMapping("zalopay/submit-payment")
    public ResponseEntity<Map<String, Object>> submitZALOPayPayment(@RequestPart OrderVisitsDto orderVisitsDto) {

        List<VisitLocationTickets> locationTickets = visitLocationTicketService.findByVisitLocationId(orderVisitsDto.getVisitLocationId());
        BigDecimal adultPrice = BigDecimal.ZERO;
        BigDecimal childrenPrice = BigDecimal.ZERO;

        for (VisitLocationTickets tickets : locationTickets) {
            String ticketName = tickets.getTicketTypeName().toLowerCase();
            if (ticketName.equals("vé người lớn")) {
                adultPrice = tickets.getUnitPrice();
            } else if (ticketName.equals("vé trẻ em")) {
                childrenPrice = tickets.getUnitPrice();
            }
        }
        int capacityAdult = orderVisitsDto.getCapacityAdult();
        int capacityKid = orderVisitsDto.getCapacityKid();

        BigDecimal totalAdultPrice = adultPrice.multiply(BigDecimal.valueOf(capacityAdult));
        BigDecimal totalChildrenPrice = childrenPrice.multiply(BigDecimal.valueOf(capacityKid));

        BigDecimal orderTotal = totalAdultPrice.add(totalChildrenPrice);

        SessionAttr.ORDER_LOCATIONS_DTO = orderVisitsDto;

        Map<String, Object> response = new HashMap<>();

        // Use ZaloPayUtil to create and submit the order to ZaloPay
        Map<String, Object> order = ZaloPayUtil.createZaloPayOrder(orderTotal.intValue(), orderVisitsDto.getId().toString());
        String paymentUrl = ZaloPayUtil.submitPayment(order);

        if (paymentUrl == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "Error processing ZaloPay payment."));
        }
        String returnURL = DomainURL.BACKEND_URL + "/api/v1/customer/booking-location/zaloPay/success-payment";
        response.put("redirectUrl", paymentUrl);
        return ResponseEntity.ok(response);
    }

    @PostMapping("zaloPay/success-payment")
    private String successBookingTransportMomo(HttpServletRequest request) {
        System.out.println("success =================================================================");
        // Parse the request parameters sent by ZaloPay
        String appTransId = request.getParameter("apptransid"); // Replace with actual ZaloPay parameter name if different
        String zpTransId = request.getParameter("zptranstoken"); // Replace with actual ZaloPay parameter name if different

//        LogUtils.logInfo("ZaloPay Successful Payment Callback: appTransId=" + appTransId + ", zpTransId=" + zpTransId);
//
//        // Validate payment with ZaloPay
//        boolean isValid = ZaloPayUtil.validateCallback(appTransId, zpTransId);
        boolean isValid = appTransId != null || zpTransId != null;
        OrderVisits orderVisitSuccess = null;
        OrderVisitsDto orderVisitsDto = SessionAttr.ORDER_LOCATIONS_DTO;

        List<VisitLocationTickets> locationTickets = visitLocationTicketService.findByVisitLocationId(orderVisitsDto.getVisitLocationId());
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
            orderVisits.setCustomerPhone(user.getPhone());
            orderVisits.setCustomerEmail(user.getEmail());
            orderVisits.setOrderTotal(orderTotal);
            orderVisits.setDateCreated(new Timestamp(System.currentTimeMillis()));

            if (isValid) {
                orderVisits.setOrderStatus(1); // đã thanh toán
                orderVisitSuccess = orderVisitLocationService.save(orderVisits);
            } else {
                orderVisits.setOrderStatus(2); // thất bại
                orderVisitSuccess = orderVisitLocationService.save(orderVisits);
            }

            if (capacityAdult > 0) {
                saveOrderVisitDetail(orderVisits, adultPrice, capacityAdult, adultTicketId);
            }
            if (capacityKid > 0) {
                saveOrderVisitDetail(orderVisits, childrenPrice, capacityKid, childrenTicketId);
            }
            //nhớ gửi mail
            emailService.queueEmailBookingLocation(new BookingLocationCusDto(orderVisitsDto, orderVisits));

        } catch (Exception e) {
            e.printStackTrace();
        }

        assert orderVisitSuccess != null;
        String orderStatusBase64 = Base64Utils.encodeData(String.valueOf(orderVisitSuccess.getOrderStatus()));
        String orderVisitLocationIdBase64 = Base64Utils.encodeData(String.valueOf(orderVisitSuccess.getVisitLocationId()));
        String orderIdBase64 = Base64Utils.encodeData(String.valueOf(orderVisitSuccess.getId()));
        return "redirect:" + DomainURL.FRONTEND_URL + "/tourism-location/tourism-location-detail/" + orderVisitLocationIdBase64 + "/booking-location/customer-information/check-information/payment-success?orderVisitId=" + orderIdBase64 + "&orderStatus=" + orderStatusBase64 + "&paymentMethod=ZALOPAY";
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