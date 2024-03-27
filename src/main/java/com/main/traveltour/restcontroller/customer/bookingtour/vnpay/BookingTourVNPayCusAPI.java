package com.main.traveltour.restcontroller.customer.bookingtour.vnpay;

import com.main.traveltour.config.DomainURL;
import com.main.traveltour.configpayment.vnpay.VNPayService;
import com.main.traveltour.dto.customer.booking.BookingDto;
import com.main.traveltour.dto.customer.booking.BookingToursDto;
import com.main.traveltour.entity.BookingTours;
import com.main.traveltour.entity.TourDetails;
import com.main.traveltour.restcontroller.customer.bookingtour.service.BookingTourAPIService;
import com.main.traveltour.service.staff.TourDetailsService;
import com.main.traveltour.service.utils.EmailService;
import com.main.traveltour.utils.Base64Utils;
import com.main.traveltour.utils.EntityDtoUtils;
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

@Controller
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("api/v1/customer/booking-tour/")
public class BookingTourVNPayCusAPI {

    @Autowired
    private BookingTourAPIService bookingTourAPIService;

    @Autowired
    private TourDetailsService tourDetailsService;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private VNPayService vnPayService;

    @Autowired
    private EmailService emailService;

    @PostMapping("vnpay/submit-payment")
    private ResponseEntity<Map<String, Object>> submitOrderVNPay(@RequestBody BookingDto bookingDto) {
        SessionAttr.BOOKING_DTO = bookingDto;
        BookingToursDto bookingToursDto = bookingDto.getBookingToursDto();
        TourDetails tourDetails = tourDetailsService.findById(bookingToursDto.getTourDetailId());

        // tính tiền
        BigDecimal unitPriceDecimal = tourDetails.getUnitPrice();
        int capacityAdult = bookingToursDto.getCapacityAdult();
        int capacityKid = bookingToursDto.getCapacityKid();
        int unitPrice = unitPriceDecimal.intValue();
        BigDecimal orderTotal = BigDecimal.valueOf((capacityAdult * unitPrice) + (capacityKid * (unitPrice * 0.3)));

        String orderInfo = bookingToursDto.getId();

        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        baseUrl += "/api/v1/customer/booking-tour/vnpay/success-payment";
        String vnPayUrl = vnPayService.createOrder(orderTotal.intValue(), orderInfo, baseUrl);

        Map<String, Object> response = new HashMap<>();
        response.put("redirectUrl", vnPayUrl);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("vnpay/success-payment")
    private String successBookingTourVNPay(HttpServletRequest request) {
        int paymentStatus = vnPayService.orderReturn(request);

        BookingDto bookingDto = SessionAttr.BOOKING_DTO;
        BookingToursDto bookingToursDto = bookingDto.getBookingToursDto();
        List<Map<String, String>> bookingTourCustomersDto = bookingDto.getBookingTourCustomersDto();

        Integer userId = bookingToursDto.getUserId();
        Integer totalAmountBook = bookingToursDto.getCapacityAdult() + bookingToursDto.getCapacityKid() + bookingToursDto.getCapacityBaby();
        Integer orderStatus = null;

        try {
            if (paymentStatus == 1) {
                if (userId != null) {
                    BookingTours bookingTours = EntityDtoUtils.convertToEntity(bookingToursDto, BookingTours.class);
                    bookingTours.setDateCreated(new Timestamp(System.currentTimeMillis()));
                    bookingTourAPIService.createBookingTour(bookingToursDto, bookingTours, bookingTourCustomersDto, 1);

                    bookingTourAPIService.createInvoices(bookingTours.getId());
                    bookingTourAPIService.createContracts(bookingTours.getId());
                    bookingTourAPIService.decreaseAmountTour(bookingTours.getTourDetailId(), totalAmountBook);
                } else {
                    bookingTourAPIService.createUser(bookingToursDto, bookingTourCustomersDto, totalAmountBook, 1); // thành công
                }
                orderStatus = 1;
                emailService.queueEmailBookingTour(bookingDto);
            } else {
                if (userId != null) {
                    BookingTours bookingTours = EntityDtoUtils.convertToEntity(bookingToursDto, BookingTours.class);
                    bookingTours.setDateCreated(new Timestamp(System.currentTimeMillis()));
                    bookingTourAPIService.createBookingTour(bookingToursDto, bookingTours, bookingTourCustomersDto, 2);

                    bookingTourAPIService.createInvoices(bookingTours.getId());
                    bookingTourAPIService.createContracts(bookingTours.getId());
                    bookingTourAPIService.decreaseAmountTour(bookingTours.getTourDetailId(), totalAmountBook);
                } else {
                    bookingTourAPIService.createUser(bookingToursDto, bookingTourCustomersDto, totalAmountBook, 2);
                }
                orderStatus = 2;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String orderStatusBase64 = Base64Utils.encodeData(String.valueOf(orderStatus));
        String paymentMethodBase64 = Base64Utils.encodeData("VNPAY");
        return "redirect:" + DomainURL.FRONTEND_URL + "/tours/tour-detail/booking-tour/customer-information/payment-success?" +
                "orderStatus=" + orderStatusBase64 + "&paymentMethod=" + paymentMethodBase64;
    }
}
