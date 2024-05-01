package com.main.traveltour.restcontroller.customer.bookingtour.momo;

import com.main.traveltour.config.DomainURL;
import com.main.traveltour.configpayment.momo.config.Environment;
import com.main.traveltour.configpayment.momo.enums.RequestType;
import com.main.traveltour.configpayment.momo.models.PaymentResponse;
import com.main.traveltour.configpayment.momo.processor.CreateOrderMoMo;
import com.main.traveltour.configpayment.momo.shared.utils.LogUtils;
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
public class BookingTourMomoCusAPI {

    @Autowired
    private BookingTourAPIService bookingTourAPIService;

    @Autowired
    private TourDetailsService tourDetailsService;

    @Autowired
    private EmailService emailService;

    /**
     * phương thức gọi tới api momo để thanh toán
     *
     * @param bookingDto dữ liệu bên FE trả qua để thanh toán
     */
    @PostMapping("momo/submit-payment")
    private ResponseEntity<Map<String, Object>> submitOrderMomo(@RequestBody BookingDto bookingDto) throws Exception {
        SessionAttr.BOOKING_DTO = bookingDto;
        BookingToursDto bookingToursDto = bookingDto.getBookingToursDto();
        TourDetails tourDetails = tourDetailsService.findById(bookingToursDto.getTourDetailId());

        // tính tiền
        BigDecimal unitPriceDecimal = tourDetails.getUnitPrice();
        int capacityAdult = bookingToursDto.getCapacityAdult();
        int capacityKid = bookingToursDto.getCapacityKid();
        int unitPrice = unitPriceDecimal.intValue();
        BigDecimal orderTotal = BigDecimal.valueOf((capacityAdult * unitPrice) + (capacityKid * (unitPrice * 0.3)));

        String bookingTourId = bookingToursDto.getId();

        Map<String, Object> response = new HashMap<>();

        LogUtils.init();
        String requestId = String.valueOf(System.currentTimeMillis());
        String orderId = String.valueOf(System.currentTimeMillis());

        String orderInfo = "Thanh Toan Don Hang #" + bookingTourId;
        String returnURL = DomainURL.BACKEND_URL + "/api/v1/customer/booking-tour/momo/success-payment";
        String notifyURL = "/api/v1/customer/booking-tour/momo/success-payment";

        Environment environment = Environment.selectEnv("dev");

        PaymentResponse captureWalletMoMoResponse = CreateOrderMoMo.process(environment, orderId, requestId, Long.toString(orderTotal.intValue()), orderInfo, returnURL, notifyURL, "", RequestType.CAPTURE_WALLET, Boolean.TRUE);

        assert captureWalletMoMoResponse != null;
        response.put("redirectUrl", captureWalletMoMoResponse.getPayUrl());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * phương thức giao dịch thành công của momo trả về
     */
    @GetMapping("momo/success-payment")
    private String successBookingMomo(HttpServletRequest request) {
        String payType = request.getParameter("payType");
        int paymentStatus = payType.equals("qr") ? 1 : 0;

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
                } else {
                    bookingTourAPIService.createUser(bookingToursDto, bookingTourCustomersDto, totalAmountBook, 2);
                }
                orderStatus = 2;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        String orderStatusBase64 = Base64Utils.encodeData(String.valueOf(orderStatus));
        String paymentMethodBase64 = Base64Utils.encodeData("MOMO");
        return "redirect:" + DomainURL.FRONTEND_URL + "/tours/tour-detail/booking-tour/customer-information/payment-success?" +
                "orderStatus=" + orderStatusBase64 + "&paymentMethod=" + paymentMethodBase64;
    }
}
